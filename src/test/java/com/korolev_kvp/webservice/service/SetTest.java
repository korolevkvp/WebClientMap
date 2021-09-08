package com.korolev_kvp.webservice.service;

import org.junit.jupiter.api.*;

class SetTest {

    private DataBaseService dataBaseService;

    private final String key = "key";
    private final String value = "value";

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До SetTest класса");
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После SetTest класса");
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
        dataBaseService.set(key, value);
        Assertions.assertEquals(value, dataBaseService.get(key));
    }

    @Test
    void testKey() {
        dataBaseService.set(key, value);
        Assertions.assertTrue(DataBaseService.getDataBaseMap().containsKey(key));
    }



}