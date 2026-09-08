# [SpringBoot 실습2-2] BookRestController + Entity + Repository

## 개요

[실습2-1]에서 만든 Book 엔티티/Repository에 REST API 컨트롤러를 추가함. 존재하지 않는 데이터 조회 시 처리 방식을 두 가지(Optional 방식, 커스텀 예외 방식)로 구현하고 비교함.


## 개념 정리

- **`@RestController`**: 메서드 반환값을 자동으로 JSON으로 직렬화해서 응답하는 컨트롤러
- **`Optional.map()` / `orElse()`**: 값이 존재하면 `map()`으로 원하는 응답을 만들고, 없으면 `orElse()`로 대체 응답(예: 404)을 반환. 예외 없이 존재 여부를 분기 처리하는 방식
- **`BusinessException`**: 비즈니스 로직 오류를 나타내는 커스텀 런타임 예외. 컨트롤러에서 조건에 맞지 않으면 이 예외를 던짐
- **`ErrorObject`**: 예외 발생 시 클라이언트에게 내려줄 에러 응답 형식(statusCode, message, timestamp 등)을 담는 클래스
- **`@RestControllerAdvice` + `@ExceptionHandler`**: 애플리케이션 전역에서 발생하는 특정 예외(`BusinessException`)를 가로채 일관된 형식의 에러 응답으로 자동 변환. 컨트롤러마다 try-catch를 반복하지 않아도 됨

## 구현 내용

| 메서드 | 엔드포인트 | 설명 | 404 처리 방식 |
|---|---|---|---|
| POST | /api/books | 도서 등록 | - |
| GET | /api/books | 전체 도서 조회 | - |
| GET | /api/books/{id} | ID로 도서 조회 | Optional.map()/orElse() |
| GET | /api/books/isbn/{isbn} | ISBN으로 도서 조회 | BusinessException + ErrorObject |
| PUT | /api/books/{id} | 도서 정보 수정 | Optional.map()/orElse() |
| DELETE | /api/books/{id} | 도서 삭제 | Optional.map()/orElse() |

### 예외 처리 클래스

- `ErrorObject.java`: 에러 응답 형식 정의
- `BusinessException.java`: 비즈니스 예외 클래스
- `DefaultExceptionAdvice.java`: 전역 예외 처리기, `BusinessException`을 가로채 `ErrorObject` 형태로 응답 변환

### Optional 방식 vs BusinessException 방식 비교

같은 "찾는 데이터가 없을 때"를 두 가지 방식으로 구현해서 차이를 비교함.

- **Optional 방식** (`getUserById`, `updateBook`, `deleteBook`): 상태 코드만 404로 내려가고 응답 body는 비어 있음
- **BusinessException 방식** (`getUserByIsbn`): 상태 코드 404와 함께 구체적인 에러 메시지가 JSON body에 담겨서 내려옴

## Postman 테스트 결과

### 1. 도서 등록 (POST /api/books)

<img width="1481" height="827" alt="image" src="https://github.com/user-attachments/assets/4824f88c-374f-4cbb-b0a8-0557978da61b" />
<img width="1558" height="885" alt="image" src="https://github.com/user-attachments/assets/0c776a62-05d6-45d0-89d7-a9b8147bb37f" />

### 2. 전체 도서 조회 (GET /api/books)

<img width="1347" height="862" alt="image" src="https://github.com/user-attachments/assets/0f3dbadb-0660-43b0-b3b2-356337911002" />

### 3. ID로 도서 조회 (GET /api/books/{id})

<img width="1365" height="885" alt="image" src="https://github.com/user-attachments/assets/3d3d3d40-4f2d-4bdc-a901-56e4d0360df3" />

### 4. ISBN으로 도서 조회 (GET /api/books/isbn/{isbn})

<img width="1362" height="878" alt="image" src="https://github.com/user-attachments/assets/dc41f905-dbcf-4da6-b1c6-c7e4c7fd7610" />


### 5. 존재하지 않는 ID/ISBN 조회 (404 처리 비교)

<img width="1367" height="877" alt="image" src="https://github.com/user-attachments/assets/3c6f2294-eca4-4776-ab9b-3b20c5f04b31" />
<img width="1375" height="880" alt="image" src="https://github.com/user-attachments/assets/22a714f7-0b4d-4aef-8ab3-2f6cac31636e" />


### 6. 도서 정보 수정 (PUT /api/books/{id})

<img width="1381" height="887" alt="image" src="https://github.com/user-attachments/assets/6f8efe53-a4cc-4b1d-895f-6ca3b7de2a9d" />


### 7. 도서 삭제 (DELETE /api/books/{id})

<img width="1367" height="892" alt="image" src="https://github.com/user-attachments/assets/432dcea4-acf4-420e-8bf4-42a52509b244" />
<img width="1371" height="883" alt="image" src="https://github.com/user-attachments/assets/f7c871c1-3461-4e5e-a7f2-de5df32b1426" />
