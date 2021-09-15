package com.korolev_kvp.webservice.controller;

import com.korolev_kvp.webservice.service.DataBaseService;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LoadTest {

    private static DataBaseService dataBaseService;
    private static DataBaseController dataBaseController;

    private static final String key = "key";
    private static final String value = "value";

    // для того чтобы не стереть сохранённое хранилище при тестах
    private static Map<String, String> map;
    private static Map<String, String> defaultMap;

    private static final String fileName = "Test";

    @BeforeAll
    static void beforeClass() {
        System.out.println("До LoadTest controller класса");
        dataBaseService = new DataBaseService();
        dataBaseController = new DataBaseController(dataBaseService);
        if (new File(fileName).isFile())
            if (dataBaseService.load(fileName)) map = DataBaseService.getDataBaseMap();
        if (new File(dataBaseService.getFileName()).isFile())
            if (dataBaseService.load()) defaultMap = DataBaseService.getDataBaseMap();
        DataBaseService.setDataBaseMap(new HashMap<>() {{
            put(key, value);
        }});
        dataBaseService.dump(fileName);
        dataBaseService.dump();
    }

    @AfterAll
    static void afterClass() {
        System.out.println("После LoadTest controller класса");
        dataBaseService = new DataBaseService();
        if (map != null) {
            DataBaseService.setDataBaseMap(map);
            dataBaseService.dump(fileName);
        } else {
            new File(fileName).delete();
        }
        if (defaultMap != null) {
            DataBaseService.setDataBaseMap(defaultMap);
            dataBaseService.dump(dataBaseService.getFileName());
        } else {
            new File(dataBaseService.getFileName()).delete();
        }

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
    void testLoad() {
        Assertions.assertEquals(new ResponseEntity<>("Хранилище загружено из файла: " + new File(fileName), HttpStatus.OK), dataBaseController.load(fileName));
        Assertions.assertEquals(new ResponseEntity<>(value, HttpStatus.OK), dataBaseController.get(key));
    }

    @Test
    void testDefaultLoad() {
        Assertions.assertEquals(new ResponseEntity<>("Хранилище загружено из файла по умолчанию.", HttpStatus.OK), dataBaseController.load());
        Assertions.assertEquals(new ResponseEntity<>(value, HttpStatus.OK), dataBaseController.get(key));
    }

    @Test
    void testWrongLoad() {
        if (dataBaseService.get(key) != null) dataBaseService.remove(key);
        Assertions.assertEquals(new ResponseEntity<>("Произошла ошибка. Хранилище не загружено из файла.", HttpStatus.NOT_FOUND), dataBaseController.load(""));
        Assertions.assertEquals(new ResponseEntity<>("Ключа с заданным значением нет в хранилище.", HttpStatus.NOT_FOUND), dataBaseController.get(key));
    }
}
