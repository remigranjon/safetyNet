package com.safetynet.alerts.utility;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONReaderNode {

    final File file;

    final ObjectMapper objectMapper = new ObjectMapper();

    public JSONReaderNode(String dataFilePath) {
        this.file = new File(getFileFromResources(dataFilePath));

    }

    private String getFileFromResources(String filePath) {
        try {
            return getClass().getClassLoader().getResource(filePath).getFile();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("File not found in resources: " + filePath);
        }
    }

    public JsonNode getRootNode() {
        if (!file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("File not found or not readable: " + file.getPath());
        }
        try {
            return objectMapper.readTree(file);
        } catch (Exception e) {
            System.err.println("Error reading JSON file: " + file.getPath());
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid JSON", e);
        }
    }
}
