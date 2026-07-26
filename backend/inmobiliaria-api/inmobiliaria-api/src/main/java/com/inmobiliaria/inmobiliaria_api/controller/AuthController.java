package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.request.LoginRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.LoginResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Usuario;
import com.inmobiliaria.inmobiliaria_api.repository.UsuarioRepository;
import com.inmobiliaria.inmobiliaria_api.security.jwt.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository) {

        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCorreo(),
                            request.getPassword()
                    )
            );

            System.out.println("===== LOGIN EXITOSO =====");

            Usuario usuario = usuarioRepository
                    .findByCorreo(request.getCorreo())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Usuario no encontrado."
                            )
                    );

            String token =
                    jwtService.generarToken(
                            request.getCorreo(),
                            usuario.getRol().getNombre()
                    );

            return ResponseEntity.ok(new LoginResponse(token));


        } catch (Exception e) {

            System.out.println("===== ERROR LOGIN =====");
            e.printStackTrace();

            throw e;
        }
    }


}