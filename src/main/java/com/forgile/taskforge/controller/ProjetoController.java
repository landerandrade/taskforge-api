package com.forgile.taskforge.controller;

import java.util.List;

import com.forgile.taskforge.config.ApiResponse;
import com.forgile.taskforge.dto.ProjetoRequest;
import com.forgile.taskforge.dto.ProjetoResponse;
import com.forgile.taskforge.service.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Projetos", description = "Gerenciamento de projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria um novo projeto", description = "Cria um novo projeto com base nos dados fornecidos")
    public ApiResponse<ProjetoResponse> criar(@Valid @RequestBody ProjetoRequest request) {
        return ApiResponse.ok(projetoService.criar(request));
    }

    @GetMapping
    @Operation(summary = "Lista projetos", description = "Retorna uma lista de projetos")
    public ApiResponse<List<ProjetoResponse>> listar() {
        return ApiResponse.ok(projetoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca projeto por ID", description = "Retorna um projeto com base no ID fornecido")
    public ApiResponse<ProjetoResponse> buscarPorId(@PathVariable Long id) {
        return ApiResponse.ok(projetoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um projeto existente", description = "Atualiza um projeto existente com base nos dados fornecidos")
    public ApiResponse<ProjetoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ProjetoRequest request) {
        return ApiResponse.ok(projetoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta um projeto", description = "Deleta um projeto com base no ID fornecido")
    public void deletar(@PathVariable Long id) {
        projetoService.deletar(id);
    }
}
