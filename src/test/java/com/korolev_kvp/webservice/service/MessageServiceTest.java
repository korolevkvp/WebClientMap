package com.korolev_kvp.webservice.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

class MessageServiceTest {

    private MessageService messageService;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Before CalculatorTest.class");
    }

    @AfterClass
    public  static void afterClass() {
        System.out.println("After CalculatorTest.class");
    }

    @Before
    public void initTest() {
        messageService = new MessageService();
    }

    @After
    public void afterTest() {
        messageService = null;
    }

    @Test
    void set() {
    }

    @Test
    void testSet() {
    }

    @Test
    void get() {
    }

    @Test
    void remove() {
    }

    @Test
    void dump() {
    }

    @Test
    void load() {
    }
}