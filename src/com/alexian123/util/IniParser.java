package com.alexian123.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IniParser {
    private final Map<String, Map<String, String>> data = new HashMap<>();

    public IniParser(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String section = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith(";")) {
                    // Skip empty lines and comments
                    continue;
                }
                if (line.startsWith("[") && line.endsWith("]")) {
                    // Section header
                    section = line.substring(1, line.length() - 1);
                    data.putIfAbsent(section, new HashMap<>());
                } else if (section != null && line.contains("=")) {
                    // Key-value pair
                    String[] parts = line.split("=", 2);
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    data.get(section).put(key, value);
                }
            }
        }
    }

    public String get(String section, String key) throws Exception {
        Map<String, String> sectionData = data.get(section);
        if (sectionData != null) {
            String data = sectionData.get(key);
            if (data != null) {
            	return data;
            }
            throw new Exception("Field " + key + " not found in section " + section);
        }
        throw new Exception("Section not found: " + section);
    }
}
