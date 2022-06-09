package com.netcrecker.controller;

import com.netcrecker.model.Book;
import com.netcrecker.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable(value = "id") Integer id){
        try {
            Optional<Book> book = bookService.findById(id);
            if (book.isPresent()) {
                bookService.delete(book.get());
            }
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/books")
    public ResponseEntity<Book> saveBook(@RequestBody Book book){
        try {
            return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateId( @RequestBody Book book){
        try {
            return new ResponseEntity<Book>(bookService.save(book), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBook(@PathVariable(value = "id") Integer id) {
        try {
            Book book = bookService.findById(id).get();
            return new ResponseEntity<Book>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/update/{id}/{warehouse}")
    public ResponseEntity<Book> updateId(@PathVariable(value = "id") Integer id, @PathVariable Integer warehouse){
        try {
            Book book = bookService.findById(id).get();
            book.setWarehouse(warehouse);
            return new ResponseEntity<Book>(bookService.save(book), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/various")
    public List<String> getVarious() {
        return bookService.getVariousBooks();
    }

    @GetMapping("/name")
    public List<String> getName() {
        return bookService.getName();
    }

}
