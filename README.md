# [SpringBoot 실습2-1] Entity, Repository, Test케이스 작성

## 개요

도서(Book) 정보를 DB에 저장/조회/수정/삭제할 수 있는 기능을 구현하고, 이를 검증하는 Repository 테스트 케이스를 작성함. `main` 브랜치(실습1 결과물) 기준으로 `lab_2-1` 브랜치를 새로 생성하여 진행함.

## 브랜치 전략

```bash
git checkout -b lab_2-1
```

- `main`: 실습1 (Spring Boot 프로젝트 초기 생성, `MyPropRunner`/`MyEnvironment`/`ProdConfig`/`TestConfig` 등)
- `lab_2-1`: main 기반으로 분기, 실습2-1 내용(Book 관련 클래스) 추가

## 구현 내용

### 1. Book 엔티티

`books` 테이블과 매핑되는 JPA 엔티티. 필드: `id(PK)`, `title`, `author`, `isbn(Unique)`, `publishDate`, `price`

- 패키지: `com.rookies6.MySpringBootLabProject.entity`
- Lombok(`@Getter`, `@Setter`, `@Builder` 등)으로 보일러플레이트 코드 제거

### 2. BookRepository

`JpaRepository<Book, Long>`를 상속받는 인터페이스. 기본 CRUD 메서드 외에 아래 메서드를 메서드 이름 기반 쿼리로 추가함.

- `findByIsbn(String isbn)`
- `findByAuthor(String author)`

### 3. BookRepositoryTest

`@SpringBootTest` 기반으로 아래 5개 테스트 케이스를 구현함.

| 테스트 메서드 | 검증 내용 |
|---|---|
| `testCreateBook` | 도서 등록 시 id가 자동 생성되고 값이 정확히 저장되는지 |
| `testFindByIsbn` | ISBN으로 조회 시 해당 도서가 정확히 반환되는지 |
| `testFindByAuthor` | 저자명으로 조회 시 해당 저자의 도서 목록이 반환되는지 |
| `testUpdateBook` | 도서 정보(제목, 가격) 수정 시 변경 사항이 반영되는지 |
| `testDeleteBook` | 도서 삭제 후 조회 시 존재하지 않는지 |

## 트러블슈팅

`BookRepositoryTest` 실행 시 `@SpringBootTest`가 애플리케이션 전체 컨텍스트를 로드하면서, 실습1에서 만든 `MyPropRunner`/`MyEnvironment` 설정과 충돌하는 문제가 발생함.

1. **1차 에러**: `No qualifying bean of type 'MyEnvironment' available`
   - 원인: `MyEnvironment` 빈이 `@Profile("prod")` / `@Profile("test")`에만 등록되어 있는데, 활성 프로파일 없이 테스트가 실행되어 빈 생성 실패
   - 해결: 테스트 클래스에 `@ActiveProfiles("test")` 추가

2. **2차 에러**: `Could not resolve placeholder 'myprop.username'`
   - 원인: `test` 프로파일 활성화 후 `application-test.properties`에 `myprop.username` 값이 없어 `MyPropRunner`의 `@Value` 주입 실패
   - 해결: `application-test.properties`에 `myprop.username`, `myprop.port` 값 추가

## 테스트 결과

```
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

5개 테스트 모두 통과. Book 엔티티 및 BookRepository의 CRUD 기능이 정상 동작함을 확인함.
