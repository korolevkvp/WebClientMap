package com.korolev_kvp.webservice.service;

import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LoadTest {

    private static DataBaseService dataBaseService;

    private static final String key = "key";
    private static final String value = "value";

    // для того чтобы не стереть сохранённое хранилище при тестах
    private static Map<String, String> oldMap;

    private static String fileName;

    @BeforeAll
    static void beforeClass() {
        System.out.println("До LoadTest service класса");
        dataBaseService = new DataBaseService();
        fileName = dataBaseService.getFileName();
        if (new File(fileName).isFile())
            if (dataBaseService.load()) oldMap = DataBaseService.getDataBaseMap();
        DataBaseService.setDataBaseMap(new HashMap<>() {{put(key, value);}});
    }

    @AfterAll
    static void afterClass() {
        System.out.println("После LoadTest service класса");
        dataBaseService = new DataBaseService();
        if (oldMap != null) {
            DataBaseService.setDataBaseMap(oldMap);
            dataBaseService.dump();
        }
        else new File(fileName).delete();
    }

    @BeforeEach
    public void initTest() {
        dataBaseService = new DataBaseService();
    }

    @AfterEach
    public void afterTest() {
        dataBaseService = null;
    }

    @Test
    void testLoad() {
        Assertions.assertTrue(dataBaseService.load(fileName));
        Assertions.assertEquals(value, dataBaseService.get(key));
    }

    @Test
    void testDefaultLoad() {
        Assertions.assertTrue(dataBaseService.load());
        Assertions.assertEquals(value, dataBaseService.get(key));
    }

    @Test
    void testWrongLoad() {
        if (dataBaseService.get(key) != null) dataBaseService.remove(key);
        Assertions.assertFalse(dataBaseService.load(""));
        Assertions.assertNull(dataBaseService.get(key));
    }
}
