package com.korolev_kvp.webservice.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CSVReader {

    /**
     * Преобразует CSV файл в строку.
     *
     * @param fileName имя файла, из которого происходит считывание
     * @return содержимое файла в строковом виде
     */
    public static String fileToCSV(String fileName) {
        StringBuilder data = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                data.append(scanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден.");
        } catch (NullPointerException e) {
            data = new StringBuilder();
        }
        return data.toString();
    }

}
