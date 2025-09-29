package com.smartbookkeeping.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息VO
 */
@Data
@Accessors(chain = true)
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    private String mobile;
    private String email;
    private Integer gender;
    private List<String> roles;
}