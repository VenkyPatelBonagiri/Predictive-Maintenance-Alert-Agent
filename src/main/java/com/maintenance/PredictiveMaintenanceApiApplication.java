package com.maintenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PredictiveMaintenanceApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(PredictiveMaintenanceApiApplication.class, args);
        System.out.println("Predictive maintenance API running at http://localhost:8080");
        System.out.println("Try: http://localhost:8080/api/readings");
    }
}