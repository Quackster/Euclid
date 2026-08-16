package euclid.network.streams;

import euclid.util.extensions.StringExtensions;

import java.util.LinkedHashMap;
import java.util.Map;

public class Request {

    private String header;
    private String content;

    public Request(String header, String content) {
        this.header = header;
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    public String getMessageBody() {
        String consoleText = content;
        for (int i = 0; i < 14; i++) {
            consoleText = consoleText.replace(String.valueOf((char) i), "[" + i + "]");
        }
        return consoleText;
    }

    public void skip(int num) {
        if (content.length() < num) {
            return;
        }
        content = content.substring(num);
    }

    public int getArgumentAmount(String delimeter) {
        if (content.length() == 0) {
            return 0;
        }
        return splitLiteral(content, delimeter).length;
    }

    public int getArgumentAmount() {
        return getArgumentAmount(" ");
    }

    public String getArgument(int index, String delimeter) {
        if (index < 0) {
            index = 0;
        }
        if (getArgumentAmount(delimeter) < 1) {
            return null;
        }
        return splitLiteral(content, delimeter)[index];
    }

    public String getArgument(int index) {
        return getArgument(index, " ");
    }

    public Map<String, String> getKeyValues(String content) {
        Map<String, String> values = new LinkedHashMap<>();
        if (content == null) {
            content = this.content;
        }

        String[] lines = content.split(String.valueOf((char) 13), -1);
        for (String line : lines) {
            if (line.trim().length() < 1) {
                continue;
            }
            if (!line.contains("=")) {
                continue;
            }
            int offset = line.indexOf('=');
            values.put(line.substring(0, offset), StringExtensions.filterInput(line.substring(offset + 1)));
        }
        return values;
    }

    public Map<String, String> getKeyValues() {
        return getKeyValues(null);
    }

    private static String[] splitLiteral(String content, String delimiter) {
        if (content.isEmpty()) {
            return new String[0];
        }
        java.util.List<String> parts = new java.util.ArrayList<>();
        int start = 0;
        int idx;
        while ((idx = content.indexOf(delimiter, start)) != -1) {
            parts.add(content.substring(start, idx));
            start = idx + delimiter.length();
        }
        parts.add(content.substring(start));
        return parts.toArray(new String[0]);
    }
}
