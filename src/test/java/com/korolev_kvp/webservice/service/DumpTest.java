package com.korolev_kvp.webservice.service;

import com.korolev_kvp.webservice.controller.DataBaseController;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DumpTest {

    private static DataBaseService dataBaseService;

    private static final String key = "key";
    private static final String value = "value";

    // для того чтобы не стереть сохранённое хранилище при тестах
    private static Map<String, String> map;
    private static Map<String, String> defaultMap;

    private static final String fileName = "Test";

    @BeforeAll
    public static void beforeClass() {
        System.out.println("До DumpTest service класса");
        dataBaseService = new DataBaseService();
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
    public  static void afterClass() {
        System.out.println("После DumpTest service класса");
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
    }

    @AfterEach
    public void afterTest() {
        dataBaseService = null;
    }

    @Test
    void testDefaultDump() {
        Assertions.assertTrue(dataBaseService.dump());
        File file = new File(dataBaseService.getFileName());
        Assertions.assertTrue(file.isFile());
        Assertions.assertTrue(file.delete());
    }

    @Test
    void testDump() {
        dataBaseService.dump(fileName);
        File file = new File(fileName);
        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.isFile());
        Assertions.assertTrue(file.delete());
    }
}
