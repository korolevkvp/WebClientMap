package com.korolev_kvp.webservice.controller;

import com.korolev_kvp.webservice.service.DataBaseService;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class SetTest {

    private static DataBaseService dataBaseService;
    private static DataBaseController dataBaseController;

    private final String key = "key";
    private final String value = "value";

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До SetTest controller класса");
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После SetTest controller класса");
    }

    @BeforeEach
    void initTest() {
        dataBaseService = new DataBaseService();
        dataBaseController = new DataBaseController(dataBaseService);
    }

    @AfterEach
    void afterTest() {
        dataBaseService = null;
        dataBaseController = null;
    }

    @Test
    void testSet() {
        Assertions.assertEquals(new ResponseEntity<>("Добавлена новая пара ключ-значение.", HttpStatus.CREATED), dataBaseController.set(key, value));
    }

    @Test
    void testReSet() {
        dataBaseController.set(key, value);
        Assertions.assertEquals(new ResponseEntity<>("Старое значение заданного ключа изменено на новое.", HttpStatus.OK), dataBaseController.set(key, value));
    }

    @DisplayName("выбрасывает NumberFormatException при вводе не численного значения ttl")
    @Test
    void testWrongArg() {
        Assertions.assertEquals(new ResponseEntity<>("Ошибка. В качестве времени жизни передано не число.", HttpStatus.BAD_REQUEST), dataBaseController.set(key, value, "1,0"));
    }

}