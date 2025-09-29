package com.smartbookkeeping.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 每日统计VO
 */
@Data
public class DailyStatVO {
    /**
     * 日期
     */
    private LocalDate date;
    
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
}