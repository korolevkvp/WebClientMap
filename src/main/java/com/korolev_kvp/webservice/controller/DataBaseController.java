package com.korolev_kvp.webservice.controller;

import com.korolev_kvp.webservice.service.DataBaseService;
import com.korolev_kvp.webservice.util.JSONConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;

/**
 * Класс, описывающий контроллер.
 * Тут описана логика работы с запросами извне и их отправка сервису.
 */
@RestController
@RequestMapping
public class DataBaseController {

    private static DataBaseService dataBaseService;

    /**
     * Конструктор, устанавливающий связь с сервисом.
     * @param dataBaseService связанный с контроллером сервис
     */
    @Autowired
    public DataBaseController(DataBaseService dataBaseService) {
        DataBaseController.dataBaseService = dataBaseService;
    }


    /**
     * Метод для обработки установки значения по ключу и временем жизни по умолчанию
     * @param key ключ
     * @param data значение
     * @return ответ клиенту о создании или замене пары ключ-значение
     */
    @PostMapping("/set/{key}")
    public ResponseEntity<?> set(@PathVariable(name = "key") String key, @RequestBody String data) {
        return dataBaseService.set(key, data)
                ? new ResponseEntity<>("Старое значение заданного ключа изменено на новое.", HttpStatus.OK)
                : new ResponseEntity<>("Добавлена новая пара ключ-значение.", HttpStatus.CREATED);
    }

    /**
     *
     * Тот же set, но с определённым временем жизни
     * @param key ключ
     * @param data значение
     * @param ttl время жизни
     * @return ответ клиенту о создании или замене пары ключ-значение, либо ошибка
     */
    @PostMapping("/set/{key}/{ttl}")
    public ResponseEntity<?> set(@PathVariable(name = "key") String key, @RequestBody String data, @PathVariable String ttl) {
        try {
            return dataBaseService.set(key, data, ttl)
                    ? new ResponseEntity<>("Старое значение заданного ключа изменено на новое.", HttpStatus.OK)
                    : new ResponseEntity<>("Добавлена новая пара ключ-значение.", HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Ошибка. В качестве времени жизни передано не число.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Метод для команды, которая возвращает всё хранилище.
     * @return всё хранилище или сообщение о том, что оно пустое
     */
    @GetMapping("/get")
    public ResponseEntity<String> getAll() {
        final Map<String, String> dataList = dataBaseService.getAll();
        return dataList != null && !dataList.isEmpty()
                ? new ResponseEntity<>(JSONConverter.mapToJSONString(dataList), HttpStatus.OK)
                : new ResponseEntity<>("Хранилище пустое.", HttpStatus.NOT_FOUND);
    }

    /**
     * Метод для получения значения по ключу
     * @param key ключ
     * @return значение или сообщение о том, что заданного ключа не существует
     */
    @GetMapping(value = "/get/{key}")
    public ResponseEntity<String> get(@PathVariable(name = "key") String key) {
        final String value = dataBaseService.get(key);
        return value != null
                ? new ResponseEntity<>(value, HttpStatus.OK)
                : new ResponseEntity<>("Ключа с заданным значением нет в хранилище.", HttpStatus.NOT_FOUND);
    }

    /**
     * Метод для удаления значения по ключу
     * @param key ключ
     * @return удалённое значение или сообщение о том, что заданного ключа не существует
     */
    @DeleteMapping(value = "/remove/{key}")
    public ResponseEntity<String> remove(@PathVariable(name = "key") String key) {
        String value = dataBaseService.remove(key);
        return value != null
                ? new ResponseEntity<>(value, HttpStatus.OK)
                : new ResponseEntity<>("Пара ключ-значение с заданным ключом не найдена.", HttpStatus.NOT_FOUND);
    }

    /**
     * Метод для сохранения хранилища в файл по умолчанию
     * @return записанный файл или сообщение об ошибке
     */
    @GetMapping("/dump")
    public ResponseEntity<?> dump() {
        return dataBaseService.dump()
                ? new ResponseEntity<>(new File(dataBaseService.getFileName()), HttpStatus.CREATED)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не записано в файл.", HttpStatus.NOT_FOUND);
    }

    /**
     * Метод для сохранения хранилища в определённый файл
     * @param fileName имя файла
     * @return записанный файл или сообщение об ошибке
     */
    @GetMapping("/dump/{fileName}")
    public ResponseEntity<?> dump(@PathVariable String fileName) {
        return dataBaseService.dump(fileName)
                ? new ResponseEntity<>(new File(fileName), HttpStatus.CREATED)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не записано в файл.", HttpStatus.NOT_FOUND);
    }

    /**
     * Метод для загрузки хранилища из файла по умолчанию
     * @return сообщение об успешной или неуспешной загрузке
     */
    @PostMapping("/load")
    public ResponseEntity<?> load() {
        return dataBaseService.load()
                ? new ResponseEntity<>("Хранилище загружено из файла по умолчанию.", HttpStatus.OK)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не загружено из файла.", HttpStatus.NOT_FOUND);
    }

    /**
     * Метод для загрузки хранилища из определённого файла
     * @param fileName имя файла
     * @return сообщение об успешной или неуспешной загрузке
     */
    @PostMapping("/load/{fileName}")
    public ResponseEntity<?> load(@PathVariable String fileName) {
        return dataBaseService.load(fileName)
                ? new ResponseEntity<>("Хранилище загружено из файла: " + new File(fileName), HttpStatus.OK)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не загружено из файла.", HttpStatus.NOT_FOUND);
    }

    /**
     * Метод для завершения работы
     */
    @DeleteMapping("/exit")
    public void exit() {
        System.out.println("Завершение работы...");
        System.exit(0);
    }
}
