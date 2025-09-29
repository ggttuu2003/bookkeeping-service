package com.smartbookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartbookkeeping.domain.entity.Category;
import com.smartbookkeeping.mapper.CategoryMapper;
import com.smartbookkeeping.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category findById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<Category> findByUserId(Long userId) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public Category save(Category category) {
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public Category update(Category category) {
        categoryMapper.updateById(category);
        return category;
    }

    @Override
    public void deleteById(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public String getCategoryNameById(Long categoryId) {
        Category category = findById(categoryId);
        return category != null ? category.getName() : null;
    }
}