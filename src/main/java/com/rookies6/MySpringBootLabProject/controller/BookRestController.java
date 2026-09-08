package com.rookies6.MySpringBootLabProject.controller;

import com.rookies6.MySpringBootLabProject.entity.Book;
import com.rookies6.MySpringBootLabProject.exception.BusinessException;
import com.rookies6.MySpringBootLabProject.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookRepository bookRepository;

    // 새 도서 등록
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    // 모든 도서 조회
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    // ID로 특정 도서 조회 — Optional.map()/orElse() 사용
    @GetMapping("/{id}")
    public ResponseEntity<Book> getUserById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
    }

    // ISBN으로 도서 조회 — BusinessException + ErrorObject + Advice 사용
    @GetMapping("/isbn/{isbn}")
    public Book getUserByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException(
                        "해당 ISBN의 도서를 찾을 수 없습니다: " + isbn,
                        HttpStatus.NOT_FOUND));
    }

    // 도서 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setPrice(bookDetails.getPrice());
                    book.setPublishDate(bookDetails.getPublishDate());
                    return ResponseEntity.ok(bookRepository.save(book));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}