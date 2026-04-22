package com.example.newsBot.summarizer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SummaryParser {

    public enum Section { IT, ECONOMY }

    private static final Pattern NUMBERED_LINE = Pattern.compile("^\\s*(\\d+)[.)]\\s*(.+)$");

    public Map<Section, List<String>> parse(String response) {
        Map<Section, List<String>> result = new EnumMap<>(Section.class);
        result.put(Section.IT, new ArrayList<>());
        result.put(Section.ECONOMY, new ArrayList<>());

        if (response == null || response.isBlank()) return result;

        Section current = null;
        for (String raw : response.split("\\R")) {
            String line = raw.trim();
            if (line.isEmpty()) continue;

            Section header = detectHeader(line);
            if (header != null) {
                current = header;
                continue;
            }
            if (current == null) continue;

            Matcher m = NUMBERED_LINE.matcher(line);
            if (m.matches()) {
                result.get(current).add(m.group(2).trim());
            }
        }
        return result;
    }

    private Section detectHeader(String line) {
        String stripped = line.replaceAll("[\\[\\]\\s]", "");
        if (stripped.contains("IT") || stripped.contains("과학")) return Section.IT;
        if (stripped.contains("경제")) return Section.ECONOMY;
        return null;
    }
}
