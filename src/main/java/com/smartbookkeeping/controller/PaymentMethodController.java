package com.smartbookkeeping.controller;

import com.smartbookkeeping.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付方式控制器
 */
@Slf4j
@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodController {

    /**
     * 获取支付方式列表
     */
    @GetMapping
    public ApiResponse<List<Map<String, Object>>> getPaymentMethods() {
        List<Map<String, Object>> paymentMethods = new ArrayList<>();

        Map<String, Object> wechat = new HashMap<>();
        wechat.put("id", 1);
        wechat.put("name", "微信支付");
        wechat.put("icon", "💬");
        wechat.put("color", "#1AAD19");
        paymentMethods.add(wechat);

        Map<String, Object> alipay = new HashMap<>();
        alipay.put("id", 2);
        alipay.put("name", "支付宝");
        alipay.put("icon", "💙");
        alipay.put("color", "#1677FF");
        paymentMethods.add(alipay);

        Map<String, Object> bankCard = new HashMap<>();
        bankCard.put("id", 3);
        bankCard.put("name", "银行卡");
        bankCard.put("icon", "💳");
        bankCard.put("color", "#FA8C16");
        paymentMethods.add(bankCard);

        Map<String, Object> cash = new HashMap<>();
        cash.put("id", 4);
        cash.put("name", "现金");
        cash.put("icon", "💵");
        cash.put("color", "#52C41A");
        paymentMethods.add(cash);

        return ApiResponse.success(paymentMethods);
    }
}