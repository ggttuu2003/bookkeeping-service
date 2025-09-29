package com.smartbookkeeping.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 趋势分析VO
 */
@Data
public class TrendAnalysisVO {
    /**
     * 月份列表
     */
    private List<String> months;
    
    /**
     * 收入列表
     */
    private List<BigDecimal> incomes;
    
    /**
     * 支出列表
     */
    private List<BigDecimal> expenses;
    
    /**
     * 收入增长率列表
     */
    private List<BigDecimal> incomeGrowthRates;
    
    /**
     * 支出增长率列表
     */
    private List<BigDecimal> expenseGrowthRates;
}