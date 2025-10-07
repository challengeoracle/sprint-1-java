package br.com.fiap.medix_api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Extrai o token do cabeçalho
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            // Valida o token e extrai o username
            if (tokenService.isTokenValido(token)) {
                username = tokenService.getUsername(token);
            }
        }

        // Se o username for válido e não houver autenticação no contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carrega o usuário e cria um objeto de autenticação
            UserDetails ud = authenticationService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Define a autenticação no contexto do Spring Security
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // Continua a cadeia de filtros
        chain.doFilter(request, response);
    }
}