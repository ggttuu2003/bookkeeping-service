package com.smartbookkeeping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbookkeeping.BookkeepingServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BookkeepingServiceApplication.class)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterAndLogin() throws Exception {
        // 测试注册
        Map<String, Object> registerRequest = new HashMap<>();
        registerRequest.put("username", "integrationtest");
        registerRequest.put("password", "password123");
        registerRequest.put("nickname", "集成测试用户");
        registerRequest.put("email", "integration@test.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("integrationtest"));

        // 测试登录
        Map<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("username", "integrationtest");
        loginRequest.put("password", "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    public void testGetUserInfo() throws Exception {
        // 先登录获取token
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
        String token = (String) dataMap.get("token");

        // 使用token获取用户信息
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/info")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("integrationtest"));
    }
}