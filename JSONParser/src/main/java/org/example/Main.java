package org.example;

import java.util.*;
import java.util.*;

public class Main {

    // JSON string to be parsed
    private final String json;
    // Current index used to navigate through the JSON string
    private int index;

    // Constructor initializing the JSON string and the index
    private Main(String json) {
        this.json = json.trim();
        this.index = 0;
    }

    // Entry point for parsing, returns a map of the parsed JSON
    public static Map<String, Object> parse(String json) {
        return (Map<String, Object>) new Main(json).parseValue();
    }

    // Parses a value which could be an object, array, string, number, boolean, or null
    private Object parseValue() {
        skipWhitespace();
        char c = peek();
        switch (c) {
            case '{':
                return parseObject();
            case '[':
                return parseArray();
            case '"':
                return parseString();
            case 't':
            case 'f':
                return parseBoolean();
            case 'n':
                return parseNull();
            default:
                if (isDigit(c) || c == '-') {
                    return parseNumber();
                }
                throw new RuntimeException("Unexpected token: " + c);
        }
    }

    // Peeks at the next character in the JSON string without moving the index
    private char peek() {
        return json.charAt(index);
    }

    // Skips whitespace characters in the JSON string
    private void skipWhitespace() {
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
    }

    // Parses a string value from the JSON
    private String parseString() {
        StringBuilder sb = new StringBuilder();
        index++; // skip starting "
        while (json.charAt(index) != '"') {
            sb.append(json.charAt(index));
            index++;
        }
        index++; // skip closing "
        return sb.toString();
    }

    // Parses a number value from the JSON
    private Number parseNumber() {
        int startIdx = index;
        while (index < json.length() && (isDigit(json.charAt(index)) || json.charAt(index) == '.' || json.charAt(index) == '-')) {
            index++;
        }
        String numberStr = json.substring(startIdx, index);
        if (numberStr.contains(".")) {
            return Double.parseDouble(numberStr);
        } else {
            return Integer.parseInt(numberStr);
        }
    }

    // Checks if a character is a digit
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    // Parses an object from the JSON
    private Map<String, Object> parseObject() {
        Map<String, Object> obj = new HashMap<>();
        index++; // skip opening {
        skipWhitespace();
        while (peek() != '}') {
            String key = parseString();
            skipWhitespace();
            if (peek() != ':') {
                throw new RuntimeException("Expected ':' after key in object, got: " + peek());
            }
            index++; // skip :
            skipWhitespace();
            obj.put(key, parseValue());
            skipWhitespace();
            if (peek() == ',') {
                index++; // skip ,
                skipWhitespace();
            }
        }
        index++; // skip closing }
        return obj;
    }

    // Parses an array from the JSON
    private List<Object> parseArray() {
        List<Object> array = new ArrayList<>();
        index++; // skip opening [
        skipWhitespace();
        while (peek() != ']') {
            array.add(parseValue());
            skipWhitespace();
            if (peek() == ',') {
                index++; // skip ,
                skipWhitespace();
            }
        }
        index++; // skip closing ]
        return array;
    }

    // Parses a boolean value from the JSON
    private Boolean parseBoolean() {
        if (json.substring(index).startsWith("true")) {
            index += 4;
            return true;
        } else if (json.substring(index).startsWith("false")) {
            index += 5;
            return false;
        }
        throw new RuntimeException("Unexpected token for boolean value: " + json.substring(index));
    }

    // Parses a null value from the JSON
    private Object parseNull() {
        if (json.substring(index).startsWith("null")) {
            index += 4;
            return null;
        }
        throw new RuntimeException("Unexpected token for null value: " + json.substring(index));
    }

    // Main method for testing purposes
    public static void main(String[] args) {
        String testJson = "{\n" +
                "\"debug\" : \"on\",\n" +
                "\"window\" : {\n" +
                "    \"title\" : \"sample\",\n" +
                "    \"size\": 500\n" +
                "}\n" +
                "}";
        Map<String, Object> output = Main.parse(testJson);
        assert output.get("debug").equals("on");
        assert ((Map<String, Object>) output.get("window")).get("title").equals("sample");
        assert ((Number) ((Map<String, Object>) output.get("window")).get("size")).intValue() == 500;
    }
}
