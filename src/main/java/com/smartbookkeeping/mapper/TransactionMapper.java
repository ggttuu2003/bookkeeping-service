package com.smartbookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartbookkeeping.domain.entity.Transaction;
import com.smartbookkeeping.domain.vo.TransactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {

    /**
     * 根据类型和日期范围统计金额
     */
    BigDecimal sumAmountByTypeAndDateRange(@Param("userId") Long userId,
                                          @Param("bookId") Long bookId,
                                          @Param("type") Integer type,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    /**
     * 获取最近的交易记录
     */
    List<TransactionVO> selectRecentTransactions(@Param("userId") Long userId,
                                               @Param("bookId") Long bookId,
                                               @Param("limit") Integer limit);

    /**
     * 获取月度统计数据
     */
    List<Map<String, Object>> getMonthlyStats(@Param("userId") Long userId,
                                             @Param("bookId") Long bookId,
                                             @Param("year") Integer year);

    /**
     * 获取每日统计数据
     */
    List<Map<String, Object>> getDailyStats(@Param("userId") Long userId,
                                           @Param("bookId") Long bookId,
                                           @Param("year") Integer year,
                                           @Param("month") Integer month);
}