package com.korolev_kvp.webservice.service;

import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Map;

public class DumpTest {

    private static DataBaseService dataBaseService;

    private final String key = "key";
    private final String value = "value";

    // для того чтобы не стереть сохранённое хранилище при тестах
    private static Map<String, String> map;
    private static Map<String, String> defaultMap;

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До DumpTest класса");
        dataBaseService = new DataBaseService();
        if (new File("file").isFile())
            if (dataBaseService.load("file")) map = DataBaseService.getDataBaseMap();

        if (new File(dataBaseService.getFileName()).isFile())
            if (dataBaseService.load()) defaultMap = DataBaseService.getDataBaseMap();
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После DumpTest класса");
        if (map != null) dataBaseService.load("file");
        if (defaultMap != null) dataBaseService.load();
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
        File file = new File(dataBaseService.getFileName());
        Assertions.assertTrue(file.isFile());
        Assertions.assertTrue(file.delete());
    }

    @Test
    void testDump() {
        String filename = "file";
        dataBaseService.dump(filename);
        File file = new File(filename);
        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.isFile());
        Assertions.assertTrue(file.delete());
    }
}
