package com.smartbookkeeping.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * AI预测VO
 */
@Data
public class AiPredictionVO {
    /**
     * 预测日期
     */
    private LocalDate predictionDate;
    
    /**
     * 预测金额
     */
    private BigDecimal predictedAmount;
    
    /**
     * 置信度
     */
    private BigDecimal confidence;
    
    /**
     * 交易类型：1-收入，2-支出
     */
    private Integer type;
}