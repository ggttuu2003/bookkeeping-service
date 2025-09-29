package com.smartbookkeeping.service;

import com.smartbookkeeping.domain.vo.CategoryStatVO;
import com.smartbookkeeping.domain.vo.DailyStatVO;
import com.smartbookkeeping.domain.vo.MonthlyStatVO;
import com.smartbookkeeping.domain.vo.TrendAnalysisVO;
import com.smartbookkeeping.service.impl.StatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 统计服务单元测试
 */
public class StatisticsServiceTest {

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDailyStatistics() {
        // 执行查询
        List<DailyStatVO> results = statisticsService.getDailyStats(1L, 1L, 
                LocalDate.of(2023, 6, 1), 
                LocalDate.of(2023, 6, 30));

        // 验证结果
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testGetMonthlyStatistics() {
        // 执行查询
        List<MonthlyStatVO> results = statisticsService.getMonthlyStats(1L, 1L, 2023);

        // 验证结果
        assertNotNull(results);
        assertEquals(12, results.size());
    }

    @Test
    public void testGetCategoryStatistics() {
        // 执行查询
        List<CategoryStatVO> results = statisticsService.getCategoryStats(1L, 1L, 
                LocalDate.of(2023, 6, 1), 
                LocalDate.of(2023, 6, 30), 1);

        // 验证结果
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testGetTrendAnalysis() {
        // 执行查询
        TrendAnalysisVO result = statisticsService.getTrendAnalysis(1L, 1L, 6);

        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getMonths());
        assertEquals(6, result.getMonths().size());
    }

    @Test
    public void testGetUserHabitsAnalysis() {
        // 执行查询
        Map<String, Object> result = statisticsService.getUserHabitsAnalysis(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey("topCategories"));
        assertTrue(result.containsKey("spendingHabits"));
    }

    @Test
    public void testGetBudgetExecutionStatus() {
        // 执行查询
        Map<String, Object> result = statisticsService.getBudgetExecutionStatus(1L, 1L, 2023, 6);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey("budgetAmount"));
        assertTrue(result.containsKey("spentAmount"));
        assertTrue(result.containsKey("remainingAmount"));
        assertTrue(result.containsKey("executionRate"));
    }
}