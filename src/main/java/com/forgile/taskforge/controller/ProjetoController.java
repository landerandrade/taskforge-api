package com.forgile.taskforge.controller;

import java.util.List;

import com.forgile.taskforge.config.ApiResponse;
import com.forgile.taskforge.dto.ProjetoRequest;
import com.forgile.taskforge.dto.ProjetoResponse;
import com.forgile.taskforge.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProjetoResponse> criar(@Valid @RequestBody ProjetoRequest request) {
        return ApiResponse.ok(projetoService.criar(request));
    }

    @GetMapping
    public ApiResponse<List<ProjetoResponse>> listar() {
        return ApiResponse.ok(projetoService.listar());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjetoResponse> buscarPorId(@PathVariable Long id) {
        return ApiResponse.ok(projetoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProjetoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ProjetoRequest request) {
        return ApiResponse.ok(projetoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        projetoService.deletar(id);
    }
}
