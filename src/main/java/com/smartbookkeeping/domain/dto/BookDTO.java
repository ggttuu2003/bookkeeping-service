package com.smartbookkeeping.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * 账本DTO
 */
@Data
public class BookDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "账本名称不能为空")
    @Size(max = 50, message = "账本名称长度不能超过50")
    private String name;

    @Size(max = 200, message = "账本描述长度不能超过200")
    private String description;

    private String icon;

    private String color;
}