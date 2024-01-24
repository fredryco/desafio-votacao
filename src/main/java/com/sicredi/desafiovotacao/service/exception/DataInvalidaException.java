package com.sicredi.desafiovotacao.service.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DataInvalidaException extends RuntimeException {

    private LocalDateTime dataInicial;

    private LocalDateTime dataFinal;

    public DataInvalidaException(String errorMessage, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        super(errorMessage);
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

}
