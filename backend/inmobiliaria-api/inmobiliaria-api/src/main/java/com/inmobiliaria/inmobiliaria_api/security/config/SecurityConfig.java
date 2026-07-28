package com.inmobiliaria.inmobiliaria_api.security.config;

import com.inmobiliaria.inmobiliaria_api.security.jwt.JwtFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final DaoAuthenticationProvider authenticationProvider;

    public SecurityConfig(
            JwtFilter jwtFilter,
            DaoAuthenticationProvider authenticationProvider) {

        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        System.out.println(
                "******** SECURITY CONFIG CARGADA ********"
        );

        http
                .cors(cors -> cors.configurationSource(
                        corsConfigurationSource()
                ))

                .csrf(csrf -> csrf.disable())

                .authenticationProvider(authenticationProvider)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // RUTAS PÚBLICAS
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // PAGOS - SOLO ADMINISTRADOR PUEDE CREAR
                        .requestMatchers(
                                org.springframework.http.HttpMethod.POST,
                                "/api/pagos/**"
                        ).hasRole("ADMINISTRADOR")

                        // PAGOS - SOLO ADMINISTRADOR PUEDE ANULAR
                        .requestMatchers(
                                org.springframework.http.HttpMethod.PATCH,
                                "/api/pagos/**"
                        ).hasRole("ADMINISTRADOR")

                        // PAGOS - ADMINISTRADOR Y CLIENTE PUEDEN CONSULTAR
                        .requestMatchers(
                                org.springframework.http.HttpMethod.GET,
                                "/api/pagos/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "CLIENTE"
                        )

                        // RESTO DEL SISTEMA
                                // CONTRATOS - solo ADMINISTRADOR y EMPLEADO pueden crear
                                .requestMatchers(
                                        org.springframework.http.HttpMethod.POST,
                                        "/api/contratos/**"
                                ).hasAnyRole(
                                        "ADMINISTRADOR",
                                        "EMPLEADO"
                                )

                                // CONTRATOS - solo ADMINISTRADOR y EMPLEADO pueden editar
                                .requestMatchers(
                                        org.springframework.http.HttpMethod.PUT,
                                        "/api/contratos/**"
                                ).hasAnyRole(
                                        "ADMINISTRADOR",
                                        "EMPLEADO"
                                )

                                // CONTRATOS - solo ADMINISTRADOR puede eliminar
                                .requestMatchers(
                                        org.springframework.http.HttpMethod.DELETE,
                                        "/api/contratos/**"
                                ).hasRole("ADMINISTRADOR")

                                // CONTRATOS - cliente también puede consultar
                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/contratos/**"
                                ).hasAnyRole(
                                        "ADMINISTRADOR",
                                        "EMPLEADO",
                                        "CLIENTE"
                                )
                                // ===============================
                                // FACTURAS
                                // ===============================

                                // Crear facturas
                                .requestMatchers(
                                        org.springframework.http.HttpMethod.POST,
                                        "/api/facturas/**"
                                ).hasRole("ADMINISTRADOR")

                                 // Modificar facturas
                                .requestMatchers(
                                        org.springframework.http.HttpMethod.PUT,
                                        "/api/facturas/**"
                                ).hasRole("ADMINISTRADOR")

                                // Eliminar facturas
                                .requestMatchers(
                                        org.springframework.http.HttpMethod.DELETE,
                                        "/api/facturas/**"
                                ).hasRole("ADMINISTRADOR")

                                // Consultar facturas
                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/facturas/**"
                                ).hasAnyRole(
                                        "ADMINISTRADOR",
                                        "EMPLEADO",
                                        "CLIENTE"
                                )
                                // ===============================
                                // PROPIEDADES
                                // ===============================

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.POST,
                                        "/api/propiedades/**"
                                ).hasAnyRole(
                                        "ADMINISTRADOR",
                                        "EMPLEADO"
                                )

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.PUT,
                                        "/api/propiedades/**"
                                ).hasAnyRole(
                                        "ADMINISTRADOR",
                                        "EMPLEADO"
                                )

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.DELETE,
                                        "/api/propiedades/**"
                                ).hasRole("ADMINISTRADOR")

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/propiedades/**"
                                ).hasAnyRole(
                                        "ADMINISTRADOR",
                                        "EMPLEADO",
                                        "CLIENTE"
                                )
                                // ===============================
                                // RESERVAS
                                // ===============================

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.POST,
                                        "/api/reservas/**"
                                ).hasAnyRole(
                                        "ADMINISTRADOR",
                                        "EMPLEADO"
                                )

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.PUT,
                                        "/api/reservas/**"
                                ).hasAnyRole(
                                        "ADMINISTRADOR",
                                        "EMPLEADO"
                                )

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.DELETE,
                                        "/api/reservas/**"
                                ).hasRole("ADMINISTRADOR")

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/reservas/**"
                                ).hasAnyRole(
                                        "ADMINISTRADOR",
                                        "EMPLEADO",
                                        "CLIENTE"
                                )
                        .anyRequest().authenticated()
                )


                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:4200")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setExposedHeaders(
                List.of("Authorization")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}