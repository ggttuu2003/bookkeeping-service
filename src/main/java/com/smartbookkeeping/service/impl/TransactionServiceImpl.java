package com.smartbookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartbookkeeping.domain.entity.Transaction;
import com.smartbookkeeping.domain.vo.TransactionVO;
import com.smartbookkeeping.domain.dto.TransactionDTO;
import com.smartbookkeeping.domain.dto.TransactionQueryDTO;
import com.smartbookkeeping.mapper.TransactionMapper;
import com.smartbookkeeping.service.TransactionService;
import com.smartbookkeeping.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {

    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDTO, transaction);

        if (transaction.getTransactionTime() == null) {
            transaction.setTransactionTime(LocalDateTime.now());
        }

        save(transaction);
        return transaction.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = getById(id);
        if (transaction == null) {
            return false;
        }

        BeanUtils.copyProperties(transactionDTO, transaction);
        transaction.setId(id);

        return updateById(transaction);
    }

    @Override
    public boolean deleteTransaction(Long id) {
        return removeById(id);
    }

    @Override
    public TransactionVO getTransactionById(Long id) {
        Transaction transaction = getById(id);
        if (transaction == null) {
            return null;
        }

        TransactionVO vo = new TransactionVO();
        BeanUtils.copyProperties(transaction, vo);

        if (transaction.getCategoryId() != null) {
            String categoryName = categoryService.getCategoryNameById(transaction.getCategoryId());
            vo.setCategoryName(categoryName);
        }

        return vo;
    }

    @Override
    public IPage<TransactionVO> pageTransactions(TransactionQueryDTO queryDTO) {
        Page<Transaction> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        if (queryDTO.getUserId() != null) {
            wrapper.eq(Transaction::getUserId, queryDTO.getUserId());
        }
        if (queryDTO.getBookId() != null) {
            wrapper.eq(Transaction::getBookId, queryDTO.getBookId());
        }
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(Transaction::getCategoryId, queryDTO.getCategoryId());
        }
        if (queryDTO.getType() != null) {
            wrapper.eq(Transaction::getType, queryDTO.getType());
        }

        wrapper.orderByDesc(Transaction::getTransactionTime);

        IPage<Transaction> transactionPage = page(page, wrapper);

        List<TransactionVO> voList = transactionPage.getRecords().stream().map(transaction -> {
            TransactionVO vo = new TransactionVO();
            BeanUtils.copyProperties(transaction, vo);

            if (transaction.getCategoryId() != null) {
                String categoryName = categoryService.getCategoryNameById(transaction.getCategoryId());
                vo.setCategoryName(categoryName);
            }

            return vo;
        }).collect(Collectors.toList());

        Page<TransactionVO> result = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        result.setRecords(voList);
        result.setTotal(transactionPage.getTotal());
        result.setPages(transactionPage.getPages());

        return result;
    }

    @Override
    public Map<String, BigDecimal> getIncomeExpenseSummary(Long userId, Long bookId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.atTime(java.time.LocalTime.MAX);

        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Transaction::getUserId, userId);
        if (bookId != null) {
            wrapper.eq(Transaction::getBookId, bookId);
        }
        wrapper.ge(Transaction::getTransactionTime, startTime);
        wrapper.le(Transaction::getTransactionTime, endTime);

        List<Transaction> transactions = list(wrapper);

        BigDecimal income = transactions.stream()
                .filter(t -> t.getType() == 1)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = transactions.stream()
                .filter(t -> t.getType() == 2)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("income", income);
        result.put("expense", expense);
        result.put("balance", income.subtract(expense));

        return result;
    }

    @Override
    public List<Map<String, Object>> getCategorySummary(Long userId, Long bookId, Integer type, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.atTime(java.time.LocalTime.MAX);

        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Transaction::getUserId, userId);
        if (bookId != null) {
            wrapper.eq(Transaction::getBookId, bookId);
        }
        wrapper.eq(Transaction::getType, type);
        wrapper.ge(Transaction::getTransactionTime, startTime);
        wrapper.le(Transaction::getTransactionTime, endTime);

        List<Transaction> transactions = list(wrapper);

        Map<Long, BigDecimal> categoryAmounts = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategoryId,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : categoryAmounts.entrySet()) {
            Map<String, Object> categoryInfo = new HashMap<>();
            categoryInfo.put("categoryId", entry.getKey());
            categoryInfo.put("categoryName", categoryService.getCategoryNameById(entry.getKey()));
            categoryInfo.put("amount", entry.getValue());
            result.add(categoryInfo);
        }

        return result;
    }

    @Override
    public TransactionDTO recognizeReceiptByOCR(Long userId, Long bookId, String imageBase64) {
        // 简化实现，返回基本的TransactionDTO
        TransactionDTO dto = new TransactionDTO();
        dto.setUserId(userId);
        dto.setBookId(bookId);
        dto.setDescription("OCR识别的交易");
        dto.setAmount(BigDecimal.ZERO);
        dto.setType(2); // 默认支出
        return dto;
    }

    private BigDecimal calculateTotalAmount(Long userId, Long bookId, Integer type, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Transaction::getUserId, userId);
        if (bookId != null) {
            wrapper.eq(Transaction::getBookId, bookId);
        }
        if (type != null) {
            wrapper.eq(Transaction::getType, type);
        }
        if (startTime != null) {
            wrapper.ge(Transaction::getTransactionTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(Transaction::getTransactionTime, endTime);
        }

        List<Transaction> transactions = list(wrapper);
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public TransactionDTO createFromText(String text, Long userId) {
        // 简化实现，返回基本的TransactionDTO
        TransactionDTO dto = new TransactionDTO();
        dto.setUserId(userId);
        dto.setDescription(text);
        dto.setAmount(BigDecimal.ZERO);
        dto.setType(2); // 默认支出
        return dto;
    }
}