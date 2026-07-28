package com.inmobiliaria.inmobiliaria_api.security.config;

import com.inmobiliaria.inmobiliaria_api.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

                        // ===============================
                        // RUTAS PÚBLICAS
                        // ===============================

                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()


                        // ===============================
                        // CLIENTES
                        // ===============================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/clientes/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/clientes/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/clientes/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/clientes/**"
                        ).hasRole("ADMINISTRADOR")


                        // ===============================
                        // PROPIEDADES
                        // ===============================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/propiedades/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO",
                                "CLIENTE"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/propiedades/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/propiedades/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/propiedades/**"
                        ).hasRole("ADMINISTRADOR")


                        // ===============================
                        // CONTRATOS
                        // ===============================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/contratos/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO",
                                "CLIENTE"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/contratos/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/contratos/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/contratos/**"
                        ).hasRole("ADMINISTRADOR")


                        // ===============================
                        // RESERVAS
                        // ===============================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/reservas/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO",
                                "CLIENTE"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/reservas/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/reservas/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/reservas/**"
                        ).hasRole("ADMINISTRADOR")


                        // ===============================
                        // FACTURAS
                        // ===============================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/facturas/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO",
                                "CLIENTE"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/facturas/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/facturas/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/facturas/**"
                        ).hasRole("ADMINISTRADOR")


                        // ===============================
                        // PAGOS
                        // ===============================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/pagos/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "EMPLEADO",
                                "CLIENTE"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/pagos/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/pagos/**"
                        ).hasRole("ADMINISTRADOR")


                        // ===============================
                        // RESTO DE ENDPOINTS
                        // ===============================

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