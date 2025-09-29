package com.smartbookkeeping.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分类统计VO
 */
@Data
@Accessors(chain = true)
public class CategoryStatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long categoryId;
    private String categoryName;
    private BigDecimal amount;
    private BigDecimal percentage;
    private Integer count;
}