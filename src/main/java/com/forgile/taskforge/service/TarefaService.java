package com.forgile.taskforge.service;

import java.util.List;

import com.forgile.taskforge.config.ApiException;
import com.forgile.taskforge.dto.TarefaRequest;
import com.forgile.taskforge.dto.TarefaResponse;
import com.forgile.taskforge.model.Tarefa;
import com.forgile.taskforge.model.enums.TarefaStatus;
import com.forgile.taskforge.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository repository;

    public TarefaResponse criar(TarefaRequest request) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setStatus(TarefaStatus.PENDENTE);
        return TarefaResponse.from(repository.save(tarefa));
    }

    public List<TarefaResponse> listar() {
        return repository.findAll().stream()
                .map(TarefaResponse::from)
                .toList();
    }

    public TarefaResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(TarefaResponse::from)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    public TarefaResponse atualizar(Long id, TarefaRequest request) {
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        return TarefaResponse.from(repository.save(tarefa));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Tarefa não encontrada");
        }
        repository.deleteById(id);
    }
}
