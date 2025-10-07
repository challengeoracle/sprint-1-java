package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.config.TokenService;
import br.com.fiap.medix_api.dto.request.LoginDto;
import br.com.fiap.medix_api.dto.response.TokenDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    // Endpoint para login, acessível publicamente
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {
        // Cria um token de autenticação com o email e senha do DTO
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha());

        // Autentica o token no Spring Security
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // Gera um token JWT para o usuário autenticado
        String tokenJWT = tokenService.gerarToken(authentication);

        // Retorna o token JWT na resposta
        return ResponseEntity.ok(new TokenDto(tokenJWT));
    }
}