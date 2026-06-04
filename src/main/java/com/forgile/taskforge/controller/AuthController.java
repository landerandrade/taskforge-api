package com.forgile.taskforge.controller;

import com.forgile.taskforge.config.ApiResponse;
import com.forgile.taskforge.dto.AuthResponse;
import com.forgile.taskforge.dto.LoginRequest;
import com.forgile.taskforge.dto.UsuarioRequest;
import com.forgile.taskforge.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Controle de Acesso")
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    @Operation(summary = "Registra um novo usuário", description = "Registra um novo usuário com base nos dados fornecidos")
    public ApiResponse<AuthResponse> registrar(@Valid @RequestBody UsuarioRequest request) {
        return ApiResponse.ok(usuarioService.registrar(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza o login do usuário", description = "Realiza o login do usuário com base nos dados fornecidos")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(usuarioService.autenticar(request));
    }
}
