package com.smartbookkeeping.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 月度统计VO
 */
@Data
public class MonthlyStatVO {
    /**
     * 年份
     */
    private Integer year;
    
    /**
     * 月份
     */
    private Integer month;
    
    /**
     * 收入
     */
    private BigDecimal income;
    
    /**
     * 支出
     */
    private BigDecimal expense;
    
    /**
     * 净额
     */
    private BigDecimal net;
    
    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;
    
    /**
     * 预算执行率
     */
    private BigDecimal budgetExecutionRate;
}