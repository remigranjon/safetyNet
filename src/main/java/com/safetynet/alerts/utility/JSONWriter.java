package com.safetynet.alerts.utility;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONWriter {
    final File file;
    final ObjectMapper objectMapper = new ObjectMapper();
    public JSONWriter(String dataFilePath) {
        this.file = new File(getFileFromResources(dataFilePath));
        
    }

    private String getFileFromResources(String filePath) {
        try {
            return getClass().getClassLoader().getResource(filePath).getFile();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("File not found in resources: " + filePath);
        }
    }

    public boolean writeJson(JsonNode data) {
        if (!file.exists() || !file.canWrite()) {
            throw new IllegalArgumentException("File not found or not writable: " + file.getPath());
        }
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error writing JSON", e);
        }
    }
}
