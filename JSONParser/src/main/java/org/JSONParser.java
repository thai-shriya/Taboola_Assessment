package org.example;
import java.util.*;

public class JSONParser {

    private final String json;
    private int index;

    private JSONParser(String json) {
        this.json = json.trim();
        this.index = 0;
    }

    public static Map<String, Object> parse(String json) {
        return (Map<String, Object>) new JSONParser(json).parseValue();
    }

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

    private char peek() {
        return json.charAt(index);
    }

    private void skipWhitespace() {
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
    }

    private String parseString() {
        StringBuilder sb = new StringBuilder();
        index++; // skip starting "
        while (json.charAt(index) != '"') {
            char c = json.charAt(index);
            if (c == '\\') {
                index++;
                c = json.charAt(index);
                switch (c) {
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    default:
                        sb.append(c);
                }
            } else {
                sb.append(c);
            }
            index++;
        }
        index++; // skip closing "
        return sb.toString();
    }

    private Number parseNumber() {
        int startIdx = index;
        while (index < json.length() && (isDigit(json.charAt(index)) || json.charAt(index) == '.' || json.charAt(index) == '-')) {
            index++;
        }
        String numberStr = json.substring(startIdx, index);
        if (numberStr.contains(".")) {
            return Double.parseDouble(numberStr);
        } else {
            return Long.parseLong(numberStr);
        }
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

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

    private Object parseNull() {
        if (json.substring(index).startsWith("null")) {
            index += 4;
            return null;
        }
        throw new RuntimeException("Unexpected token for null value: " + json.substring(index));
    }

    public static void main(String[] args) {
        String testJson = "{\n" + //
                "  \"area-items\": {\n" + //
                "    \"2\": [\n" + //
                "      {\n" + //
                "        \"id\": -250384452623414200,\n" + //
                "        \"title\": \"Probe Agency NIA Convicts 5 Members Of Banned Outfit In Bijnor Blast Case\",\n" + //
                "        \"url\": \"https://www.ndtv.com/india-news/nia-court-convicts-5-simi-members-in-bijnor-ied-blast-case-3120426\",\n" + //
                "        \"description\": \"A special National Investigation Agency (NIA) court in Lucknow has convicted five members of the banned organisation SIMI to commit terrorist acts and sentenced them to rigorous imprisonment in the 2014 Bijnor blast case\",\n" + //
                "        \"publishEpochMillis\": 1656742080000,\n" + //
                "        \"expirationEpochMillis\": 1656914880000,\n" + //
                "        \"modifiedEpochMillis\": null,\n" + //
                "        \"tags\": [\n" + //
                "          \"simi\",\n" + //
                "          \"national investigation agency\",\n" + //
                "          \"bijnor blast case\"\n" + //
                "        ],\n" + //
                "        \"categories\": [\n" + //
                "          \"news\"\n" + //
                "        ],\n" + //
                "        \"flags\": [],\n" + //
                "        \"blocked\": false,\n" + //
                "        \"thumbnail-url\": \"https://c.ndtvimg.com/2022-06/8jh8j9f8_police-generic-_625x300_01_June_22.jpg\",\n" + //
                "        \"publish-date\": \"2022-07-02 11:38:00\",\n" + //
                "        \"expiration-date\": \"2022-07-04 11:38:00\",\n" + //
                "        \"modified-date\": null,\n" + //
                "        \"flag-update-time\": null\n" + //
                "      }\n" + //
                "    ]\n" + //
                "  }\n" + //
                "}\n" + //
                "\n" + //
                ""; 
    
  
    Map<String, Object> output = JSONParser.parse(testJson);

    // Ensure the top-level map contains the "area-items" key
    assert output.containsKey("area-items");

    // Extract the "area-items" map
    Map<String, Object> areaItemsMap = (Map<String, Object>) output.get("area-items");
    assert areaItemsMap != null;

    // Check the "2" key exists in "area-items"
    assert areaItemsMap.containsKey("2");

    // Extract the array associated with the "2" key
    List<Object> itemList = (List<Object>) areaItemsMap.get("2");
    assert itemList != null && !itemList.isEmpty();

    // Extract the first item in the array
    Map<String, Object> firstItem = (Map<String, Object>) itemList.get(0);
    assert firstItem != null;

    // Now, we will generate assertions for each key-value pair:
    assert Long.valueOf(-250384452623414200L).equals(firstItem.get("id"));
    assert "Probe Agency NIA Convicts 5 Members Of Banned Outfit In Bijnor Blast Case".equals(firstItem.get("title"));
    assert "https://www.ndtv.com/india-news/nia-court-convicts-5-simi-members-in-bijnor-ied-blast-case-3120426".equals(firstItem.get("url"));
    assert "A special National Investigation Agency (NIA) court in Lucknow has convicted five members of the banned organisation SIMI to commit terrorist acts and sentenced them to rigorous imprisonment in the 2014 Bijnor blast case".equals(firstItem.get("description"));
    assert Long.valueOf(1656742080000L).equals(firstItem.get("publishEpochMillis"));
    assert Long.valueOf(1656914880000L).equals(firstItem.get("expirationEpochMillis"));
    assert firstItem.get("modifiedEpochMillis") == null;
    
    List<String> tags = (List<String>) firstItem.get("tags");
    assert tags != null && tags.size() == 3 && "simi".equals(tags.get(0)) && "national investigation agency".equals(tags.get(1)) && "bijnor blast case".equals(tags.get(2));

    List<String> categories = (List<String>) firstItem.get("categories");
    assert categories != null && categories.size() == 1 && "news".equals(categories.get(0));
    
    List<String> flags = (List<String>) firstItem.get("flags");
    assert flags != null && flags.isEmpty();
    
    assert Boolean.FALSE.equals(firstItem.get("blocked"));
    assert "https://c.ndtvimg.com/2022-06/8jh8j9f8_police-generic-_625x300_01_June_22.jpg".equals(firstItem.get("thumbnail-url"));
    assert "2022-07-02 11:38:00".equals(firstItem.get("publish-date"));
    assert "2022-07-04 11:38:00".equals(firstItem.get("expiration-date"));
    assert firstItem.get("modified-date") == null;
    assert firstItem.get("flag-update-time") == null;

    }
}
