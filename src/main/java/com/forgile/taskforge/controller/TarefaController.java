package com.forgile.taskforge.controller;

import java.util.List;

import com.forgile.taskforge.config.ApiResponse;
import com.forgile.taskforge.dto.TarefaRequest;
import com.forgile.taskforge.dto.TarefaResponse;
import com.forgile.taskforge.service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService service;

    @PostMapping
    public ResponseEntity<ApiResponse<TarefaResponse>> criar(@RequestBody TarefaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.criar(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TarefaResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.listar()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TarefaResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TarefaResponse>> atualizar(
            @PathVariable Long id,
            @RequestBody TarefaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
