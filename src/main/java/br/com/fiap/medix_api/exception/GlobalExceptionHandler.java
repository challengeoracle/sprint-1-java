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

    // Retorna 404 Not Found quando uma entidade não é encontrada
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

    // Retorna 400 Bad Request para erros de validação de DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroValidacaoDto>> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        List<ErroValidacaoDto> listaErros = erros.stream()
                .map(erro -> new ErroValidacaoDto(erro.getField(), erro.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(listaErros);
    }

    // Retorna 409 Conflict para erros de unicidade de dados no banco
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
}