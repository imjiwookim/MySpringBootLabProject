# [제출2-4] Book과 BookDetail (1:1 관계)

## 개요

기존에 Book 엔티티 하나로 관리하던 도서 정보를, 기본 정보(Book)와 상세 정보(BookDetail)로 분리하여 1:1 연관관계로 구현함. 양방향 연관관계, 지연 로딩(LAZY), Cascade 처리를 실습함.

## 개념 정리

- **`@OneToOne`**: 두 엔티티가 1:1로 매핑되는 연관관계를 나타냄
- **연관관계의 주인(owner)**: 외래 키(FK)를 실제로 가지는 쪽. `BookDetail`이 `book_id` 컬럼을 가지므로 주인이며, `@JoinColumn`을 사용함
- **`mappedBy`**: 주인이 아닌 쪽(`Book`)에 명시하여, 이 연관관계는 상대편(`BookDetail.book`)이 관리한다는 것을 표시. 조회 전용이며 FK를 직접 갖지 않음
- **`FetchType.LAZY`**: 연관된 엔티티를 즉시 로딩하지 않고, 실제로 접근하는 시점에 쿼리를 날려 로딩함. 불필요한 조회를 줄이기 위해 사용
- **`CascadeType.ALL` + `orphanRemoval`**: Book을 저장/삭제할 때 연관된 BookDetail도 함께 저장/삭제되도록 전파. `orphanRemoval = true`는 연관관계가 끊어진 자식 엔티티를 자동으로 삭제함
- **Fetch Join(`JOIN FETCH`)**: 지연 로딩 관계를 한 번의 쿼리로 함께 조회하고 싶을 때 JPQL에서 사용하는 방식. N+1 문제를 피하기 위해 상세 정보가 필요한 조회 메서드에 별도로 적용함

## 구현 내용

### 엔티티

- `Book.java`: 제목/저자/ISBN/가격/출판일을 저장. `bookDetail` 필드로 연관관계의 비주인 쪽 참조를 가짐(`mappedBy = "book"`)
- `BookDetail.java`: 설명/언어/페이지 수/출판사/표지 이미지 URL/에디션을 저장. `@JoinColumn(name = "book_id", unique = true)`로 외래 키를 소유하는 연관관계의 주인

### 레포지토리

- `BookRepository`: 기존 CRUD 메서드에 더해 `existsByIsbn`, `findByAuthorContaining`, `findByTitleContaining`, 그리고 BookDetail을 함께 로딩하는 `findByIdWithBookDetail`/`findByIsbnWithBookDetail` 추가
- `BookDetailRepository`: `findByBookId`, `findByIdWithBook`, `findByPublisher` 제공

### DTO

`BookDTO` 클래스 안에 4개의 중첩 클래스로 구성

| 중첩 클래스 | 용도 |
|---|---|
| `Request` | 도서 생성/수정 요청. title/author/isbn/price/publishDate 필수 검증 + `detailRequest` 포함 |
| `BookDetailDTO` | 상세 정보 요청용 |
| `Response` | 도서 응답. 기본 정보 + `detail` 포함 |
| `BookDetailResponse` | 상세 정보 응답용 |

유효성 검증: ISBN 패턴(`@Pattern`), 가격 음수 방지(`@PositiveOrZero`), 출간일 과거/오늘 제한(`@PastOrPresent`) 등을 적용함.

### 서비스

`BookService`에서 도서 전체/단건/검색 조회, 등록/수정/삭제를 처리. 등록·수정 시 ISBN 중복 여부를 `existsByIsbn`로 확인하고, 수정 시에는 **실제로 ISBN이 변경되는 경우에만** 중복 체크를 수행하도록 조건을 나눔.

```java
if (!book.getIsbn().equals(request.getIsbn())
        && bookRepository.existsByIsbn(request.getIsbn())) {
    throw new BusinessException("이미 등록된 ISBN입니다: " + request.getIsbn(), HttpStatus.CONFLICT);
}
```

### 컨트롤러

| 메서드 | 엔드포인트 | 설명 |
|---|---|---|
| GET | /api/books | 전체 도서 조회 |
| GET | /api/books/{id} | ID로 도서 조회 |
| GET | /api/books/isbn/{isbn} | ISBN으로 도서 조회 |
| GET | /api/books/search/author?author={author} | 저자로 검색 |
| GET | /api/books/search/title?title={title} | 제목으로 검색 |
| POST | /api/books | 도서 등록 (상세 정보 포함) |
| PUT | /api/books/{id} | 도서 수정 |
| DELETE | /api/books/{id} | 도서 삭제 |

## Postman 테스트 결과

<img width="1330" height="892" alt="스크린샷 2026-09-11 173721" src="https://github.com/user-attachments/assets/58dc9ff6-671a-4b32-a93e-83f691ba1545" />
<img width="1324" height="897" alt="스크린샷 2026-09-11 173702" src="https://github.com/user-attachments/assets/8c1d0df7-8a92-4ada-afc5-7cd611d03bd6" />
