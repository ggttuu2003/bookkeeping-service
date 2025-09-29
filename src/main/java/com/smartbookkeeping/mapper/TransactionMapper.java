package com.smartbookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartbookkeeping.domain.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {
}