package com.smartbookkeeping.service.impl;

import com.smartbookkeeping.domain.dto.TransactionDTO;
import com.smartbookkeeping.domain.entity.Transaction;
import com.smartbookkeeping.domain.vo.AiPredictionVO;
import com.smartbookkeeping.mapper.TransactionMapper;
import com.smartbookkeeping.service.AiService;
import com.smartbookkeeping.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * AI服务实现类
 */
@Service
@Slf4j
public class AiServiceImpl implements AiService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private CategoryService categoryService;

    @Value("${app.ai.enabled:true}")
    private boolean aiEnabled;

    @Value("${app.ai.prediction-days:30}")
    private int defaultPredictionDays;

    @Value("${app.ai.api-key:}")
    private String aiApiKey;

    @Value("${app.ai.api-url:}")
    private String aiApiUrl;

    private final WebClient webClient;

    public AiServiceImpl() {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public List<AiPredictionVO> predictExpense(Long userId, Long bookId, Long categoryId, Integer days) {
        if (!aiEnabled) {
            return Collections.emptyList();
        }

        days = (days == null || days <= 0) ? defaultPredictionDays : days;

        // 获取历史交易数据
        List<Transaction> historicalTransactions = getHistoricalTransactions(userId, bookId, categoryId, 2, 90);

        // 如果历史数据不足，返回空结果
        if (historicalTransactions.size() < 10) {
            return Collections.emptyList();
        }

        try {
            // 在实际项目中，这里应该调用AI模型API进行预测
            // 这里使用模拟数据演示
            return simulatePrediction(historicalTransactions, days, 2);
        } catch (Exception e) {
            log.error("预测支出失败", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<AiPredictionVO> predictIncome(Long userId, Long bookId, Long categoryId, Integer days) {
        if (!aiEnabled) {
            return Collections.emptyList();
        }

        days = (days == null || days <= 0) ? defaultPredictionDays : days;

        // 获取历史交易数据
        List<Transaction> historicalTransactions = getHistoricalTransactions(userId, bookId, categoryId, 1, 90);

        // 如果历史数据不足，返回空结果
        if (historicalTransactions.size() < 10) {
            return Collections.emptyList();
        }

        try {
            // 在实际项目中，这里应该调用AI模型API进行预测
            // 这里使用模拟数据演示
            return simulatePrediction(historicalTransactions, days, 1);
        } catch (Exception e) {
            log.error("预测收入失败", e);
            return Collections.emptyList();
        }
    }

    @Override
    public String getSpendingAdvice(Long userId, Long bookId) {
        if (!aiEnabled) {
            return "AI服务未启用，无法提供消费建议。";
        }

        // 获取最近30天的支出数据
        List<Transaction> recentTransactions = getHistoricalTransactions(userId, bookId, null, 2, 30);

        if (recentTransactions.isEmpty()) {
            return "数据不足，无法提供消费建议。";
        }

        // 计算总支出
        BigDecimal totalExpense = recentTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 按分类统计支出
        Map<Long, BigDecimal> categoryExpenses = recentTransactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategoryId,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        // 找出支出最多的分类
        Map.Entry<Long, BigDecimal> topCategory = categoryExpenses.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // 生成消费建议
        StringBuilder advice = new StringBuilder();
        advice.append("根据您最近30天的消费数据分析：\n\n");
        advice.append("1. 总支出：").append(totalExpense).append("元\n");

        if (topCategory != null) {
            String categoryName = categoryService.getCategoryNameById(topCategory.getKey());
            BigDecimal percentage = topCategory.getValue().multiply(new BigDecimal("100"))
                    .divide(totalExpense, 2, RoundingMode.HALF_UP);

            advice.append("2. 最高支出类别：").append(categoryName)
                  .append("，占总支出的").append(percentage).append("%\n");

            // 根据支出比例给出建议
            if (percentage.compareTo(new BigDecimal("50")) > 0) {
                advice.append("3. 建议：您在").append(categoryName)
                      .append("上的支出比例过高，建议适当控制这方面的开支。\n");
            }
        }

        // 添加一般性建议
        advice.append("4. 一般建议：\n");
        advice.append("   - 建立预算计划，控制非必要支出\n");
        advice.append("   - 关注经常性小额支出，它们累积起来可能是一笔不小的开销\n");
        advice.append("   - 定期检查您的消费模式，及时调整消费习惯\n");

        return advice.toString();
    }

    @Override
    public TransactionDTO recognizeReceipt(String imageBase64) {
        if (!aiEnabled || imageBase64 == null || imageBase64.isEmpty()) {
            return null;
        }

        try {
            // 在实际项目中，这里应该调用OCR API识别票据
            // 这里使用模拟数据演示
            return simulateReceiptRecognition();
        } catch (Exception e) {
            log.error("识别票据失败", e);
            return null;
        }
    }

    @Override
    public Long suggestCategory(String description, Double amount, Integer type) {
        if (!aiEnabled || description == null || description.isEmpty()) {
            return null;
        }

        try {
            // 在实际项目中，这里应该调用AI模型API进行分类预测
            // 这里使用模拟数据演示
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("description", description);
            requestBody.put("amount", amount);
            requestBody.put("type", type);

            // 模拟调用外部AI服务
            // 实际项目中应该使用WebClient调用真实的AI服务
            /*
            return webClient.post()
                    .uri(aiApiUrl + "/suggest-category")
                    .header("Authorization", "Bearer " + aiApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();
            */

            // 模拟返回结果
            return simulateCategorySuggestion(description, type);
        } catch (Exception e) {
            log.error("智能分类失败", e);
            return null;
        }
    }

    /**
     * 获取历史交易数据
     */
    private List<Transaction> getHistoricalTransactions(Long userId, Long bookId, Long categoryId, Integer type, Integer days) {
        // 简化实现，返回空列表
        return new ArrayList<>();
    }

    /**
     * 模拟预测结果
     */
    private List<AiPredictionVO> simulatePrediction(List<Transaction> historicalTransactions, Integer days, Integer type) {
        List<AiPredictionVO> predictions = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // 计算历史平均值和标准差
        BigDecimal sum = historicalTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal avg = sum.divide(new BigDecimal(historicalTransactions.size()), 2, RoundingMode.HALF_UP);
        
        // 计算标准差
        BigDecimal variance = historicalTransactions.stream()
                .map(t -> t.getAmount().subtract(avg).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(historicalTransactions.size()), 2, RoundingMode.HALF_UP);
        
        BigDecimal stdDev = new BigDecimal(Math.sqrt(variance.doubleValue()));

        // 生成预测数据
        Random random = new Random();
        for (int i = 1; i <= days; i++) {
            LocalDate predictionDate = today.plusDays(i);
            
            // 根据历史数据生成预测值，加入一些随机波动
            double randomFactor = 0.8 + random.nextDouble() * 0.4; // 0.8 - 1.2之间的随机因子
            BigDecimal predictedAmount = avg.multiply(new BigDecimal(randomFactor))
                    .setScale(2, RoundingMode.HALF_UP);
            
            // 计算置信度 (60% - 95%)
            BigDecimal confidence = new BigDecimal(60 + random.nextInt(36))
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            
            AiPredictionVO prediction = new AiPredictionVO();
            prediction.setPredictionDate(predictionDate);
            prediction.setPredictedAmount(predictedAmount);
            prediction.setConfidence(confidence);
            prediction.setType(type);
            
            predictions.add(prediction);
        }

        return predictions;
    }

    /**
     * 模拟票据识别
     */
    private TransactionDTO simulateReceiptRecognition() {
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(new BigDecimal(ThreadLocalRandom.current().nextDouble(10, 200)).setScale(2, RoundingMode.HALF_UP));
        dto.setType(2); // 支出
        dto.setDescription("超市购物");
        dto.setTransactionTime(LocalDateTime.now());
        
        // 模拟识别的分类ID (假设超市购物对应的分类ID为8)
        dto.setCategoryId(8L);
        
        return dto;
    }

    /**
     * 模拟分类建议
     */
    private Long simulateCategorySuggestion(String description, Integer type) {
        // 简单的关键词匹配
        String lowerDesc = description.toLowerCase();
        
        if (type == 1) { // 收入
            if (lowerDesc.contains("工资") || lowerDesc.contains("薪水") || lowerDesc.contains("salary")) {
                return 1L; // 工资分类ID
            } else if (lowerDesc.contains("奖金") || lowerDesc.contains("bonus")) {
                return 2L; // 奖金分类ID
            } else if (lowerDesc.contains("投资") || lowerDesc.contains("股票") || lowerDesc.contains("基金")) {
                return 3L; // 投资收益分类ID
            } else {
                return 6L; // 其他收入分类ID
            }
        } else { // 支出
            if (lowerDesc.contains("餐") || lowerDesc.contains("饭") || lowerDesc.contains("吃") || lowerDesc.contains("food")) {
                return 7L; // 餐饮分类ID
            } else if (lowerDesc.contains("购物") || lowerDesc.contains("超市") || lowerDesc.contains("shopping")) {
                return 8L; // 购物分类ID
            } else if (lowerDesc.contains("交通") || lowerDesc.contains("车") || lowerDesc.contains("地铁") || lowerDesc.contains("公交")) {
                return 9L; // 交通分类ID
            } else if (lowerDesc.contains("房") || lowerDesc.contains("租") || lowerDesc.contains("水电")) {
                return 10L; // 住房分类ID
            } else {
                return 16L; // 其他支出分类ID
            }
        }
    }
}