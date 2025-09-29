package com.smartbookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartbookkeeping.domain.entity.Budget;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BudgetMapper extends BaseMapper<Budget> {
}