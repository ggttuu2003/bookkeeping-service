package com.smartbookkeeping.controller;

import com.smartbookkeeping.common.ApiResponse;
import com.smartbookkeeping.domain.dto.BookDTO;
import com.smartbookkeeping.domain.entity.Book;
import com.smartbookkeeping.domain.vo.BookVO;
import com.smartbookkeeping.exception.BusinessException;
import com.smartbookkeeping.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 账本管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 创建账本
     */
    @PostMapping
    public ApiResponse<BookVO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        // 简化实现，实际应该从JWT token中获取用户ID
        Long currentUserId = 1L;

        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        book.setUserId(currentUserId);

        Book createdBook = bookService.createBook(book);

        BookVO bookVO = new BookVO();
        BeanUtils.copyProperties(createdBook, bookVO);
        bookVO.setBookId(createdBook.getId())
               .setCreateTime(createdBook.getCreatedTime());

        return ApiResponse.success("创建成功", bookVO);
    }

    /**
     * 获取账本列表
     */
    @GetMapping
    public ApiResponse<List<BookVO>> getBooks() {
        // 简化实现，实际应该从JWT token中获取用户ID
        Long currentUserId = 1L;

        List<Book> books = bookService.getBooksByUserId(currentUserId);

        List<BookVO> bookVOs = books.stream().map(book -> {
            BookVO bookVO = new BookVO();
            BeanUtils.copyProperties(book, bookVO);
            bookVO.setBookId(book.getId())
                   .setCreateTime(book.getCreatedTime());
            return bookVO;
        }).collect(Collectors.toList());

        return ApiResponse.success(bookVOs);
    }

    /**
     * 获取账本详情
     */
    @GetMapping("/{bookId}")
    public ApiResponse<BookVO> getBookDetail(@PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new BusinessException(404, "账本不存在");
        }

        BookVO bookVO = new BookVO();
        BeanUtils.copyProperties(book, bookVO);
        bookVO.setBookId(book.getId())
               .setCreateTime(book.getCreatedTime());

        return ApiResponse.success(bookVO);
    }

    /**
     * 更新账本信息
     */
    @PutMapping("/{bookId}")
    public ApiResponse<Void> updateBook(@PathVariable Long bookId, @RequestBody BookDTO bookDTO) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new BusinessException(404, "账本不存在");
        }

        // 更新字段
        if (bookDTO.getName() != null) {
            book.setName(bookDTO.getName());
        }
        if (bookDTO.getDescription() != null) {
            book.setDescription(bookDTO.getDescription());
        }
        if (bookDTO.getIcon() != null) {
            book.setIcon(bookDTO.getIcon());
        }
        if (bookDTO.getColor() != null) {
            book.setColor(bookDTO.getColor());
        }

        bookService.updateBook(book);

        return ApiResponse.success("更新成功", null);
    }

    /**
     * 删除账本
     */
    @DeleteMapping("/{bookId}")
    public ApiResponse<Void> deleteBook(@PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new BusinessException(404, "账本不存在");
        }

        bookService.deleteBook(bookId);

        return ApiResponse.success("删除成功", null);
    }
}