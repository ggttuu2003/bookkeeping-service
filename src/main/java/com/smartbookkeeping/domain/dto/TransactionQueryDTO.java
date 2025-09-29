package com.smartbookkeeping.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TransactionQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bookId;
    private Long userId;
    private Long categoryId;
    private Integer type;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String keyword;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}