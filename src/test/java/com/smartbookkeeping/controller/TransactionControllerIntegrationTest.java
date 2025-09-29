package com.smartbookkeeping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbookkeeping.BookkeepingServiceApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BookkeepingServiceApplication.class)
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        // 登录获取token
        Map<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("username", "integrationtest");
        loginRequest.put("password", "password123");

        String result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> resultMap = objectMapper.readValue(result, Map.class);
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        token = (String) dataMap.get("token");
    }

    @Test
    public void testCreateAndGetTransaction() throws Exception {
        // 创建交易记录
        Map<String, Object> transactionRequest = new HashMap<>();
        transactionRequest.put("bookId", 1);
        transactionRequest.put("categoryId", 1);
        transactionRequest.put("amount", 100.00);
        transactionRequest.put("type", 1); // 支出
        transactionRequest.put("transactionTime", "2023-06-15 12:00:00");
        transactionRequest.put("description", "集成测试交易");

        String createResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> createResultMap = objectMapper.readValue(createResult, Map.class);
        Map<String, Object> dataMap = (Map<String, Object>) createResultMap.get("data");
        Integer transactionId = (Integer) dataMap.get("id");

        // 获取交易记录
        mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/" + transactionId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.description").value("集成测试交易"));
    }

    @Test
    public void testGetTransactionList() throws Exception {
        // 获取交易记录列表
        mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions")
                .header("Authorization", "Bearer " + token)
                .param("bookId", "1")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }
}