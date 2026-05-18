package com.forgile.taskforge.controller;

import com.forgile.taskforge.config.ApiResponse;
import com.forgile.taskforge.dto.AuthResponse;
import com.forgile.taskforge.dto.LoginRequest;
import com.forgile.taskforge.dto.UsuarioRequest;
import com.forgile.taskforge.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ApiResponse<AuthResponse> registrar(@Valid @RequestBody UsuarioRequest request) {
        return ApiResponse.ok(usuarioService.registrar(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(usuarioService.autenticar(request));
    }
}
