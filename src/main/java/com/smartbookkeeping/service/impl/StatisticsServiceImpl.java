package com.smartbookkeeping.service.impl;

import com.smartbookkeeping.domain.entity.Budget;
import com.smartbookkeeping.domain.vo.CategoryStatVO;
import com.smartbookkeeping.domain.vo.DailyStatVO;
import com.smartbookkeeping.domain.vo.MonthlyStatVO;
import com.smartbookkeeping.domain.vo.TrendAnalysisVO;
import com.smartbookkeeping.mapper.BudgetMapper;
import com.smartbookkeeping.mapper.TransactionMapper;
import com.smartbookkeeping.service.StatisticsService;
import com.smartbookkeeping.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private TransactionService transactionService;

    @Override
    public List<DailyStatVO> getDailyStats(Long userId, Long bookId, LocalDate startDate, LocalDate endDate) {
        return new ArrayList<>();
    }

    @Override
    public List<MonthlyStatVO> getMonthlyStats(Long userId, Long bookId, Integer year) {
        return new ArrayList<>();
    }

    @Override
    public List<CategoryStatVO> getCategoryStats(Long userId, Long bookId, LocalDate startDate, LocalDate endDate, Integer type) {
        try {
            // 使用TransactionService获取分类汇总数据
            List<Map<String, Object>> categorySummary = transactionService.getCategorySummary(userId, bookId, type, startDate, endDate);

            if (categorySummary == null || categorySummary.isEmpty()) {
                return new ArrayList<>();
            }

            // 计算总金额
            BigDecimal totalAmount = categorySummary.stream()
                    .map(item -> (BigDecimal) item.get("amount"))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 转换为CategoryStatVO
            List<CategoryStatVO> result = new ArrayList<>();
            for (Map<String, Object> item : categorySummary) {
                CategoryStatVO statVO = new CategoryStatVO();
                statVO.setCategoryId((Long) item.get("categoryId"));
                statVO.setCategoryName((String) item.get("categoryName"));
                statVO.setAmount((BigDecimal) item.get("amount"));

                // 计算百分比
                BigDecimal amount = (BigDecimal) item.get("amount");
                BigDecimal percentage = totalAmount.compareTo(BigDecimal.ZERO) > 0
                    ? amount.divide(totalAmount, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                    : BigDecimal.ZERO;
                statVO.setPercentage(percentage);

                result.add(statVO);
            }

            return result;
        } catch (Exception e) {
            log.error("获取分类统计数据失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public TrendAnalysisVO getTrendAnalysis(Long userId, Long bookId, Integer months) {
        TrendAnalysisVO result = new TrendAnalysisVO();
        result.setMonths(new ArrayList<>());
        result.setIncomes(new ArrayList<>());
        result.setExpenses(new ArrayList<>());
        return result;
    }

    @Override
    public Map<String, Object> getSpendingHabits(Long userId, Long bookId) {
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getBudgetExecution(Long userId, Long bookId, Integer year, Integer month) {
        Map<String, Object> result = new HashMap<>();
        result.put("hasBudget", false);
        return result;
    }

    @Override
    public BigDecimal getMonthlyAmount(Long userId, Long bookId, LocalDate startDate, LocalDate endDate, Integer type) {
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.atTime(23, 59, 59);

        BigDecimal amount = transactionMapper.sumAmountByTypeAndDateRange(userId, bookId, type, startTime, endTime);
        return amount != null ? amount : BigDecimal.ZERO;
    }
}