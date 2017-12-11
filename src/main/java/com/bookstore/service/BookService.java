package com.bookstore.service;

import com.bookstore.domain.Book;

import java.util.List;

/**
 * Created by User on 02.12.2017.
 */
public interface BookService {

    Book getBook(Long id);
    List<Book> getAllBooks();
}
