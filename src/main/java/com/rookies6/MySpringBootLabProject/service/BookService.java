package com.rookies6.MySpringBootLabProject.service;

import com.rookies6.MySpringBootLabProject.dto.BookDTO;
import com.rookies6.MySpringBootLabProject.entity.Book;
import com.rookies6.MySpringBootLabProject.exception.BusinessException;
import com.rookies6.MySpringBootLabProject.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    // 도서 등록
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        Book savedBook = bookRepository.save(book);
        return toResponse(savedBook);
    }

    // 전체 도서 조회 - Stream API로 Entity 리스트를 DTO 리스트로 변환
    @Transactional(readOnly = true)
    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ID로 도서 조회
    @Transactional(readOnly = true)
    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        return toResponse(book);
    }

    // ISBN으로 도서 조회
    @Transactional(readOnly = true)
    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException(
                        "해당 ISBN의 도서를 찾을 수 없습니다: " + isbn, HttpStatus.NOT_FOUND));
        return toResponse(book);
    }

    // 도서 정보 수정 - 입력값이 있는 필드만 변경
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));

        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            book.setPublishDate(request.getPublishDate());
        }

        // @Transactional 안에서 조회한 영속 엔티티라 save() 없이도
        // 트랜잭션 종료 시 변경 감지(dirty checking)로 자동 반영됨
        return toResponse(book);
    }

    // 도서 삭제
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        bookRepository.delete(book);
    }

    // Entity -> DTO 변환
    private BookDTO.BookResponse toResponse(Book book) {
        return BookDTO.BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .publishDate(book.getPublishDate())
                .build();
    }
}