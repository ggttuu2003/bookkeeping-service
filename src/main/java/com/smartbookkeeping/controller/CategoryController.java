package com.smartbookkeeping.controller;

import com.smartbookkeeping.common.ApiResponse;
import com.smartbookkeeping.domain.entity.Category;
import com.smartbookkeeping.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类列表
     * V1版本不支持自定义类目
     */
    @GetMapping
    public ApiResponse<List<Category>> getCategories(@RequestParam(required = false) Integer type) {
        // 简化实现，返回预设分类
        List<Category> categories = new ArrayList<>();

        if (type == null || type == 1) {
            // 收入分类
            categories.add(new Category().setId(11L).setName("工资").setIcon("💰").setColor("#58D68D").setType(1));
            categories.add(new Category().setId(12L).setName("理财").setIcon("📈").setColor("#5DADE2").setType(1));
            categories.add(new Category().setId(13L).setName("兼职").setIcon("💼").setColor("#F8C471").setType(1));
            categories.add(new Category().setId(14L).setName("红包").setIcon("🧧").setColor("#EC7063").setType(1));
            categories.add(new Category().setId(15L).setName("其他").setIcon("💎").setColor("#A569BD").setType(1));
        }

        if (type == null || type == 2) {
            // 支出分类
            categories.add(new Category().setId(1L).setName("餐饮").setIcon("🍽️").setColor("#FF6B6B").setType(2));
            categories.add(new Category().setId(2L).setName("交通").setIcon("🚗").setColor("#4ECDC4").setType(2));
            categories.add(new Category().setId(3L).setName("购物").setIcon("🛍️").setColor("#45B7D1").setType(2));
            categories.add(new Category().setId(4L).setName("娱乐").setIcon("🎬").setColor("#96CEB4").setType(2));
            categories.add(new Category().setId(5L).setName("居家").setIcon("🏠").setColor("#FFEAA7").setType(2));
            categories.add(new Category().setId(6L).setName("学习").setIcon("📚").setColor("#DDA0DD").setType(2));
            categories.add(new Category().setId(7L).setName("医疗").setIcon("💊").setColor("#98D8C8").setType(2));
            categories.add(new Category().setId(8L).setName("服饰").setIcon("👔").setColor("#F7DC6F").setType(2));
            categories.add(new Category().setId(9L).setName("人情").setIcon("🎁").setColor("#BB8FCE").setType(2));
            categories.add(new Category().setId(10L).setName("其他").setIcon("📝").setColor("#A9A9A9").setType(2));
        }

        return ApiResponse.success(categories);
    }
}