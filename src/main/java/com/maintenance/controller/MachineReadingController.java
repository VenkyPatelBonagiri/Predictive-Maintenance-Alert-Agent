package com.maintenance.controller;

import com.maintenance.model.MachineReading;
import com.maintenance.service.MachineReadingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MachineReadingController {

    private final MachineReadingService machineReadingService;

    public MachineReadingController(MachineReadingService machineReadingService) {
        this.machineReadingService = machineReadingService;
    }

    // Bare array of all failure readings — this is the endpoint the n8n HTTP Request node should call
    @GetMapping("/readings")
    public List<MachineReading> getAllReadings() {
        return machineReadingService.getAllFailures();
    }

    // Same data, wrapped with a count — handy for a UI or a quick sanity check
    @GetMapping("/machines")
    public Map<String, Object> getAllMachines() {
        List<MachineReading> failures = machineReadingService.getAllFailures();
        return Map.of("count", failures.size(), "failures", failures);
    }

    // Single reading by UDI, e.g. /api/readings/23
    @GetMapping("/readings/{udi}")
    public ResponseEntity<MachineReading> getReadingByUdi(@PathVariable("udi") int udi) {
        return machineReadingService.getByUdi(udi)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}