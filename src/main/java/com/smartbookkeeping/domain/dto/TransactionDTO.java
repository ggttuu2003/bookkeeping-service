package com.smartbookkeeping.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TransactionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal amount;

    @NotNull(message = "交易类型不能为空")
    private Integer type;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @NotNull(message = "支付方式ID不能为空")
    private Long paymentMethodId;

    @NotNull(message = "交易时间不能为空")
    private String transactionTime;

    private String description;

    private Long bookId;

    private Long userId;

    private String location;

    private String imageUrls;
}