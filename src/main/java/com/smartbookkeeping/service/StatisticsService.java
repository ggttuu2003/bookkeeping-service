package com.smartbookkeeping.service;

import com.smartbookkeeping.domain.vo.CategoryStatVO;
import com.smartbookkeeping.domain.vo.DailyStatVO;
import com.smartbookkeeping.domain.vo.MonthlyStatVO;
import com.smartbookkeeping.domain.vo.TrendAnalysisVO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计分析服务接口
 */
public interface StatisticsService {

    /**
     * 获取每日收支统计
     * @param userId 用户ID
     * @param bookId 账本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每日收支统计列表
     */
    List<DailyStatVO> getDailyStats(Long userId, Long bookId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取月度收支统计
     * @param userId 用户ID
     * @param bookId 账本ID
     * @param year 年份
     * @return 月度收支统计列表
     */
    List<MonthlyStatVO> getMonthlyStats(Long userId, Long bookId, Integer year);

    /**
     * 获取分类收支统计
     * @param userId 用户ID
     * @param bookId 账本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param type 交易类型：1-收入，2-支出
     * @return 分类收支统计列表
     */
    List<CategoryStatVO> getCategoryStats(Long userId, Long bookId, LocalDate startDate, LocalDate endDate, Integer type);

    /**
     * 获取收支趋势分析
     * @param userId 用户ID
     * @param bookId 账本ID
     * @param months 分析月数
     * @return 收支趋势分析结果
     */
    TrendAnalysisVO getTrendAnalysis(Long userId, Long bookId, Integer months);

    /**
     * 获取用户消费习惯分析
     * @param userId 用户ID
     * @param bookId 账本ID
     * @return 消费习惯分析结果
     */
    Map<String, Object> getSpendingHabits(Long userId, Long bookId);

    /**
     * 获取预算执行情况
     * @param userId 用户ID
     * @param bookId 账本ID
     * @param year 年份
     * @param month 月份
     * @return 预算执行情况
     */
    Map<String, Object> getBudgetExecution(Long userId, Long bookId, Integer year, Integer month);

    /**
     * 获取指定时间段内的收入或支出总额
     * @param userId 用户ID
     * @param bookId 账本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param type 交易类型：1-收入，2-支出
     * @return 金额总计
     */
    BigDecimal getMonthlyAmount(Long userId, Long bookId, LocalDate startDate, LocalDate endDate, Integer type);
}