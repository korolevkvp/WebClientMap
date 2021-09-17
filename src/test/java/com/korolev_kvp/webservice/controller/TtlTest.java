package com.korolev_kvp.webservice.controller;

import com.korolev_kvp.webservice.service.DataBaseService;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TtlTest {

    private DataBaseService dataBaseService;
    private static DataBaseController dataBaseController;

    private final String key = "key";
    private final String value = "value";

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До TtlTest controller класса");
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После TtlTest controller класса");
    }

    @BeforeEach
    public void initTest() {
        dataBaseService = new DataBaseService();
        dataBaseController = new DataBaseController(dataBaseService);
    }

    @AfterEach
    public void afterTest() {
        dataBaseService = null;
        dataBaseController = null;
    }

    @Test
    void testTtlAfter() throws InterruptedException {
        dataBaseController.set(key, value, "1");
        Thread.sleep(2000);
        Assertions.assertEquals(new ResponseEntity<>("Ключа с заданным значением нет в хранилище.", HttpStatus.NOT_FOUND), dataBaseController.get(key));
    }

    @Test
    void testTtlBefore() throws InterruptedException {
        dataBaseController.set(key, value, "1");
        Thread.sleep(100);
        Assertions.assertEquals(new ResponseEntity<>(value, HttpStatus.OK), dataBaseController.get(key));
    }

}
