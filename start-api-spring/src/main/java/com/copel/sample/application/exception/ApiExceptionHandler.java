package com.copel.sample.application.exception;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.copel.pcm.model.ErrorResponse;
import com.copel.pcm.model.ErrorResponseError;
import com.copel.sample.infrastructure.wsc.WSCBusinessException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex, WebRequest request) {
        log.warn("Cliente não encontrado: {}", ex.getMessage());
        return buildErrorResponse(
            "CLIENTE_NAO_ENCONTRADO",
            "Nenhum cliente foi encontrado com o documento fornecido.",
            HttpStatus.NOT_FOUND,
            null
        );
    }

    @ExceptionHandler(LimiteUcsExcedidoException.class)
    public ResponseEntity<ErrorResponse> handleLimiteUcsExcedido(LimiteUcsExcedidoException ex, WebRequest request) {
        log.warn("Limite de UCs excedido: {}", ex.getMessage());
        return buildErrorResponse(
            "LIMITE_UCS_EXCEDIDO",
            "O cliente possui mais UCs do que o limite permitido para esta consulta.",
            HttpStatus.UNPROCESSABLE_ENTITY,
            Map.of("limiteEspecificado", ex.getLimite())
        );
    }

    @ExceptionHandler(WSCBusinessException.class)
    public ResponseEntity<ErrorResponse> handleWSCBusinessException(WSCBusinessException ex, WebRequest request) {
        log.warn("Erro de negócio retornado pela API externa: {}", ex.getMessage());
        return buildErrorResponse(
            "ERRO_SERVICO_EXTERNO",
            ex.getMessage(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Erro inesperado na aplicação", ex);
        return buildErrorResponse(
            "ERRO_INTERNO",
            "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.",
            HttpStatus.INTERNAL_SERVER_ERROR,
            null
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String code, String message, HttpStatus status, Map<String, Object> details) {
        ErrorResponseError error = new ErrorResponseError().code(code).message(message).details(details);
        ErrorResponse errorResponse = new ErrorResponse().error(error);
        return new ResponseEntity<>(errorResponse, status);
    }
}