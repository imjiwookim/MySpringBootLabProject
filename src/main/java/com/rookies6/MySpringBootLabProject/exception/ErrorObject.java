package com.rookies6.MySpringBootLabProject.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorObject {

    private int statusCode;
    private String message;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}