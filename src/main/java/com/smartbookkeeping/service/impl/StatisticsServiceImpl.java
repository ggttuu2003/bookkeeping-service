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
        try {
            int year = startDate.getYear();
            int month = startDate.getMonthValue();

            log.debug("获取每日统计数据: userId={}, bookId={}, year={}, month={}", userId, bookId, year, month);

            // 从数据库获取每日统计数据
            List<Map<String, Object>> dailyData = transactionMapper.getDailyStats(userId, bookId, year, month);

            log.debug("数据库返回每日统计记录数: {}", dailyData.size());

            // 创建结果列表
            List<DailyStatVO> result = new ArrayList<>();

            // 生成当月所有日期的数据
            int daysInMonth = startDate.lengthOfMonth();
            for (int day = 1; day <= daysInMonth; day++) {
                DailyStatVO dailyStat = new DailyStatVO();
                dailyStat.setDate(LocalDate.of(year, month, day));
                dailyStat.setIncome(BigDecimal.ZERO);
                dailyStat.setExpense(BigDecimal.ZERO);

                // 查找是否有该日的数据
                for (Map<String, Object> data : dailyData) {
                    Integer dataDay = (Integer) data.get("day");
                    if (dataDay != null && dataDay == day) {
                        BigDecimal income = (BigDecimal) data.get("income");
                        BigDecimal expense = (BigDecimal) data.get("expense");
                        dailyStat.setIncome(income != null ? income : BigDecimal.ZERO);
                        dailyStat.setExpense(expense != null ? expense : BigDecimal.ZERO);
                        break;
                    }
                }

                result.add(dailyStat);
            }

            return result;
        } catch (Exception e) {
            log.error("获取每日统计数据失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<MonthlyStatVO> getMonthlyStats(Long userId, Long bookId, Integer year) {
        try {
            // 从数据库获取月度统计数据
            List<Map<String, Object>> monthlyData = transactionMapper.getMonthlyStats(userId, bookId, year);

            // 创建结果列表
            List<MonthlyStatVO> result = new ArrayList<>();

            // 生成12个月的数据
            for (int month = 1; month <= 12; month++) {
                MonthlyStatVO monthlyStat = new MonthlyStatVO();
                monthlyStat.setYear(year);
                monthlyStat.setMonth(month);
                monthlyStat.setIncome(BigDecimal.ZERO);
                monthlyStat.setExpense(BigDecimal.ZERO);

                // 查找是否有该月的数据
                for (Map<String, Object> data : monthlyData) {
                    Integer dataMonth = (Integer) data.get("month");
                    if (dataMonth != null && dataMonth == month) {
                        BigDecimal income = (BigDecimal) data.get("income");
                        BigDecimal expense = (BigDecimal) data.get("expense");
                        monthlyStat.setIncome(income != null ? income : BigDecimal.ZERO);
                        monthlyStat.setExpense(expense != null ? expense : BigDecimal.ZERO);
                        break;
                    }
                }

                // 计算净额
                monthlyStat.setNet(monthlyStat.getIncome().subtract(monthlyStat.getExpense()));
                result.add(monthlyStat);
            }

            return result;
        } catch (Exception e) {
            log.error("获取月度统计数据失败", e);
            return new ArrayList<>();
        }
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

        log.debug("查询参数: userId={}, bookId={}, type={}, startTime={}, endTime={}",
                 userId, bookId, type, startTime, endTime);

        BigDecimal amount = transactionMapper.sumAmountByTypeAndDateRange(userId, bookId, type, startTime, endTime);

        log.debug("查询结果: amount={}", amount);

        return amount != null ? amount : BigDecimal.ZERO;
    }
}