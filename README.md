# Predictive Maintenance Alert Agent

An AI-powered workflow automation project that analyzes real industrial equipment sensor data, applies rule-based risk scoring grounded in documented failure mechanisms, and uses an LLM to generate plain-language maintenance alerts — routed automatically to email (high risk) or a log sheet (medium risk).

## Overview

Unplanned equipment failure is a major cost and safety risk in industrial and construction settings. This project simulates a lightweight version of a predictive maintenance pipeline: it takes real sensor readings from confirmed equipment failures, scores each one against documented failure mechanisms, has an LLM translate the technical risk into a plain-language explanation and recommended action, and routes the result to the right channel based on severity.

The project combines a self-built REST API (Java/Spring Boot) with a no-code/low-code automation layer (n8n) and an LLM (Claude), reflecting a realistic split between backend engineering and workflow orchestration.

## Architecture

```
┌───────────────────────┐
│  Spring Boot REST API │  Serves real failure records from the
│  (Java, OpenCSV)      │  AI4I 2020 dataset via /api/readings
└───────────┬───────────┘
            │ HTTP GET
            ▼
┌─────────────────────────────────────────────────────────────┐
│                      n8n Workflow                           │
│                                                             │
│  HTTP Request → Preprocess Features → Rule-Based Risk Score │
│         → Filter (drop low risk) → Claude LLM Reasoning     │
│         → Parse LLM Output → Route by Severity (IF node)    │
│                     ├── High risk  → Gmail alert            │
│                     └── Medium risk → Google Sheets log     │
└─────────────────────────────────────────────────────────────┘
```

## Dataset

This project uses the **[AI4I 2020 Predictive Maintenance Dataset](https://archive.ics.uci.edu/dataset/601/ai4i+2020+predictive+maintenance+dataset)** (UCI Machine Learning Repository) — 10,000 synthetic-but-realistic industrial machine readings, including air/process temperature, rotational speed, torque, tool wear, and documented failure labels.

The API filters this down to the **339 rows where `Machine failure = 1`** — i.e., confirmed real failure cases — so the pipeline analyzes actual failure conditions rather than routine, healthy readings.

The dataset documents 5 independent failure modes, each with a known physical trigger condition:
- **TWF** — Tool Wear Failure
- **HDF** — Heat Dissipation Failure
- **PWF** — Power Failure
- **OSF** — Overstrain Failure
- **RNF** — Random Failure (baseline noise, not modeled)

## Backend: Spring Boot REST API

A layered Spring Boot application (Controller → Service → Model) that loads the dataset once at startup and serves it over REST.

**Endpoints:**
| Method | Path | Description |
|---|---|---|
| GET | `/api/readings` | All confirmed failure records (bare array, consumed by n8n) |
| GET | `/api/machines` | Same data, wrapped with a count |
| GET | `/api/readings/{udi}` | Single reading by UDI |

**Tech:** Java 17, Spring Boot 3.2, OpenCSV, Maven

## Risk Scoring Logic

Rather than using arbitrary thresholds, the risk score is built directly from the dataset's own documented failure mechanisms:

| Failure mode | Rule | Points |
|---|---|---|
| Tool Wear | `tool_wear_min >= 200` | +2 |
| Heat Dissipation | `(process_temp - air_temp) < 8.6K` AND `rpm < 1380` | +3 |
| Power | `power (torque × angular velocity) < 3500W or > 9000W` | +3 |
| Overstrain | `tool_wear × torque > type-specific threshold (L:11000 / M:12000 / H:13000)` | +3 |

Total score maps to `low` (0-2), `medium` (3-5), or `high` (6+) risk. Only `medium`/`high` items proceed to the LLM step — this keeps LLM API usage proportional to genuine risk signals rather than processing every row.

## LLM Reasoning

Flagged items are sent to **Claude (Haiku 4.5)** via n8n's native Anthropic node. The prompt supplies the risk score, flagged reasons, and raw sensor values, and asks the model to return a structured JSON response with a plain-language explanation, a recommended action, and an urgency rating — output a non-technical operations manager could act on immediately.

## Routing & Alerting

- **High risk** → Gmail node sends a formatted HTML alert email with machine ID, risk details, explanation, and recommended action
- **Medium risk** → Google Sheets node appends a row to a running log, including the LLM's explanation and the dataset's own ground-truth failure flags (TWF/HDF/PWF/OSF) for later comparison against the rule-based reasoning

Routing decisions are based on the rule-based `risk_level` (deterministic, always available) rather than the LLM's own `urgency` field — keeping the pipeline's control flow independent of LLM output reliability.

## Tech Stack

**Backend:** Java, Spring Boot, OpenCSV, Maven
**Automation:** n8n (self-hosted via Docker)
**AI:** Claude API (Anthropic)
**Data:** AI4I 2020 Predictive Maintenance Dataset (UCI)
**Alerting:** Gmail API, Google Sheets API

## Setup

1. Clone the repo
2. `mvn spring-boot:run` to start the API (runs on `localhost:8080`)
3. Import `n8n-workflow/predictive-maintenance-workflow.json` into your n8n instance
4. Configure credentials in n8n: Anthropic API key, Gmail OAuth2, Google Sheets OAuth2
5. If n8n runs in Docker, point the HTTP Request node to `http://host.docker.internal:8080/api/readings` instead of `localhost`

## Possible Improvements

- Validate rule-based flagged reasons against the dataset's ground-truth failure columns (TWF/HDF/PWF/OSF) to measure detection accuracy
- Add scheduling (poll new readings on an interval rather than one-off runs)
- Persist historical risk trends for a simple dashboard view

## Author

Venky Patel Bonagiri — [LinkedIn](http://linkedin.com/in/venky-patel-bonagiri-298957255) · [GitHub](https://github.com/VenkyPatelBonagiri)
