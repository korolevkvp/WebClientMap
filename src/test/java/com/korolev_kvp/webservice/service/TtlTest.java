package com.korolev_kvp.webservice.service;

import org.junit.jupiter.api.*;

public class TtlTest {

    private DataBaseService dataBaseService;

    private final String key = "key";
    private final String value = "value";

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До TtlTest service класса");
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После TtlTest service класса");
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
    void testTtlAfter() throws InterruptedException {
        dataBaseService.set(key, value, "1");
        Thread.sleep(2000);
        Assertions.assertFalse(DataBaseService.getDataBaseMap().containsKey(key));
    }

    @Test
    void testTtlBefore() throws InterruptedException {
        dataBaseService.set(key, value, "1");
        Thread.sleep(100);
        Assertions.assertTrue(DataBaseService.getDataBaseMap().containsKey(key));
    }

}
