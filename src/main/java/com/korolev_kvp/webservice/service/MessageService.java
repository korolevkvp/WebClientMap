package com.korolev_kvp.webservice.service;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class MessageService {

    // Хранилище данных
    private static final Map<String, String> MESSAGE_REPOSITORY_MAP = new HashMap<>();

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
    public boolean remove(String key) {
        return MESSAGE_REPOSITORY_MAP.remove(key) != null;
    }

    /**
     * Сохраняет текущее состояние хранилища
     * и записывает его в файл
     *
     * @param fileName - имя файла
     * @return - true - запись удалась, иначе false
     */
    public boolean dump(String fileName) {
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
     * Вспомогательный метод для преобразования
     * Map в String в формате JSON
     *
     * @param map - объект типа Map<String, String>
     * @return - получаемая строка с парами ключ-значение в формате JSON
     */
    private String mapToString(Map<String, String> map) {
        StringBuilder result = new StringBuilder("{");
        for (String key : map.keySet()) {
            result.append("\n    \"").append(key).append("\": \"").append(map.get(key)).append("\",");
        }
        int last = result.lastIndexOf(",");
        result.delete(last, last + 1);
        result.append("\n}");
        return new String(result);
    }



}