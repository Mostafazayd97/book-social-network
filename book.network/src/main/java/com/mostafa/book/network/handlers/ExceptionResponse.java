package com.mostafa.book.network.handlers;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionResponse {

    private Integer code;
    private String message;

    private Set<String> validationErrors;

}
