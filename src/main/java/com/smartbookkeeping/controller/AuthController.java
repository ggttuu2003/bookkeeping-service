package com.smartbookkeeping.controller;

import com.smartbookkeeping.common.ApiResponse;
import com.smartbookkeeping.domain.dto.UserLoginDTO;
import com.smartbookkeeping.domain.dto.UserRegisterDTO;
import com.smartbookkeeping.domain.entity.User;
import com.smartbookkeeping.domain.vo.UserInfoVO;
import com.smartbookkeeping.domain.vo.UserLoginVO;
import com.smartbookkeeping.exception.BusinessException;
import com.smartbookkeeping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 用户认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<UserLoginVO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existUser = userService.findByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 创建新用户
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

        User savedUser = userService.save(user);

        // 生成token（简化实现）
        String token = "mock_token_" + savedUser.getId();

        UserLoginVO loginVO = new UserLoginVO()
                .setUserId(savedUser.getId())
                .setUsername(savedUser.getUsername())
                .setNickname(savedUser.getNickname())
                .setToken(token)
                .setExpiresIn(86400L);

        return ApiResponse.success("注册成功", loginVO);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<UserLoginVO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        User user = userService.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException(400, "用户名或密码错误");
        }

        // 简化密码验证（实际应用中应该使用加密密码）
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            throw new BusinessException(400, "用户名或密码错误");
        }

        // 生成token（简化实现）
        String token = "mock_token_" + user.getId();

        UserLoginVO loginVO = new UserLoginVO()
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setNickname(user.getNickname())
                .setToken(token)
                .setExpiresIn(86400L);

        return ApiResponse.success("登录成功", loginVO);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public ApiResponse<UserInfoVO> getUserInfo() {
        // 简化实现，实际应该从JWT token中获取用户ID
        Long currentUserId = 1L;

        User user = userService.findById(currentUserId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        UserInfoVO userInfo = new UserInfoVO()
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setNickname(user.getNickname())
                .setAvatar(user.getAvatar())
                .setMobile(user.getPhone())
                .setEmail(user.getEmail())
                .setGender(user.getGender())
                .setRoles(Arrays.asList("ROLE_USER"));

        return ApiResponse.success(userInfo);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public ApiResponse<Void> updateUserInfo(@RequestBody UserInfoVO userInfoVO) {
        // 简化实现，实际应该从JWT token中获取用户ID
        Long currentUserId = 1L;

        User user = userService.findById(currentUserId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 更新用户信息
        if (userInfoVO.getNickname() != null) {
            user.setNickname(userInfoVO.getNickname());
        }
        if (userInfoVO.getAvatar() != null) {
            user.setAvatar(userInfoVO.getAvatar());
        }
        if (userInfoVO.getMobile() != null) {
            user.setPhone(userInfoVO.getMobile());
        }
        if (userInfoVO.getEmail() != null) {
            user.setEmail(userInfoVO.getEmail());
        }
        if (userInfoVO.getGender() != null) {
            user.setGender(userInfoVO.getGender());
        }
        user.setUpdatedTime(LocalDateTime.now());

        userService.update(user);

        return ApiResponse.success("更新成功", null);
    }
}