package com.smartbookkeeping.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账本VO
 */
@Data
@Accessors(chain = true)
public class BookVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bookId;
    private String name;
    private String description;
    private String icon;
    private String color;
    private LocalDateTime createTime;
    private Integer memberCount;
    private Integer transactionCount;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
}