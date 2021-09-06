package com.korolev_kvp.webservice.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class MessageService {

    // Хранилище данных
    private static Map<String, String> MESSAGE_REPOSITORY_MAP = new HashMap<>();

    private final String fileName = "data.json";

    /**
     * Обновляет данные с заданным ключом,
     * в соответствии с переданным клиентом,
     * если такой уже имеется.
     * Иначе создает новые данные
     *
     * @param key  - ключ для создания
     * @param data - данные для создания
     * @return создано(false) или изменено(true)
     */
    public boolean set(String key, String data) {
        boolean updated = MESSAGE_REPOSITORY_MAP.containsKey(key);
        MESSAGE_REPOSITORY_MAP.put(key, data);
        return updated;
    }

    /**
     * Возвращает список всех имеющихся данных
     *
     * @return список данных
     */
    public Map<String, String> getAll() {
        return MESSAGE_REPOSITORY_MAP;
    }

    /**
     * Возвращает клиента по его ключу
     *
     * @param key - ключ клиента
     * @return - объект клиента с заданным ключом
     */
    public String get(String key) {
        return MESSAGE_REPOSITORY_MAP.get(key);
    }

    /**
     * Удаляет данные с заданным ключом
     *
     * @param key - ключ клиента, которого нужно удалить
     * @return - true если клиент был удален, иначе false
     */
    public String remove(String key) {
        return MESSAGE_REPOSITORY_MAP.remove(key);
    }

    /**
     * Сохраняет текущее состояние хранилища
     * и записывает его в файл
     *
     * @param fileName - имя файла
     * @return - true - запись удалась, иначе false
     */
    public boolean dump(String fileName) {
        return dumpFromFile(fileName);

    }

    /**
     * Сохраняет текущее состояние хранилища
     * и записывает его в файл по умолчанию
     *
     * @return - true - запись удалась, иначе false
     */
    public boolean dump() {
        return dumpFromFile(fileName);

    }

    /**
     * Вспомогательный метод для сохранения
     * текущего состояния хранилища
     * и записи его в файл
     *
     * @return - true - запись удалась, иначе false
     */
    private boolean dumpFromFile(String fileName) {
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            final Map<String, String> dataList = getAll();
            byte[] dataToBytes = mapToString(dataList).getBytes();
            outputStream.write(dataToBytes);
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * Загружает текущее состояние хранилища
     * из файла
     *
     * @param fileName - имя файла
     * @return - true - загрузка удалась, иначе false
     */
    public boolean load(String fileName) {
        return loadFromFile(fileName);
    }

    /**
     * Загружает текущее состояние хранилища
     * из файла по умолчанию
     *
     * @return - true - загрузка удалась, иначе false
     */
    public boolean load() {
        return loadFromFile(fileName);
    }

    /**
     * Вспомогательный метод для преобразования для
     * загрузки текущее состояние хранилища из файла
     *
     * @return - true - загрузка удалась, иначе false
     */
    private boolean loadFromFile(String fileName) {
        try {
            String content = Files.lines(Paths.get(fileName)).reduce("", String::concat);
            MESSAGE_REPOSITORY_MAP = stringToMap(content);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Вспомогательный метод для преобразования
     * Map в String в формате JSON
     *
     * @param map - объект типа Map<String, String>
     * @return - получаемая строка с парами ключ-значение в формате JSON
     */
    public String mapToString(Map<String, String> map) {
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
     * @param str - строка с парами ключ-значение в формате JSON
     * @return - полученный объект типа Map<String, String> (null при ошибке)
     */
    public Map<String, String> stringToMap(String str) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        while (!str.equals("")) {
            int k1 = str.indexOf("\"") + 1;
            int k2 = k1;
            char x = 'x';
            while (x != '"') {
                x = str.charAt(k2);
                k2 += 1;
            }
            String key = str.substring(k1, k2 - 1);
            k1 = k2 + 3;
            k2 = k1;
            x = 'x';
            while (x != '"') {
                x = str.charAt(k2);
                k2 += 1;
            }
            String value = str.substring(k1, k2 - 1);
            map.put(key, value);
            str = str.substring(k2 + 1);
        }
        return map;
    }

}