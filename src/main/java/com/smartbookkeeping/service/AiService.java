package com.smartbookkeeping.service;

import com.smartbookkeeping.domain.dto.TransactionDTO;
import com.smartbookkeeping.domain.vo.AiPredictionVO;

import java.time.LocalDate;
import java.util.List;

/**
 * AI服务接口
 */
public interface AiService {

    /**
     * 预测未来支出
     *
     * @param userId     用户ID
     * @param bookId     账本ID
     * @param categoryId 分类ID，可为null表示所有分类
     * @param days       预测天数
     * @return 预测结果列表
     */
    List<AiPredictionVO> predictExpense(Long userId, Long bookId, Long categoryId, Integer days);

    /**
     * 预测未来收入
     *
     * @param userId     用户ID
     * @param bookId     账本ID
     * @param categoryId 分类ID，可为null表示所有分类
     * @param days       预测天数
     * @return 预测结果列表
     */
    List<AiPredictionVO> predictIncome(Long userId, Long bookId, Long categoryId, Integer days);

    /**
     * 获取消费建议
     *
     * @param userId 用户ID
     * @param bookId 账本ID
     * @return 消费建议
     */
    String getSpendingAdvice(Long userId, Long bookId);

    /**
     * 识别票据
     *
     * @param imageBase64 图片Base64编码
     * @return 交易DTO
     */
    TransactionDTO recognizeReceipt(String imageBase64);

    /**
     * 智能分类交易
     *
     * @param description 交易描述
     * @param amount      金额
     * @param type        类型：1-收入，2-支出
     * @return 推荐的分类ID
     */
    Long suggestCategory(String description, Double amount, Integer type);
}