package com.test.testproject;

import com.test.testproject.model.TestTable;
import com.test.testproject.repository.TestTableRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TestTableRepositoryTests {
    @Autowired
    private TestTableRepository repository;

    @Test
    void testSaveAndFind() {
        TestTable testTable = new TestTable();
        testTable.setName("Alice");
        testTable.setAge(30);
        TestTable saved = repository.save(testTable);
        Assertions.assertNotNull(saved.getId());
        TestTable found = repository.findById(saved.getId()).orElse(null);
        Assertions.assertNotNull(found);
        Assertions.assertEquals("Alice", found.getName());
        Assertions.assertEquals(30, found.getAge());
    }
}

