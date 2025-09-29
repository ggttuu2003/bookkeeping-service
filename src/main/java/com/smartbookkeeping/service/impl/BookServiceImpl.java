package com.smartbookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartbookkeeping.domain.entity.Book;
import com.smartbookkeeping.mapper.BookMapper;
import com.smartbookkeeping.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Book createBook(Book book) {
        book.setMemberCount(1);
        book.setTransactionCount(0);
        book.setTotalIncome(BigDecimal.ZERO);
        book.setTotalExpense(BigDecimal.ZERO);
        book.setCreatedTime(LocalDateTime.now());
        book.setUpdatedTime(LocalDateTime.now());
        bookMapper.insert(book);
        return book;
    }

    @Override
    public List<Book> getBooksByUserId(Long userId) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return bookMapper.selectList(queryWrapper);
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookMapper.selectById(bookId);
    }

    @Override
    public Book updateBook(Book book) {
        book.setUpdatedTime(LocalDateTime.now());
        bookMapper.updateById(book);
        return book;
    }

    @Override
    public void deleteBook(Long bookId) {
        bookMapper.deleteById(bookId);
    }
}