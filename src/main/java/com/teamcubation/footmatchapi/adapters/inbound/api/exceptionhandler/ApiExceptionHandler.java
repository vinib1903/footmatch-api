package com.teamcubation.footmatchapi.adapters.inbound.api.exceptionhandler;

import com.teamcubation.footmatchapi.application.dto.response.ErroResponseDTO;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeEmUsoException;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeNaoEncontradaException;
import com.teamcubation.footmatchapi.domain.exceptions.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<Object> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErroResponseDTO errorResponse = new ErroResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<Object> handleEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErroResponseDTO errorResponse = new ErroResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Object> handleRegraDeNegocio(RegraDeNegocioException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponseDTO errorResponse = new ErroResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}