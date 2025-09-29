package com.smartbookkeeping.controller;

import com.smartbookkeeping.common.ApiResponse;
import com.smartbookkeeping.domain.vo.PaymentMethodVO;
import com.smartbookkeeping.service.PaymentMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付方式控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    /**
     * 获取支付方式列表
     */
    @GetMapping
    public ApiResponse<List<PaymentMethodVO>> getPaymentMethods() {
        List<PaymentMethodVO> paymentMethods = paymentMethodService.getAllPaymentMethods();
        return ApiResponse.success(paymentMethods);
    }

    /**
     * 根据ID获取支付方式
     */
    @GetMapping("/{id}")
    public ApiResponse<PaymentMethodVO> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethodVO paymentMethod = paymentMethodService.getPaymentMethodById(id);
        if (paymentMethod == null) {
            return ApiResponse.error(404, "支付方式不存在");
        }
        return ApiResponse.success(paymentMethod);
    }
}