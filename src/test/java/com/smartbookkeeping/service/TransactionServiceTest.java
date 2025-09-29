package com.smartbookkeeping.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartbookkeeping.domain.entity.Transaction;
import com.smartbookkeeping.domain.vo.TransactionVO;
import com.smartbookkeeping.mapper.TransactionMapper;
import com.smartbookkeeping.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 交易服务单元测试
 */
public class TransactionServiceTest {

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransaction() {
        // 准备测试数据
        Transaction transaction = new Transaction();
        transaction.setBookId(1L);
        transaction.setUserId(1L);
        transaction.setCategoryId(1L);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(2); // 支出
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setRemark("测试交易");

        // 模拟插入操作
        when(transactionMapper.insert(any())).thenReturn(1);

        // 执行创建
        boolean result = transactionService.createTransaction(transaction);

        // 验证结果
        assertTrue(result);
        verify(transactionMapper, times(1)).insert(any());
    }

    @Test
    public void testUpdateTransaction() {
        // 准备测试数据
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("200.00"));
        transaction.setRemark("更新后的交易");

        // 模拟更新操作
        when(transactionMapper.updateById(any())).thenReturn(1);

        // 执行更新
        boolean result = transactionService.updateTransaction(transaction);

        // 验证结果
        assertTrue(result);
        verify(transactionMapper, times(1)).updateById(any());
    }

    @Test
    public void testDeleteTransaction() {
        // 模拟删除操作
        when(transactionMapper.deleteById(anyLong())).thenReturn(1);

        // 执行删除
        boolean result = transactionService.deleteTransaction(1L);

        // 验证结果
        assertTrue(result);
        verify(transactionMapper, times(1)).deleteById(anyLong());
    }

    @Test
    public void testGetTransactionById() {
        // 准备测试数据
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setBookId(1L);
        transaction.setUserId(1L);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(2);
        transaction.setTransactionTime(LocalDateTime.now());

        // 模拟查询操作
        when(transactionMapper.selectById(anyLong())).thenReturn(transaction);

        // 执行查询
        TransactionVO result = transactionService.getTransactionById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(transactionMapper, times(1)).selectById(anyLong());
    }

    @Test
    public void testGetTransactionsByBookId() {
        // 准备测试数据
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setBookId(1L);
        transaction1.setAmount(new BigDecimal("100.00"));
        transactions.add(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setBookId(1L);
        transaction2.setAmount(new BigDecimal("200.00"));
        transactions.add(transaction2);

        // 模拟查询操作
        when(transactionMapper.selectList(any())).thenReturn(transactions);

        // 执行查询
        List<TransactionVO> results = transactionService.getTransactionsByBookId(1L);

        // 验证结果
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(transactionMapper, times(1)).selectList(any());
    }
}