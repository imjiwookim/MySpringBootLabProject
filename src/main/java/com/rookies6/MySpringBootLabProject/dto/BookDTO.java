package com.rookies6.MySpringBootLabProject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

public class BookDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "저자는 필수입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수입니다.")
        @Pattern(regexp = "^(?:\\d{10}|\\d{13})$", message = "ISBN은 10자리 또는 13자리 숫자여야 합니다.")
        private String isbn;

        @NotNull(message = "가격은 필수입니다.")
        @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
        private Integer price;

        @NotNull(message = "출판일은 필수입니다.")
        @PastOrPresent(message = "출판일은 오늘 이전이어야 합니다.")
        private LocalDate publishDate;

        @Valid
        private BookDetailDTO detailRequest;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailDTO {

        private String description;
        private String language;

        @PositiveOrZero(message = "페이지 수는 0 이상이어야 합니다.")
        private Integer pageCount;

        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailResponse {

        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }
}