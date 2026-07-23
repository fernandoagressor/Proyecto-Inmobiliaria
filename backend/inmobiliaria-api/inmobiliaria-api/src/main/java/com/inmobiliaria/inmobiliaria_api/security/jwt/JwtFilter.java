package com.inmobiliaria.inmobiliaria_api.security.jwt;

import com.inmobiliaria.inmobiliaria_api.service.impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilter(JwtService jwtService,
                     CustomUserDetailsService customUserDetailsService) {

        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n========== JWT FILTER ==========");
        System.out.println("URL: " + request.getServletPath());
        System.out.println("METODO: " + request.getMethod());

        if (request.getServletPath().startsWith("/api/auth/")) {
            System.out.println("LOGIN LIBRE");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        System.out.println("HEADER = [" + authHeader + "]");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("NO HAY TOKEN");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        System.out.println("TOKEN = [" + token + "]");
        System.out.println("LONGITUD TOKEN = " + token.length());

        try {

            String username = jwtService.extraerUsuario(token);

            System.out.println("USUARIO JWT = " + username);

            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authentication.setDetails(
                    new org.springframework.security.web.authentication.WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("USUARIO AUTENTICADO");

        } catch (Exception e) {

            System.out.println("ERROR AL LEER EL JWT");
            e.printStackTrace();

        }

        filterChain.doFilter(request, response);
    }

}