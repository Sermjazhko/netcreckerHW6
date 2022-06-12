package com.netcrecker.controller;

import com.netcrecker.model.Book;
import com.netcrecker.services.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

   /* @Autowired
    private BookRepository bookRepository;*/

    @GetMapping("/books")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable(value = "id") Integer id){
        try {
            Optional<Book> book = bookRepository.findById(id);
            if (book.isPresent()) {
                bookRepository.delete(book.get());
            }
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/books")
    public ResponseEntity<Book> saveBook(@RequestBody Book book){
        try {
            return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateId( @RequestBody Book book){
        try {
            return new ResponseEntity<Book>(bookRepository.save(book), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBook(@PathVariable(value = "id") Integer id) {
        try {
            Book book = bookRepository.findById(id).get();
            return new ResponseEntity<Book>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/update/{id}/{warehouse}")
    public ResponseEntity<Book> updateId(@PathVariable(value = "id") Integer id, @PathVariable String  warehouse){
        try {
            Book book = bookRepository.findById(id).get();
            book.setWarehouse(warehouse);
            return new ResponseEntity<Book>(bookRepository.save(book), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/various")
    public List<String> getVarious() {
        return bookRepository.getVariousBooks();
    }

    @GetMapping("/name")
    public List<String> getName() {
        return bookRepository.getName();
    }

}
