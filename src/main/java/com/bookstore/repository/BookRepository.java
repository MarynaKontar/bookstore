package com.bookstore.repository;

import com.bookstore.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by User on 02.12.2017.
 */
public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findAllByIsbn(int isbn);
    List<Book> findAllByCategory(String category);

}
