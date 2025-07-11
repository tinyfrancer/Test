package com.test.testproject;

import com.test.testproject.model.TestTable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestTableModelTests {
    @Test
    void testGettersAndSetters() {
        TestTable table = new TestTable();
        table.setId(123L);
        table.setName("TestName");
        table.setAge(42);
        assertEquals(123L, table.getId());
        assertEquals("TestName", table.getName());
        assertEquals(42, table.getAge());
    }
}

