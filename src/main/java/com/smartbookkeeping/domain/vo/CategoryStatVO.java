package com.smartbookkeeping.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 分类统计VO
 */
@Data
public class CategoryStatVO {
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 百分比
     */
    private BigDecimal percentage;
    
    /**
     * 交易次数
     */
    private Integer count;
}