package com.smartbookkeeping.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class TransactionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long bookId;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private String categoryColor;
    private Long paymentMethodId;
    private BigDecimal amount;
    private Integer type;
    private LocalDateTime transactionTime;
    private String description;
    private String location;
    private String imageUrls;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private List<Map<String, Object>> tags;
}