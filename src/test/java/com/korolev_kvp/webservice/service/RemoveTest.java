package com.korolev_kvp.webservice.service;

import org.junit.jupiter.api.*;

public class RemoveTest {

    private DataBaseService dataBaseService;

    private final String key = "key";
    private final String value = "value";

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До RemoveTest класса");
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После RemoveTest класса");
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
    void testDefaultRemove() {
        Assertions.assertEquals(value, dataBaseService.remove(key));
    }

    @Test
    void testWrongRemove() {
        Assertions.assertNull(dataBaseService.remove(""));
    }
}
