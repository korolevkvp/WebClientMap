package com.korolev_kvp.webservice.service;

import org.junit.jupiter.api.*;

public class TtlTest {

    private DataBaseService dataBaseService;

    private final String key = "key";
    private final String value = "value";

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До TtlTest класса");
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После TtlTest класса");
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
        Thread.sleep(1000);
        Assertions.assertFalse(DataBaseService.getDataBaseMap().containsKey(key));
    }

    @Test
    void testTtlBefore() throws InterruptedException {
        dataBaseService.set(key, value, "1");
        Thread.sleep(500);
        Assertions.assertTrue(DataBaseService.getDataBaseMap().containsKey(key));
    }

    @DisplayName("выбрасывает NumberFormatException при вводе не численного значения ttl")
    @Test
    void testWrongTtl() {
        Exception exception = Assertions.assertThrows(NumberFormatException.class, () ->
                dataBaseService.set(key, value, "1,0"));
        Assertions.assertEquals("For input string: \"1,0\"", exception.getMessage());
    }

}
