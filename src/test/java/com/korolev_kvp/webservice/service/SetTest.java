package com.korolev_kvp.webservice.service;

import org.junit.jupiter.api.*;

class SetTest {

    private DataBaseService dataBaseService;

    @BeforeAll
    public static void beforeClass() {
        System.out.println("Before SetTest class");
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("After SetTest class");
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
    void testValue() {
        String key = "key";
        String value = "value";
        dataBaseService.set(key, value);
        Assertions.assertEquals(value, dataBaseService.get(key));
    }

    @Test
    void testKey() {
        String key = "key";
        String value = "value";
        dataBaseService.set(key, value);
        Assertions.assertTrue(DataBaseService.getDataBaseMap().containsKey(key));
    }

    @Test
    void testTtlAfter() throws InterruptedException {
        String key = "key";
        String value = "value";
        dataBaseService.set(key, value, 1);
        Thread.sleep(1000);
        Assertions.assertFalse(DataBaseService.getDataBaseMap().containsKey(key));
    }

    @Test
    void testTtlBefore() throws InterruptedException {
        String key = "key";
        String value = "value";
        dataBaseService.set(key, value, 1);
        Thread.sleep(500);
        Assertions.assertTrue(DataBaseService.getDataBaseMap().containsKey(key));
    }
}