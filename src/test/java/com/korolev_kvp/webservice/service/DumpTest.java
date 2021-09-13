package com.korolev_kvp.webservice.service;

import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DumpTest {

    private static DataBaseService dataBaseService;

    private final String key = "key";
    private final String value = "value";

    // для того чтобы не стереть сохранённое хранилище при тестах
    private static Map<String, String> oldMap;

    private static String fileName;

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До DumpTest service класса");
        dataBaseService = new DataBaseService();
        fileName = dataBaseService.getFileName();
        if (new File(fileName).isFile())
            if (dataBaseService.load()) oldMap = DataBaseService.getDataBaseMap();
        DataBaseService.setDataBaseMap(new HashMap<>());
    }

    @AfterAll
    public  static void afterClass() {
        dataBaseService = new DataBaseService();
        System.out.println("После DumpTest service класса");
        if (oldMap != null) {
            DataBaseService.setDataBaseMap(oldMap);
            dataBaseService.dump();
        }
        else new File(fileName).delete();
    }

    @BeforeEach
    public void initTest() {
        dataBaseService = new DataBaseService();
        dataBaseService.set(key, value);
    }

    @AfterEach
    public void afterTest() {
        dataBaseService = null;
    }

    @Test
    void testDefaultDump() {
        Assertions.assertTrue(dataBaseService.dump());
        File file = new File(fileName);
        Assertions.assertTrue(file.isFile());
        Assertions.assertTrue(file.delete());
    }

    @Test
    void testDump() {
        dataBaseService.dump(fileName);
        File file = new File(fileName + ".json");
        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.isFile());
        Assertions.assertTrue(file.delete());
    }
}
