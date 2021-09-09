package com.korolev_kvp.webservice.service;

import org.junit.jupiter.api.*;

public class LoadTest {

    private static DataBaseService dataBaseService;

    private static final String key = "key";
    private static final String value = "value";

    @BeforeAll
    static void beforeClass() {
        System.out.println("До LoadTest класса");
        dataBaseService = new DataBaseService();
        dataBaseService.set(key, value);
        dataBaseService.dump();
        dataBaseService.dump("file");
        dataBaseService.remove(key);
    }

    @AfterAll
    static void afterClass() {
        System.out.println("После LoadTest класса");
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
        Assertions.assertTrue(dataBaseService.load("file"));
        Assertions.assertEquals(value, dataBaseService.get(key));
    }

    @Test
    void testDefaultLoad() {
        Assertions.assertTrue(dataBaseService.load());
        Assertions.assertEquals(value, dataBaseService.get(key));
    }

    @Test
    void testWrongLoad() {
        Assertions.assertFalse(dataBaseService.load(""));
        Assertions.assertNotEquals(value, dataBaseService.get(key));
    }
}
