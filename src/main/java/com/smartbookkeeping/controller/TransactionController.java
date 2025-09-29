package com.smartbookkeeping.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartbookkeeping.common.ApiResponse;
import com.smartbookkeeping.domain.dto.TransactionDTO;
import com.smartbookkeeping.domain.dto.TransactionQueryDTO;
import com.smartbookkeeping.domain.entity.Transaction;
import com.smartbookkeeping.domain.vo.PageResult;
import com.smartbookkeeping.domain.vo.TransactionVO;
import com.smartbookkeeping.exception.BusinessException;
import com.smartbookkeeping.service.CategoryService;
import com.smartbookkeeping.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 交易记录控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 创建交易记录
     */
    @PostMapping
    public ApiResponse<TransactionVO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        // 验证交易类型
        if (transactionDTO.getType() != 1 && transactionDTO.getType() != 2) {
            throw new BusinessException(400, "交易类型必须为1或2");
        }

        // 验证支付方式ID
        if (transactionDTO.getPaymentMethodId() == null) {
            throw new BusinessException(400, "支付方式ID不能为空");
        }

        // 设置默认值
        if (transactionDTO.getBookId() == null) {
            transactionDTO.setBookId(1L);
        }

        Long transactionId = transactionService.createTransaction(transactionDTO);
        TransactionVO transactionVO = transactionService.getTransactionById(transactionId);

        return ApiResponse.success("创建成功", transactionVO);
    }

    /**
     * 分页查询交易记录
     */
    @GetMapping
    public ApiResponse<PageResult<TransactionVO>> getTransactions(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "transactionTime") String sort,
            @RequestParam(defaultValue = "desc") String order) {

        // 使用默认值
        Long currentUserId = 1L;
        Long currentBookId = 1L;

        TransactionQueryDTO queryDTO = new TransactionQueryDTO()
                .setUserId(currentUserId)
                .setBookId(currentBookId)
                .setType(type)
                .setCategoryId(categoryId)
                .setPageNum(page)
                .setPageSize(size);

        if (startDate != null) {
            queryDTO.setStartTime(startDate.atStartOfDay());
        }
        if (endDate != null) {
            queryDTO.setEndTime(endDate.atTime(23, 59, 59));
        }

        IPage<TransactionVO> pageResult = transactionService.pageTransactions(queryDTO);

        PageResult<TransactionVO> result = new PageResult<TransactionVO>()
                .setTotal(pageResult.getTotal())
                .setPages(pageResult.getPages())
                .setCurrent(pageResult.getCurrent())
                .setSize(pageResult.getSize())
                .setRecords(pageResult.getRecords());

        return ApiResponse.success(result);
    }

    /**
     * 获取交易记录详情
     */
    @GetMapping("/{transactionId}")
    public ApiResponse<TransactionVO> getTransactionDetail(@PathVariable Long transactionId) {
        TransactionVO transactionVO = transactionService.getTransactionById(transactionId);
        if (transactionVO == null) {
            throw new BusinessException(404, "交易记录不存在");
        }

        return ApiResponse.success(transactionVO);
    }

    /**
     * 更新交易记录
     */
    @PutMapping("/{transactionId}")
    public ApiResponse<Void> updateTransaction(@PathVariable Long transactionId,
                                             @RequestBody TransactionDTO transactionDTO) {
        TransactionVO existingTransaction = transactionService.getTransactionById(transactionId);
        if (existingTransaction == null) {
            throw new BusinessException(404, "交易记录不存在");
        }

        boolean success = transactionService.updateTransaction(transactionId, transactionDTO);
        if (!success) {
            throw new BusinessException(500, "更新失败");
        }

        return ApiResponse.success("更新成功", null);
    }

    /**
     * 删除交易记录
     */
    @DeleteMapping("/{transactionId}")
    public ApiResponse<Void> deleteTransaction(@PathVariable Long transactionId) {
        TransactionVO existingTransaction = transactionService.getTransactionById(transactionId);
        if (existingTransaction == null) {
            throw new BusinessException(404, "交易记录不存在");
        }

        boolean success = transactionService.deleteTransaction(transactionId);
        if (!success) {
            throw new BusinessException(500, "删除失败");
        }

        return ApiResponse.success("删除成功", null);
    }

    /**
     * OCR识别创建交易
     */
    @PostMapping("/ocr")
    public ApiResponse<TransactionDTO> recognizeReceipt(@RequestBody Map<String, Object> request) {
        Long bookId = Long.valueOf(request.get("bookId").toString());
        String imageBase64 = request.get("imageBase64").toString();
        Integer type = Integer.valueOf(request.get("type").toString());

        // 使用默认用户ID
        Long currentUserId = 1L;

        TransactionDTO result = transactionService.recognizeReceiptByOCR(currentUserId, bookId, imageBase64);
        result.setType(type);

        return ApiResponse.success("识别成功", result);
    }
}