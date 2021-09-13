package com.korolev_kvp.webservice.controller;

import com.korolev_kvp.webservice.service.DataBaseService;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Map;

public class DumpTest {

    private static DataBaseService dataBaseService;
    private static DataBaseController dataBaseController;

    private final String key = "key";
    private final String value = "value";

    // для того чтобы не стереть сохранённое хранилище при тестах
    private static Map<String, String> oldMap;

    private static String fileName;

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До DumpTest controller класса");
        dataBaseService = new DataBaseService();
        dataBaseController = new DataBaseController(dataBaseService);
        fileName = dataBaseService.getFileName();
        if (new File(fileName).isFile())
            if (dataBaseService.load()) oldMap = DataBaseService.getDataBaseMap();
    }

    @AfterAll
    public  static void afterClass() {
        System.out.println("После DumpTest controller класса");
        dataBaseService = new DataBaseService();
        if (oldMap != null) dataBaseService.load();
        else new File(fileName).delete();
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
    void testDefaultDump() {
        Assertions.assertEquals(new ResponseEntity<>(new File(dataBaseService.getFileName()), HttpStatus.CREATED), dataBaseController.dump());
        File file = new File(dataBaseService.getFileName());
        Assertions.assertTrue(file.isFile());
        Assertions.assertTrue(file.delete());
    }

    @Test
    void testDump() {
        dataBaseController.dump(fileName);
        File file = new File(fileName + ".json");
        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.isFile());
        Assertions.assertTrue(file.delete());
    }
}
