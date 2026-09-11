package com.rookies6.MySpringBootLabProject.service;

import com.rookies6.MySpringBootLabProject.dto.BookDTO;
import com.rookies6.MySpringBootLabProject.entity.Book;
import com.rookies6.MySpringBootLabProject.entity.BookDetail;
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

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException(
                        "해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        return toResponse(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException(
                        "해당 ISBN의 도서를 찾을 수 없습니다: " + isbn, HttpStatus.NOT_FOUND));
        return toResponse(book);
    }

    public List<BookDTO.Response> searchByAuthor(String author) {
        return bookRepository.findByAuthorContaining(author).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookDTO.Response> searchByTitle(String title) {
        return bookRepository.findByTitleContaining(title).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookDTO.Response createBook(BookDTO.Request request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(
                    "이미 등록된 ISBN입니다: " + request.getIsbn(), HttpStatus.CONFLICT);
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        if (request.getDetailRequest() != null) {
            BookDTO.BookDetailDTO d = request.getDetailRequest();
            BookDetail detail = BookDetail.builder()
                    .description(d.getDescription())
                    .language(d.getLanguage())
                    .pageCount(d.getPageCount())
                    .publisher(d.getPublisher())
                    .coverImageUrl(d.getCoverImageUrl())
                    .edition(d.getEdition())
                    .build();
            book.setBookDetail(detail);
        }

        Book savedBook = bookRepository.save(book);
        return toResponse(savedBook);
    }

    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException(
                        "해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));

        // ISBN을 실제로 변경하는 경우에만 중복 체크
        if (!book.getIsbn().equals(request.getIsbn())
                && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(
                    "이미 등록된 ISBN입니다: " + request.getIsbn(), HttpStatus.CONFLICT);
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        if (request.getDetailRequest() != null) {
            BookDTO.BookDetailDTO d = request.getDetailRequest();
            BookDetail detail = book.getBookDetail();
            if (detail == null) {
                detail = new BookDetail();
                book.setBookDetail(detail);
            }
            detail.setDescription(d.getDescription());
            detail.setLanguage(d.getLanguage());
            detail.setPageCount(d.getPageCount());
            detail.setPublisher(d.getPublisher());
            detail.setCoverImageUrl(d.getCoverImageUrl());
            detail.setEdition(d.getEdition());
        }

        return toResponse(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        bookRepository.delete(book);
    }

    private BookDTO.Response toResponse(Book book) {
        BookDTO.BookDetailResponse detailResponse = null;
        if (book.getBookDetail() != null) {
            BookDetail d = book.getBookDetail();
            detailResponse = BookDTO.BookDetailResponse.builder()
                    .id(d.getId())
                    .description(d.getDescription())
                    .language(d.getLanguage())
                    .pageCount(d.getPageCount())
                    .publisher(d.getPublisher())
                    .coverImageUrl(d.getCoverImageUrl())
                    .edition(d.getEdition())
                    .build();
        }

        return BookDTO.Response.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .publishDate(book.getPublishDate())
                .detail(detailResponse)
                .build();
    }
}