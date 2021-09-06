package com.korolev_kvp.webservice.controller;

import com.korolev_kvp.webservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @PostMapping("{key}")
    public ResponseEntity<?> set(@PathVariable(name = "key") String key, @RequestBody String data) {
        return messageService.set(key, data)
                ? new ResponseEntity<>("Старое значение заданного ключа изменено на новое.", HttpStatus.OK)
                : new ResponseEntity<>("Добавлена новая пара ключ-значение.", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<String> getAll() {
        final Map<String, String> dataList = messageService.getAll();
        return dataList != null && !dataList.isEmpty()
                ? new ResponseEntity<>(messageService.mapToString(dataList), HttpStatus.OK)
                : new ResponseEntity<>("Хранилище пустое.", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "{key}")
    public ResponseEntity<String> get(@PathVariable(name = "key") String key) {
        final String result = messageService.get(key);
        return result != null
                ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>("Ключа с заданным значением нет в хранилище.", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "{key}")
    public ResponseEntity<String> remove(@PathVariable(name = "key") String key) {
        String result = messageService.remove(key);
        return result != null
                ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>("Пара ключ-значение с заданным ключом не найдена.", HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/dump/{fileName}")
    public ResponseEntity<?> dump(@PathVariable String fileName) {
        return messageService.dump(fileName)
                ? new ResponseEntity<>(new File(fileName), HttpStatus.CREATED)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не записано в файл.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/dump")
    public ResponseEntity<?> dump() {
        return messageService.dump()
                ? new ResponseEntity<>(new File(messageService.getFileName()), HttpStatus.CREATED)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не записано в файл.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/load/{fileName}")
    public ResponseEntity<?> load(@PathVariable String fileName) {
        return messageService.load(fileName)
                ? new ResponseEntity<>("Хранилище загружено из файла: " + new File(fileName), HttpStatus.OK)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не загружено из файла.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/load")
    public ResponseEntity<?> load() {
        return messageService.load()
                ? new ResponseEntity<>("Хранилище загружено из файла по умолчанию.", HttpStatus.OK)
                : new ResponseEntity<>("Произошла ошибка. Хранилище не загружено из файла.", HttpStatus.NOT_FOUND);
    }

}
