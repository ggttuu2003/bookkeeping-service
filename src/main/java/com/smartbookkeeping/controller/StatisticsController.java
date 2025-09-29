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
import java.util.Arrays;
import java.util.List;

/**
 * 统计分析控制器
 */
@Slf4j
@RestController
@RequestMapping("/statistics")
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
}