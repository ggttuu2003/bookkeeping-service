package com.smartbookkeeping.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录实体类
 */
@Data
@Accessors(chain = true)
@TableName("transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账本ID
     */
    private Long bookId;

    /**
     * 记录用户ID
     */
    private Long userId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 类型：1-收入，2-支出，3-转账
     */
    private Integer type;

    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;

    /**
     * 描述
     */
    private String description;

    /**
     * 位置
     */
    private String location;

    /**
     * 图片URL，多个以逗号分隔
     */
    private String imageUrls;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 是否删除：0-否，1-是
     */
    @TableLogic
    private Integer deleted;
}