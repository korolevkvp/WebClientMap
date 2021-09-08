package com.korolev_kvp.webservice.service;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataBaseService {

    // Хранилище данных
    private static Map<String, String> DATA_BASE_MAP = new HashMap<>();

    public static Map<String, String> getDataBaseMap() {
        return DATA_BASE_MAP;
    }

    private final String fileName = "data.json";

    public String getFileName() {
        return fileName;
    }

    private static final Map<String, Double> timeMap = new ConcurrentHashMap<>();

    private static final Thread timeThread;

    // поток для счёта времени и удаления просроченных данных
    static {
        timeThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (String key:
                        timeMap.keySet()) {
                    timeMap.put(key, timeMap.get(key) - 1);
                    if (timeMap.get(key) <= 0) {
                        timeMap.remove(key);
                        DATA_BASE_MAP.remove(key);
                        System.out.println("Данные по ключу \"" + key + "\" удалены из таблицы.");

                    }
                }

            }
        });
        DataBaseService.timeThread.start();
    }


    /**
     * Обновляет данные с заданным ключом,
     * в соответствии с переданным клиентом,
     * если такой уже имеется.
     * Иначе создает новые данные.
     * Время жизни записи устанавливается в 100 секунд.
     *
     * @param key  - ключ для создания
     * @param data - данные для создания
     * @return false если созданы новые данные, true если старые данные обновлены
     */
    public boolean set(String key, String data) {
        return setElement(key, data, 100.);
    }

    /**
     * Обновляет данные с заданным ключом,
     * в соответствии с переданным клиентом,
     * если такой уже имеется.
     * Иначе создает новые данные.
     * Устанавливается передаваемое время жизни записи.
     *
     * @param key  - ключ для создания
     * @param data - данные для создания
     * @param ttl - время жизни записи
     * @return false если созданы новые данные, true если старые данные обновлены
     */
    public boolean set(String key, String data, String ttl) throws NumberFormatException{
        return setElement(key, data, Double.parseDouble(ttl));
    }

    /**
     * Вспомогательный метод для добавления
     * новых данных в хранилище.
     *
     * @return - true - запись удалась, иначе false
     */
    private boolean setElement(String key, String data, Double ttl) {
        boolean updated = DATA_BASE_MAP.containsKey(key);
        DATA_BASE_MAP.put(key, data);
        timeMap.put(key, ttl);
        return updated;
    }

    /**
     * Возвращает список всех имеющихся данных
     *
     * @return список данных
     */
    public Map<String, String> getAll() {
        return DATA_BASE_MAP;
    }

    /**
     * Возвращает клиента по его ключу
     *
     * @param key - ключ клиента
     * @return - объект клиента с заданным ключом
     */
    public String get(String key) {
        return DATA_BASE_MAP.get(key);
    }

    /**
     * Удаляет данные с заданным ключом
     *
     * @param key - ключ клиента, которого нужно удалить
     * @return - true если клиент был удален, иначе false
     */
    public String remove(String key) {
        return DATA_BASE_MAP.remove(key);
    }

    /**
     * Сохраняет текущее состояние хранилища
     * и записывает его в файл
     *
     * @param fileName - имя файла
     * @return - true если запись удалась, иначе false
     */
    public boolean dump(String fileName) {
        return dumpFromFile(fileName);

    }

    /**
     * Сохраняет текущее состояние хранилища
     * и записывает его в файл по умолчанию
     *
     * @return - true если запись удалась, иначе false
     */
    public boolean dump() {
        return dumpFromFile(fileName);

    }

    /**
     * Вспомогательный метод для сохранения
     * текущего состояния хранилища
     * и записи его в файл
     *
     * @return - true если запись удалась, иначе false
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
     * @return - true если загрузка удалась, иначе false
     */
    public boolean load(String fileName) {
        return loadFromFile(fileName);
    }

    /**
     * Загружает текущее состояние хранилища
     * из файла по умолчанию
     *
     * @return - true если загрузка удалась, иначе false
     */
    public boolean load() {
        return loadFromFile(fileName);
    }

    /**
     * Вспомогательный метод для преобразования для
     * загрузки текущее состояние хранилища из файла
     *
     * @return - true если загрузка удалась, иначе false
     */
    private boolean loadFromFile(String fileName) {
        try {
            String content = Files.lines(Paths.get(fileName)).reduce("", String::concat);
            DATA_BASE_MAP = stringToMap(content);
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