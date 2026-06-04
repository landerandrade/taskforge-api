package com.forgile.taskforge.controller;

import com.forgile.taskforge.config.ApiResponse;
import com.forgile.taskforge.dto.PageResponse;
import com.forgile.taskforge.dto.TarefaRequest;
import com.forgile.taskforge.dto.TarefaResponse;
import com.forgile.taskforge.model.enums.TarefaStatus;
import com.forgile.taskforge.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Gerenciamento de tarefas")
public class TarefaController {

    private final TarefaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria uma nova tarefa", description = "Cria uma nova tarefa com base nos dados fornecidos")
    public ApiResponse<TarefaResponse> criar(@Valid @RequestBody TarefaRequest request) {
        return ApiResponse.ok(service.criar(request));
    }

    @GetMapping
    @Operation(summary = "Lista tarefas", description = "Retorna uma lista paginada de tarefas com base nos filtros fornecidos")
    public ApiResponse<PageResponse<TarefaResponse>> listar(
            @RequestParam(required = false) TarefaStatus status,
            @RequestParam(required = false) Long projetoId,
            @PageableDefault(sort = "titulo") Pageable pageable) {
        return ApiResponse.ok(service.listar(status, projetoId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca tarefa por ID", description = "Retorna uma tarefa com base no ID fornecido")
    public ApiResponse<TarefaResponse> buscarPorId(@PathVariable Long id) {
        return ApiResponse.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma tarefa existente", description = "Atualiza uma tarefa existente com base nos dados fornecidos")
    public ApiResponse<TarefaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody TarefaRequest request) {
        return ApiResponse.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta uma tarefa", description = "Deleta uma tarefa com base no ID fornecido")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
