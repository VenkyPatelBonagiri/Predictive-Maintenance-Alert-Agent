package com.maintenance.service;

import com.maintenance.model.MachineReading;
import com.opencsv.CSVReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MachineReadingService {

    private static final String CSV_PATH = "data/ai4i2020.csv";

    private final List<MachineReading> failedReadings;

    public MachineReadingService() {
        this.failedReadings = loadFailuresFromCsv();
        System.out.println("Loaded " + failedReadings.size() + " machine failure records from " + CSV_PATH);
    }

    public List<MachineReading> getAllFailures() {
        return failedReadings;
    }

    public Optional<MachineReading> getByUdi(int udi) {
        return failedReadings.stream().filter(r -> r.getUdi() == udi).findFirst();
    }

    private List<MachineReading> loadFailuresFromCsv() {
        List<MachineReading> results = new ArrayList<>();

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(new ClassPathResource(CSV_PATH).getInputStream(), StandardCharsets.UTF_8))) {

            String[] row;
            boolean isHeader = true;

            while ((row = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false; // skip the header row (UDI, Product ID, Type, ...)
                    continue;
                }

                int machineFailure = Integer.parseInt(row[8].trim());

                // Only keep rows where the machine actually failed
                if (machineFailure != 1) {
                    continue;
                }

                MachineReading reading = new MachineReading(
                        Integer.parseInt(row[0].trim()),          // UDI
                        row[1].trim(),                             // Product ID
                        row[2].trim(),                             // Type (L/M/H)
                        Double.parseDouble(row[3].trim()),         // Air temperature [K]
                        Double.parseDouble(row[4].trim()),         // Process temperature [K]
                        Integer.parseInt(row[5].trim()),           // Rotational speed [rpm]
                        Double.parseDouble(row[6].trim()),         // Torque [Nm]
                        Integer.parseInt(row[7].trim()),           // Tool wear [min]
                        machineFailure,                            // Machine failure
                        Integer.parseInt(row[9].trim()),           // TWF
                        Integer.parseInt(row[10].trim()),          // HDF
                        Integer.parseInt(row[11].trim()),          // PWF
                        Integer.parseInt(row[12].trim()),          // OSF
                        Integer.parseInt(row[13].trim())           // RNF
                );

                results.add(reading);
            }

        } catch (IOException | com.opencsv.exceptions.CsvValidationException e) {
            throw new RuntimeException("Failed to load ai4i2020.csv from resources", e);
        }

        return results;
    }
}