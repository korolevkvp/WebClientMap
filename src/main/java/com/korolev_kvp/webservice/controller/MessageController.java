package com.korolev_kvp.webservice.controller;

import com.korolev_kvp.webservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
            ? new ResponseEntity<>(HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getAll() {
        final Map<String, String> dataList = messageService.getAll();
        return dataList != null && !dataList.isEmpty()
                ? new ResponseEntity<>(dataList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "{key}")
    public ResponseEntity<String> get(@PathVariable(name = "key") String key) {
        final String result = messageService.get(key);
        return result != null
                ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "{key}")
    public ResponseEntity<?> remove(@PathVariable(name = "key") String key) {
        return messageService.remove(key)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/dump/{fileName}")
    public ResponseEntity<?> dump(@PathVariable String fileName) {
        return messageService.dump(fileName)
                ? new ResponseEntity<>(new File(fileName), HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



}
