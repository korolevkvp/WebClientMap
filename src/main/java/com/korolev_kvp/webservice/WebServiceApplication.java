package com.korolev_kvp.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс для запуска сервиса.
 */
@SpringBootApplication
public class WebServiceApplication {

    /**
     * Запуск сервиса
     * @param args -
     */
    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
    }

}
