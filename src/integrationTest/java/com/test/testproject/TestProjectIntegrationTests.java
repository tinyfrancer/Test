package com.test.testproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.testproject.model.TestTable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TestProjectIntegrationTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private static final String INTEGRATION_USER = "IntegrationUser";

    public TestProjectIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void testAddAndRetrieveTestTable() throws Exception {
        TestTable testTable = new TestTable();
        testTable.setName(INTEGRATION_USER);
        testTable.setAge(99);

        // Add TestTable
        mockMvc.perform(post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTable)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(INTEGRATION_USER))
                .andExpect(jsonPath("$.age").value(99));

        // Retrieve all TestTables
        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(INTEGRATION_USER))
                .andExpect(jsonPath("$[0].age").value(99));
    }
}
