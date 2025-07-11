package com.test.testproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.testproject.controller.TestController;
import com.test.testproject.model.TestTable;
import com.test.testproject.repository.TestTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TestControllerTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public TestControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Mock
    private TestTableRepository repository;

    @InjectMocks
    private TestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, world!"));
    }

    @Test
    void testAddAndGetAllEndpoints() throws Exception {
        TestTable testTable = new TestTable();
        testTable.setName("Bob");
        testTable.setAge(25);
        // Add entity
        mockMvc.perform(post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTable)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.age").value(25));
        // Get all entities
        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bob"))
                .andExpect(jsonPath("$[0].age").value(25));
    }

    @Test
    void testAddWithMissingFields() throws Exception {
        // Missing age
        String json = "{" +
                "\"name\": \"NoAge\"" +
                "}";
        mockMvc.perform(post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("NoAge"));
    }

    @Test
    void testAddWithEmptyPayload() throws Exception {
        mockMvc.perform(post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testAddWithInvalidType() throws Exception {
        String json = "{" +
                "\"name\": \"InvalidAge\"," +
                "\"age\": \"notANumber\"}";
        mockMvc.perform(post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllWhenEmpty() throws Exception {
        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testHello() {
        String result = controller.hello();
        assertThat(result).isEqualTo("Hello, world!");
    }

    @Test
    void testAddTestTable() {
        TestTable testTable = new TestTable();
        when(repository.save(testTable)).thenReturn(testTable);
        TestTable result = controller.addTestTable(testTable);
        assertThat(result).isSameAs(testTable);
        verify(repository, times(1)).save(testTable);
    }

    @Test
    void testGetAllTestTables() {
        TestTable t1 = new TestTable();
        TestTable t2 = new TestTable();
        List<TestTable> list = Arrays.asList(t1, t2);
        when(repository.findAll()).thenReturn(list);
        List<TestTable> result = controller.getAllTestTables();
        assertThat(result).containsExactly(t1, t2);
        verify(repository, times(1)).findAll();
    }
}
