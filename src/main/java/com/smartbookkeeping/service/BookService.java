package com.smartbookkeeping.service;

import com.smartbookkeeping.domain.entity.Book;
import java.util.List;

public interface BookService {

    Book createBook(Book book);

    List<Book> getBooksByUserId(Long userId);

    Book getBookById(Long bookId);

    Book updateBook(Book book);

    void deleteBook(Long bookId);
}