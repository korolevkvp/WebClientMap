package com.korolev_kvp.webservice.util;

import java.util.HashMap;
import java.util.Map;

public class JSONConverter {

    /**
     * Вспомогательный метод для преобразования
     * Map в String в формате JSON
     *
     * @param map объект типа Map<String, String>
     * @return получаемая строка с парами ключ-значение в формате JSON
     */
    public static String mapToJSONString(Map<String, String> map) {
        StringBuilder result = new StringBuilder("{");
        for (String key : map.keySet()) {
            result.append("\n    \"").append(key).append("\": \"").append(map.get(key)).append("\",");
        }
        int last = result.lastIndexOf(",");
        if (last != -1) result.delete(last, last + 1);
        result.append("\n}");
        return new String(result);
    }

    /**
     * Вспомогательный метод для преобразования
     * String в формате JSON в map
     *
     * @param str строка с парами ключ-значение в формате JSON
     * @return полученный объект типа Map<String, String> (null при ошибке)
     */
    public static Map<String, String> JSONStringToMap(String str) {
        HashMap<String, String> map = new HashMap<>();
        while (!str.equals("")) {
            int startChar = str.indexOf("\"") + 1;
            int endChar = startChar;
            char currentChar = 'x';
            while (currentChar != '"') {
                currentChar = str.charAt(endChar);
                endChar += 1;
            }
            String key = str.substring(startChar, endChar - 1);
            startChar = endChar + 3;
            endChar = startChar;
            currentChar = 'x';
            while (currentChar != '"') {
                currentChar = str.charAt(endChar);
                endChar += 1;
            }
            String value = str.substring(startChar, endChar - 1);
            map.put(key, value);
            str = str.substring(endChar + 1);
        }
        return map;
    }
}
