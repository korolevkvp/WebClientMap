package com.korolev_kvp.webservice.controller;

import com.korolev_kvp.webservice.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping
public class DataBaseController {

    private static DataBaseService dataBaseService;

    @Autowired
    public DataBaseController(DataBaseService dataBaseService) {
        DataBaseController.dataBaseService = dataBaseService;
    }


    @PostMapping("/set/{key}")
    public ResponseEntity<?> set(@PathVariable(name = "key") String key, @RequestBody String data) {
        return dataBaseService.set(key, data)
                ? new ResponseEntity<>("Старое значение заданного ключа изменено на новое.", HttpStatus.OK)
                : new ResponseEntity<>("Добавлена новая пара ключ-значение.", HttpStatus.CREATED);
    }

    @PostMapping("/set/{key}/{ttl}")
    public ResponseEntity<?> set(@PathVariable(name = "key") String key, @RequestBody String data,  @PathVariable String ttl) {
        try {
            return dataBaseService.set(key, data, ttl)
                    ? new ResponseEntity<>("Старое значение заданного ключа изменено на новое.", HttpStatus.OK)
                    : new ResponseEntity<>("Добавлена новая пара ключ-значение.", HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Ошибка. В качестве времени жизни передано не число.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<String> getAll() {
        final Map<String, String> dataList = dataBaseService.getAll();
        return dataList != null && !dataList.isEmpty()
                ? new ResponseEntity<>(dataBaseService.mapToString(dataList), HttpStatus.OK)
                : new ResponseEntity<>("Хранилище пустое.", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/get/{key}")
    public ResponseEntity<String> get(@PathVariable(name = "key") String key) {
        final String value = dataBaseService.get(key);
        return value != null
                ? new ResponseEntity<>(value, HttpStatus.OK)
                : new ResponseEntity<>("Ключа с заданным значением нет в хранилище.", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "{key}")
    public ResponseEntity<String> remove(@PathVariable(name = "key") String key) {
        String value = dataBaseService.remove(key);
        return value != null
                ? new ResponseEntity<>(value, HttpStatus.OK)
                : new ResponseEntity<>("Пара ключ-значение с заданным ключом не найдена.", HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/dump/{fileName}")
    public ResponseEntity<?> dump(@PathVariable String fileName) {
        return dataBaseService.dump(fileName)
                ? new ResponseEntity<>(new File(fileName), HttpStatus.CREATED)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не записано в файл.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/dump")
    public ResponseEntity<?> dump() {
        return dataBaseService.dump()
                ? new ResponseEntity<>(new File(dataBaseService.getFileName()), HttpStatus.CREATED)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не записано в файл.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/load/{fileName}")
    public ResponseEntity<?> load(@PathVariable String fileName) {
        return dataBaseService.load(fileName)
                ? new ResponseEntity<>("Хранилище загружено из файла: " + new File(fileName), HttpStatus.OK)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не загружено из файла.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/load")
    public ResponseEntity<?> load() {
        return dataBaseService.load()
                ? new ResponseEntity<>("Хранилище загружено из файла по умолчанию.", HttpStatus.OK)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не загружено из файла.", HttpStatus.NOT_FOUND);
    }

}
