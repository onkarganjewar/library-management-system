package edu.sjsu.cmpe275.project.service;

import java.util.List;

import edu.sjsu.cmpe275.project.model.Book;

/**
 * @author Onkar Ganjewar
 */
public interface BookService {

    Book findById(String id);

    List<Book> findByTitle(String title);

    Integer saveBook(Book book);

    void updateBook (Book book);

    void deleteBook(int id);

    List<Book> findAllBooks();

}
