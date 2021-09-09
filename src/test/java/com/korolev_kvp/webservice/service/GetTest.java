package com.korolev_kvp.webservice.service;

import org.junit.jupiter.api.*;

import java.util.HashMap;

class GetTest {

    private DataBaseService dataBaseService;

    private final String key = "key";
    private final String value = "value";

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До GetTest service класса");
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После GetTest service класса");
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
    void testDefaultGet() {
        Assertions.assertEquals(value, dataBaseService.get(key));
    }

    @Test
    void testGetEmpty() {
        Assertions.assertNull(dataBaseService.get(""));
    }

    @Test
    void testGetAll() {
        Assertions.assertEquals(new HashMap<>() {{put(key, value);}}, dataBaseService.getAll());
    }

    @Test
    void testEmptyGetAll() {
        dataBaseService.remove(key);
        Assertions.assertEquals(new HashMap<>(), dataBaseService.getAll());
    }

}