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
    private static Map<String, String> map;
    private static Map<String, String> defaultMap;

    private static final String fileName = "Test";

    @BeforeAll
    static void beforeClass() {
        System.out.println("До LoadTest service класса");
        dataBaseService = new DataBaseService();
        if (new File(fileName).isFile())
            if (dataBaseService.load(fileName)) map = DataBaseService.getDataBaseMap();
        if (new File(dataBaseService.getFileName()).isFile())
            if (dataBaseService.load()) defaultMap = DataBaseService.getDataBaseMap();
        DataBaseService.setDataBaseMap(new HashMap<>() {{
            put(key, value);
        }});
        dataBaseService.dump(fileName);
        dataBaseService.dump();
    }

    @AfterAll
    static void afterClass() {
        System.out.println("После LoadTest service класса");
        dataBaseService = new DataBaseService();
        if (map != null) {
            DataBaseService.setDataBaseMap(map);
            dataBaseService.dump(fileName);
        } else {
            new File(fileName).delete();
        }
        if (defaultMap != null) {
            DataBaseService.setDataBaseMap(defaultMap);
            dataBaseService.dump(dataBaseService.getFileName());
        } else {
            new File(dataBaseService.getFileName()).delete();
        }
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
