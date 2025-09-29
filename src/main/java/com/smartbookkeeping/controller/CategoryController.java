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
     */
    @GetMapping
    public ApiResponse<List<Category>> getCategories(@RequestParam(required = false) Integer type) {
        List<Category> categories = categoryService.getCategoriesByType(type);
        return ApiResponse.success(categories);
    }
}