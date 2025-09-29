package com.smartbookkeeping.service;

import com.smartbookkeeping.domain.entity.Category;
import java.util.List;

public interface CategoryService {

    Category findById(Long id);

    List<Category> findByUserId(Long userId);

    Category save(Category category);

    Category update(Category category);

    void deleteById(Long id);

    String getCategoryNameById(Long categoryId);
}