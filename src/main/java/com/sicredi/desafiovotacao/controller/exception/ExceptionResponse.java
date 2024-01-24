package com.sicredi.desafiovotacao.controller.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse {

    private String httpStatus;
    private String message;
}