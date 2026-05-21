package com.forgile.taskforge.service;

import java.util.List;
import java.util.Objects;

import com.forgile.taskforge.config.ApiException;
import com.forgile.taskforge.dto.TarefaRequest;
import com.forgile.taskforge.dto.TarefaResponse;
import com.forgile.taskforge.model.Tarefa;
import com.forgile.taskforge.model.Usuario;
import com.forgile.taskforge.model.enums.TarefaStatus;
import com.forgile.taskforge.repository.ProjetoRepository;
import com.forgile.taskforge.repository.TarefaRepository;
import com.forgile.taskforge.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ProjetoRepository projetoRepository;

    public TarefaResponse criar(TarefaRequest request) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setStatus(TarefaStatus.PENDENTE);
        tarefa.setUsuario(getUsuarioAutenticado());
        if (request.projetoId() != null) {
            tarefa.setProjeto(projetoRepository.findById(request.projetoId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Projeto não encontrado")));
        }
        return TarefaResponse.from(repository.save(tarefa));
    }

    public List<TarefaResponse> listar() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return repository.findByUsuarioEmail(email).stream()
                .map(TarefaResponse::from)
                .toList();
    }

    public TarefaResponse buscarPorId(Long id) {
        return TarefaResponse.from(getTarefa(id));
    }

    public TarefaResponse atualizar(Long id, TarefaRequest request) {
        Tarefa tarefa = getTarefa(id);
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        if (request.projetoId() != null) {
            tarefa.setProjeto(projetoRepository.findById(request.projetoId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Projeto não encontrado")));
        } else {
            tarefa.setProjeto(null);
        }
        return TarefaResponse.from(repository.save(tarefa));
    }

    public void deletar(Long id) {
        getTarefa(id);
        repository.deleteById(id);
    }

    private Tarefa getTarefa(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    private Usuario getUsuarioAutenticado() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado"));
    }
}
