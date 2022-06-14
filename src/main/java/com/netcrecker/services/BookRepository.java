package com.netcrecker.services;

import com.netcrecker.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    @Query(value = "select distinct on (name) name, price from book", nativeQuery = true)
    List<String> getVariousBooks();

    @Query(value = "select b.name, b.price from book b where b.name like :name or b.price >= :price order by b.price desc", nativeQuery = true)
    List<String> getName(@Param("price") Integer price, @Param("name") String name);

}
