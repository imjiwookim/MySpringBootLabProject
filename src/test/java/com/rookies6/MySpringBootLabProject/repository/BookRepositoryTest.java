package com.rookies6.MySpringBootLabProject.repository;

import com.rookies6.MySpringBootLabProject.repository.BookRepository;
import com.rookies6.MySpringBootLabProject.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build();

        book2 = Book.builder()
                .title("JPA 프로그래밍")
                .author("박둘리")
                .isbn("9788956746432")
                .price(35000)
                .publishDate(LocalDate.of(2025, 4, 30))
                .build();
    }

    @Test
    @DisplayName("도서 등록 테스트")
    void testCreateBook() {
        Book savedBook = bookRepository.save(book1);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("스프링 부트 입문");
        assertThat(savedBook.getIsbn()).isEqualTo("9788956746425");
    }

    @Test
    @DisplayName("ISBN으로 도서 조회 테스트")
    void testFindByIsbn() {
        bookRepository.save(book1);

        Optional<Book> found = bookRepository.findByIsbn("9788956746425");

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("스프링 부트 입문");
    }

    @Test
    @DisplayName("저자명으로 도서 목록 조회 테스트")
    void testFindByAuthor() {
        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findByAuthor("홍길동");

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getIsbn()).isEqualTo("9788956746425");
    }

    @Test
    @DisplayName("도서 정보 수정 테스트")
    void testUpdateBook() {
        Book savedBook = bookRepository.save(book1);

        savedBook.setTitle("스프링 부트 입문 (개정판)");
        savedBook.setPrice(25000);
        Book updatedBook = bookRepository.save(savedBook);

        assertThat(updatedBook.getTitle()).isEqualTo("스프링 부트 입문 (개정판)");
        assertThat(updatedBook.getPrice()).isEqualTo(25000);
    }

    @Test
    @DisplayName("도서 삭제 테스트")
    void testDeleteBook() {
        Book savedBook = bookRepository.save(book1);
        Long id = savedBook.getId();

        bookRepository.deleteById(id);

        Optional<Book> deleted = bookRepository.findById(id);
        assertThat(deleted).isEmpty();
    }
}