package br.com.fiap.medix_api.dto.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErroPadraoDto {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}