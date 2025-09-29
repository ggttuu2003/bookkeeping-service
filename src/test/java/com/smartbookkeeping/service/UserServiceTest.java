package com.smartbookkeeping.service;

import com.smartbookkeeping.domain.entity.User;
import com.smartbookkeeping.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 用户服务单元测试
 */
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    public void testRegister() {
        // 准备测试数据
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setNickname("Test User");
        user.setPhone("13800138000");

        // 模拟方法调用
        doReturn(null).when(userService).getUserByUsername(anyString());
        doReturn(true).when(userService).saveUser(any());

        // 执行注册
        User result = userService.register(user);

        // 验证结果
        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userService, times(1)).saveUser(any());
    }

    @Test
    public void testLogin() {
        // 准备测试数据
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setStatus(1);

        // 模拟方法调用
        doReturn(user).when(userService).getUserByUsername("testuser");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // 执行登录
        User result = userService.login("testuser", "password123");

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userService, times(1)).getUserByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        // 准备测试数据
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        // 模拟方法调用
        doReturn(user).when(userService).getUserByUsername("testuser");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // 执行登录，预期抛出异常
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.login("testuser", "wrongpassword");
        });

        // 验证异常信息
        assertTrue(exception.getMessage().contains("密码不正确"));
        verify(userService, times(1)).getUserByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    public void testUpdateUserInfo() {
        // 准备测试数据
        User user = new User();
        user.setId(1L);
        user.setNickname("New Nickname");
        user.setAvatar("new_avatar.jpg");

        // 模拟方法调用
        doReturn(true).when(userService).updateUser(any());
        doReturn(user).when(userService).getUserById(1L);

        // 执行更新
        boolean result = userService.updateUserInfo(user);

        // 验证结果
        assertTrue(result);
        verify(userService, times(1)).updateUser(any());
    }
}