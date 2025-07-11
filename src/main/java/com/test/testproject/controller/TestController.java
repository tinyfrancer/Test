package com.test.testproject.controller;


import com.test.testproject.model.TestTable;
import com.test.testproject.repository.TestTableRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private final TestTableRepository repository;

    public TestController(TestTableRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, world!";
    }

    @PostMapping("/add")
    public TestTable addTestTable(@RequestBody TestTable testTable) {
        return repository.save(testTable);
    }

    @GetMapping("/all")
    public List<TestTable> getAllTestTables() {
        return repository.findAll();
    }
}
