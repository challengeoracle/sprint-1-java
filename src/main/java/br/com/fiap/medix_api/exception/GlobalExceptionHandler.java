package br.com.fiap.medix_api.exception;

import br.com.fiap.medix_api.dto.shared.ErroValidacaoDto;
import br.com.fiap.medix_api.dto.shared.ErroPadraoDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroPadraoDto> tratarErro404(EntityNotFoundException ex, HttpServletRequest request) {
        ErroPadraoDto erro = new ErroPadraoDto(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso Não Encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroValidacaoDto>> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        List<ErroValidacaoDto> listaErros = erros.stream()
                .map(erro -> new ErroValidacaoDto(erro.getField(), erro.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(listaErros);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroPadraoDto> tratarErroDeIntegridade(DataIntegrityViolationException ex, HttpServletRequest request) {
        ErroPadraoDto erro = new ErroPadraoDto(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Conflito de Dados",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ErroPadraoDto> tratarErroDeRegraDeNegocio(RuntimeException ex, HttpServletRequest request) {
        ErroPadraoDto erro = new ErroPadraoDto(
                Instant.now(),
                HttpStatus.CONFLICT.value(), // 409 Conflict é adequado para choque de horários
                "Regra de Negócio",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }
}