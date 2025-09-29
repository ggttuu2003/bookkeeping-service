package com.smartbookkeeping.controller;

import com.smartbookkeeping.common.ApiResponse;
import com.smartbookkeeping.domain.vo.CategoryStatVO;
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

        List<Map<String, Object>> monthlyStats = new ArrayList<>();

        // 生成12个月的模拟数据
        for (int month = 1; month <= 12; month++) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", month);
            monthData.put("income", month == 9 ? "15000.00" : "0.00");
            monthData.put("expense", month == 9 ? "2419.50" : "0.00");
            monthlyStats.add(monthData);
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

        List<Map<String, Object>> dailyStats = new ArrayList<>();

        // 生成当月每天的模拟数据
        LocalDate startDate = LocalDate.of(year, month, 1);
        int daysInMonth = startDate.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("day", day);
            dayData.put("income", day == 28 ? "15000.00" : "0.00");
            dayData.put("expense", day == 29 ? "35.00" : (day == 27 ? "299.00" : "0.00"));
            dailyStats.add(dayData);
        }

        return ApiResponse.success(dailyStats);
    }
}