package com.smartbookkeeping.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartbookkeeping.domain.entity.Transaction;
import com.smartbookkeeping.domain.vo.TransactionVO;
import com.smartbookkeeping.domain.dto.TransactionDTO;
import com.smartbookkeeping.domain.dto.TransactionQueryDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 交易服务接口
 */
public interface TransactionService {

    /**
     * 创建交易记录
     *
     * @param transactionDTO 交易DTO
     * @return 交易ID
     */
    Long createTransaction(TransactionDTO transactionDTO);

    /**
     * 更新交易记录
     *
     * @param id             交易ID
     * @param transactionDTO 交易DTO
     * @return 是否成功
     */
    boolean updateTransaction(Long id, TransactionDTO transactionDTO);

    /**
     * 删除交易记录
     *
     * @param id 交易ID
     * @return 是否成功
     */
    boolean deleteTransaction(Long id);

    /**
     * 获取交易记录详情
     *
     * @param id 交易ID
     * @return 交易VO
     */
    TransactionVO getTransactionById(Long id);

    /**
     * 分页查询交易记录
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<TransactionVO> pageTransactions(TransactionQueryDTO queryDTO);

    /**
     * 获取指定日期范围内的收支统计
     *
     * @param userId    用户ID
     * @param bookId    账本ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 收支统计
     */
    Map<String, BigDecimal> getIncomeExpenseSummary(Long userId, Long bookId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取指定日期范围内的分类统计
     *
     * @param userId    用户ID
     * @param bookId    账本ID
     * @param type      类型：1-收入，2-支出
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 分类统计
     */
    List<Map<String, Object>> getCategorySummary(Long userId, Long bookId, Integer type, LocalDate startDate, LocalDate endDate);

    /**
     * 通过OCR识别票据创建交易记录
     *
     * @param userId    用户ID
     * @param bookId    账本ID
     * @param imageBase64 图片Base64编码
     * @return 识别结果
     */
    TransactionDTO recognizeReceiptByOCR(Long userId, Long bookId, String imageBase64);
}