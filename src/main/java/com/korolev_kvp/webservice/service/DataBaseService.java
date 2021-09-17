package com.korolev_kvp.webservice.service;

import com.korolev_kvp.webservice.util.CSVReader;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс, описывающий работу сервиса.
 * В нём находится хранилище и методы для его изменения.
 */
@Service
public class DataBaseService {

    // Хранилище данных
    private static Map<String, String> DATA_BASE_MAP = new HashMap<>();

    public static Map<String, String> getDataBaseMap() {
        return DATA_BASE_MAP;
    }

    public static void setDataBaseMap(Map<String, String> dataBaseMap) {
        DATA_BASE_MAP = dataBaseMap;
    }

    private final String fileName = "DataBase.csv";

    public String getFileName() {
        return fileName;
    }

    private static Map<String, Double> timeMap = new ConcurrentHashMap<>();

    private static final Thread timeThread;

    // поток для счёта времени и удаления просроченных данных
    static {
        timeThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException | ConcurrentModificationException ignored) {}
                try {
                    for (String key : timeMap.keySet()) {
                        timeMap.put(key, timeMap.get(key) - 0.1);
                        if (timeMap.get(key) <= 0) {
                            timeMap.remove(key);
                            DATA_BASE_MAP.remove(key);
                            System.out.println("Данные по ключу \"" + key + "\" удалены из таблицы.");
                        }
                    }
                } catch (ConcurrentModificationException ignored) {}

            }
        });
        DataBaseService.timeThread.start();
    }


    /**
     * Обновляет данные с заданным ключом,
     * в соответствии с переданным клиентом,
     * если такой уже имеется,
     * иначе создает новые данные.
     * Время жизни записи устанавливается в 100 секунд.
     *
     * @param key  ключ для создания
     * @param data данные для создания
     * @return false если созданы новые данные, true если старые данные обновлены
     */
    public boolean set(String key, String data) {
        return setElement(key, data, 100.);
    }

    /**
     * Обновляет данные с заданным ключом,
     * в соответствии с переданным клиентом,
     * если такой уже имеется,
     * иначе создает новые данные.
     * Устанавливается передаваемое время жизни записи.
     *
     * @param key  - ключ для создания
     * @param data - данные для создания
     * @param ttl  - время жизни записи
     * @return false если созданы новые данные, true если старые данные обновлены
     */
    public boolean set(String key, String data, String ttl) throws NumberFormatException {
        return setElement(key, data, Double.parseDouble(ttl));
    }

    /**
     * Вспомогательный метод для добавления
     * новых данных в хранилище.
     *
     * @return true - запись удалась, иначе false
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
     * @param key ключ клиента
     * @return объект клиента с заданным ключом или null, если такого объекта нет
     */
    public String get(String key) {
        return DATA_BASE_MAP.get(key);
    }

    /**
     * Удаляет данные с заданным ключом
     *
     * @param key ключ клиента, которого нужно удалить
     * @return бывшее значение этого ключа, или null, если пара ключ-значение не найдена
     */
    public String remove(String key) {
        timeMap.remove(key);
        return DATA_BASE_MAP.remove(key);
    }

    /**
     * Сохраняет текущее состояние хранилища
     * и записывает его в файл
     *
     * @param fileName - имя файла
     * @return true если запись удалась, иначе false
     */
    public boolean dump(String fileName) {
        return dumpToFile(fileName);

    }

    /**
     * Сохраняет текущее состояние хранилища
     * и записывает его в файл по умолчанию
     *
     * @return true если запись удалась, иначе false
     */
    public boolean dump() {
        return dumpToFile(fileName);
    }

    /**
     * Вспомогательный метод для сохранения
     * текущего состояния хранилища
     * и записи его в файл
     *
     * @return true если запись удалась, иначе false
     */
    private boolean dumpToFile(String fileName) {
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            byte[] dataToBytes = dumpDBToCSV().getBytes();
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
     * @param fileName имя файла
     * @return true если загрузка удалась, иначе false
     */
    public boolean load(String fileName) {
        return loadFromFile(fileName);
    }

    /**
     * Загружает текущее состояние хранилища
     * из файла по умолчанию
     *
     * @return true если загрузка удалась, иначе false
     */
    public boolean load() {
        return loadFromFile(fileName);
    }

    /**
     * Вспомогательный метод для преобразования для
     * загрузки текущее состояние хранилища из файла.
     *
     * @return true если загрузка удалась, иначе false
     */
    private boolean loadFromFile(String fileName) {
        try {
            loadDBFromCSV(CSVReader.fileToCSV(fileName));
            return true;
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке хранилища из файла.");
            return false;
        }
    }

    /**
     * Вспомогательный метод для преобразования
     * хранилища в строку в формате CSV
     *
     * @return получаемая строка с парами ключ-значение в формате CSV
     */
    private String dumpDBToCSV() {
        StringBuilder result = new StringBuilder();
        for (String key : DATA_BASE_MAP.keySet()) {
            result.append(key).append(",").append(DATA_BASE_MAP.get(key)).append(",").append(timeMap.get(key)).append("\n");
        }
        return new String(result);
    }

    /**
     * Вспомогательный метод для преобразования
     * строки в формате CSV в хранилище
     *
     * @param str строка с парами ключ-значение в формате CSV
     */
    private void loadDBFromCSV(String str) {
        Map<String, String> db = new HashMap<>();
        Map<String, Double> timMap = new HashMap<>();
        String[] lines = str.split("\r\n|\n|\r");
        for (String line :
                lines) {
            String[] elements = line.split(",");
            db.put(elements[0], elements[1]);
            timMap.put(elements[0], Double.parseDouble(elements[2]));
        }
        DATA_BASE_MAP = db;
        timeMap = timMap;
    }

}