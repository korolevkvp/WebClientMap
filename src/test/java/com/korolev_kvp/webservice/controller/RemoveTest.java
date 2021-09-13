package com.korolev_kvp.webservice.controller;

import com.korolev_kvp.webservice.service.DataBaseService;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class RemoveTest {

    private DataBaseService dataBaseService;
    private static DataBaseController dataBaseController;

    private final String key = "key";
    private final String value = "value";

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До RemoveTest controller класса");
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После RemoveTest controller класса");
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
    void testDefaultRemove() {
        Assertions.assertEquals(new ResponseEntity<>(value, HttpStatus.OK), dataBaseController.remove(key));
    }

    @Test
    void testWrongRemove() {
        Assertions.assertEquals(new ResponseEntity<>("Пара ключ-значение с заданным ключом не найдена.", HttpStatus.NOT_FOUND), dataBaseController.remove(""));
    }
}
