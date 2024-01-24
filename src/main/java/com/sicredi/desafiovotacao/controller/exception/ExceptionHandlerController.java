package com.sicredi.desafiovotacao.controller.exception;

import com.sicredi.desafiovotacao.dto.SessaoResponse;
import com.sicredi.desafiovotacao.service.exception.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> executarValidacaoException(final MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(Collectors.joining(", ", "", "."));

        return montarExceptionResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(DataInvalidaException.class)
    @ResponseBody
    public ResponseEntity<SessaoResponse> executarDataInvalidaException(final DataInvalidaException ex) {
        return construirSessaoResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(VotoInvalidoException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> executarVotoInvalidoException(final VotoInvalidoException ex) {
        return montarExceptionResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> executarEntidadeNaoEncontradaException(final EntidadeNaoEncontradaException ex) {
        return montarExceptionResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({SessaoEncerradaException.class, VotoUnicoException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> executarEntidadeNaoEncontradaException(final Exception ex) {
        return montarExceptionResponse(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
    }

    private ResponseEntity<ExceptionResponse> montarExceptionResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(new ExceptionResponse(status.getReasonPhrase(), message), status);
    }

    private ResponseEntity<SessaoResponse> construirSessaoResponse(HttpStatus status, DataInvalidaException ex) {
        return new ResponseEntity<>(SessaoResponse.of("", ex.getMessage(), ex.getDataInicial(), ex.getDataFinal()), status);
    }
}