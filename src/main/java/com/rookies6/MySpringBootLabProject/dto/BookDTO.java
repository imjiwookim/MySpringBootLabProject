package com.rookies6.MySpringBootLabProject.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

public class BookDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookCreateRequest {

        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "저자는 필수입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수입니다.")
        @Pattern(regexp = "^(?:\\d{10}|\\d{13})$", message = "ISBN은 10자리 또는 13자리 숫자여야 합니다.")
        private String isbn;

        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 0보다 커야 합니다.")
        private Integer price;

        @NotNull(message = "출판일은 필수입니다.")
        @PastOrPresent(message = "출판일은 오늘 이전이어야 합니다.")
        private LocalDate publishDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookUpdateRequest {

        private String title;
        private String author;

        @Positive(message = "가격은 0보다 커야 합니다.")
        private Integer price;

        @PastOrPresent(message = "출판일은 오늘 이전이어야 합니다.")
        private LocalDate publishDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookResponse {

        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
    }
}