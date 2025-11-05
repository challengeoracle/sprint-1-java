package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.config.TokenService;
import br.com.fiap.medix_api.dto.request.LoginDto;
import br.com.fiap.medix_api.dto.response.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(
        name = "Autenticação",
        description = "Endpoints relacionados à autenticação e geração de tokens JWT."
)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Operation(
            summary = "Autenticar usuário e gerar token JWT",
            description = """
            Realiza o login de um usuário existente com e-mail e senha válidos.
            Retorna um token JWT que deve ser usado para acessar as demais rotas protegidas.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login bem-sucedido. Retorna o token JWT.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TokenDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciais inválidas.",
                            content = @Content
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String tokenJWT = tokenService.gerarToken(authentication);
        return ResponseEntity.ok(new TokenDto(tokenJWT));
    }
}
