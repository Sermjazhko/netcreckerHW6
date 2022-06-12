package com.netcrecker.services;

import com.netcrecker.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    @Query(value = "select distinct on (name) name, price from book", nativeQuery = true)
    List<String> getVariousBooks();

    @Query(value = "select name, price from book where name like '%windows%' or  price >= 20000 order by price desc", nativeQuery = true)
    List<String> getName();

}
