package com.rookies6.MySpringBootLabProject.repository;

import com.rookies6.MySpringBootLabProject.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookDetailRepository extends JpaRepository<BookDetail, Long> {

    Optional<BookDetail> findByBookId(Long bookId);

    @Query("SELECT bd FROM BookDetail bd JOIN FETCH bd.book WHERE bd.id = :id")
    Optional<BookDetail> findByIdWithBook(@Param("id") Long id);

    List<BookDetail> findByPublisher(String publisher);
}