package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.FileModel;
import com.example.budgetmanager.entity.Expense;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;

@Service
public class CsvService {

    public FileModel generateCsvBase64(List<String[]> data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(outputStream)) {

            for (String[] row : data) {
                writer.println(String.join(",", row));
            }
            writer.flush();

            // Convertim fișierul în Base64
            String base64Csv = Base64.getEncoder().encodeToString(outputStream.toByteArray());

            // Returnăm FileModel cu numele fișierului și Base64-ul CSV
            return new FileModel("data.csv", base64Csv);
        } catch (Exception e) {
            throw new RuntimeException("Error while generating CSV", e);
        }
    }


}
