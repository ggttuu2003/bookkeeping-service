package com.smartbookkeeping.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class TransactionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long bookId;
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private Integer type;
    private LocalDateTime transactionTime;
    private String description;
    private String location;
    private String imageUrls;
    private List<Long> tagIds;
}