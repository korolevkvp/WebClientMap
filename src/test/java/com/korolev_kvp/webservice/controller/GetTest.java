package com.korolev_kvp.webservice.controller;

import com.korolev_kvp.webservice.service.DataBaseService;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

class GetTest {

    private DataBaseService dataBaseService;
    private static DataBaseController dataBaseController;

    private final String key = "key";
    private final String value = "value";

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До GetTest controller класса");
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После GetTest controller класса");
    }

    @BeforeEach
    public void initTest() {
        dataBaseService = new DataBaseService();
        dataBaseController = new DataBaseController(dataBaseService);
        dataBaseController.set(key, value);
    }

    @AfterEach
    public void afterTest() {
        dataBaseService = null;
        dataBaseController = null;
    }

    @Test
    void testDefaultGet() {
        Assertions.assertEquals(new ResponseEntity<>(value, HttpStatus.OK), dataBaseController.get(key));
    }

    @Test
    void testGetEmpty() {
        Assertions.assertEquals(new ResponseEntity<>("Ключа с заданным значением нет в хранилище.", HttpStatus.NOT_FOUND), dataBaseController.get(""));
    }

    @Test
    void testGetAll() {
        Assertions.assertEquals(new ResponseEntity<>(dataBaseService.mapToString(new HashMap<>() {{put(key, value);}}), HttpStatus.OK), dataBaseController.getAll());
    }

    @Test
    void testEmptyGetAll() {
        Assertions.assertEquals(new ResponseEntity<>("Хранилище пустое.", HttpStatus.NOT_FOUND), dataBaseController.getAll());
    }
}