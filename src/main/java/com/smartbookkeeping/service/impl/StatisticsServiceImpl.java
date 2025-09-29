package com.smartbookkeeping.service.impl;

import com.smartbookkeeping.domain.entity.Budget;
import com.smartbookkeeping.domain.vo.CategoryStatVO;
import com.smartbookkeeping.domain.vo.DailyStatVO;
import com.smartbookkeeping.domain.vo.MonthlyStatVO;
import com.smartbookkeeping.domain.vo.TrendAnalysisVO;
import com.smartbookkeeping.mapper.BudgetMapper;
import com.smartbookkeeping.mapper.TransactionMapper;
import com.smartbookkeeping.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        return new ArrayList<>();
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
}