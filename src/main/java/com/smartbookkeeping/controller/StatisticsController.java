package com.smartbookkeeping.controller;

import com.smartbookkeeping.common.ApiResponse;
import com.smartbookkeeping.domain.vo.CategoryStatVO;
import com.smartbookkeeping.domain.vo.MonthlyStatVO;
import com.smartbookkeeping.domain.vo.DailyStatVO;
import com.smartbookkeeping.domain.vo.TrendAnalysisVO;
import com.smartbookkeeping.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * 统计分析控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取分类支出占比
     */
    @GetMapping("/category")
    public ApiResponse<List<CategoryStatVO>> getCategoryStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Integer type) {

        // 简化实现，实际应该从JWT token中获取用户ID和当前账本ID
        Long currentUserId = 1L;
        Long currentBookId = 1L;

        List<CategoryStatVO> stats = statisticsService.getCategoryStats(currentUserId, currentBookId, startDate, endDate, type);

        return ApiResponse.success(stats);
    }

    /**
     * 获取月度收支趋势
     */
    @GetMapping("/trend")
    public ApiResponse<TrendAnalysisVO> getTrendAnalysis(@RequestParam(defaultValue = "6") Integer months) {
        // 简化实现，实际应该从JWT token中获取用户ID和当前账本ID
        Long currentUserId = 1L;
        Long currentBookId = 1L;

        TrendAnalysisVO trendAnalysis = statisticsService.getTrendAnalysis(currentUserId, currentBookId, months);

        return ApiResponse.success(trendAnalysis);
    }

    /**
     * 获取月度统计数据 - 小程序专用
     */
    @GetMapping("/monthly")
    public ApiResponse<List<Map<String, Object>>> getMonthlyStatistics(
            @RequestParam Integer year,
            @RequestParam Long bookId) {

        // 简化实现，实际应该从JWT token中获取用户ID
        Long currentUserId = 1L;

        List<Map<String, Object>> monthlyStats = new ArrayList<>();

        try {
            // 使用真实的统计服务获取数据
            List<MonthlyStatVO> stats = statisticsService.getMonthlyStats(currentUserId, bookId, year);

            // 转换为前端需要的格式
            for (MonthlyStatVO stat : stats) {
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", stat.getMonth());
                monthData.put("income", stat.getIncome().toString());
                monthData.put("expense", stat.getExpense().toString());
                monthlyStats.add(monthData);
            }
        } catch (Exception e) {
            log.error("获取月度统计数据失败", e);
            // 如果获取真实数据失败，返回空数据
            for (int month = 1; month <= 12; month++) {
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", month);
                monthData.put("income", "0.00");
                monthData.put("expense", "0.00");
                monthlyStats.add(monthData);
            }
        }

        return ApiResponse.success(monthlyStats);
    }

    /**
     * 获取每日统计数据 - 小程序专用
     */
    @GetMapping("/daily")
    public ApiResponse<List<Map<String, Object>>> getDailyStatistics(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Long bookId) {

        // 简化实现，实际应该从JWT token中获取用户ID
        Long currentUserId = 1L;

        List<Map<String, Object>> dailyStats = new ArrayList<>();

        try {
            // 计算当月的开始日期和结束日期
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

            // 使用真实的统计服务获取数据
            List<DailyStatVO> stats = statisticsService.getDailyStats(currentUserId, bookId, startDate, endDate);

            // 转换为前端需要的格式
            for (DailyStatVO stat : stats) {
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("day", stat.getDate().getDayOfMonth());
                dayData.put("income", stat.getIncome().toString());
                dayData.put("expense", stat.getExpense().toString());
                dailyStats.add(dayData);
            }
        } catch (Exception e) {
            log.error("获取每日统计数据失败", e);
            // 如果获取真实数据失败，返回空数据
            LocalDate startDate = LocalDate.of(year, month, 1);
            int daysInMonth = startDate.lengthOfMonth();

            for (int day = 1; day <= daysInMonth; day++) {
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("day", day);
                dayData.put("income", "0.00");
                dayData.put("expense", "0.00");
                dailyStats.add(dayData);
            }
        }

        return ApiResponse.success(dailyStats);
    }
}