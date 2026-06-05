package com.forgile.taskforge.service;

import java.util.Objects;

import com.forgile.taskforge.config.ApiException;
import com.forgile.taskforge.dto.PageResponse;
import com.forgile.taskforge.dto.TarefaRequest;
import com.forgile.taskforge.dto.TarefaResponse;
import com.forgile.taskforge.model.Tarefa;
import com.forgile.taskforge.model.Usuario;
import com.forgile.taskforge.model.enums.TarefaStatus;
import com.forgile.taskforge.repository.ProjetoRepository;
import com.forgile.taskforge.repository.TarefaRepository;
import com.forgile.taskforge.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public PageResponse<TarefaResponse> listar(TarefaStatus status, Long projetoId, Pageable pageable) {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        Page<Tarefa> page;
        if (status != null && projetoId == null) {
            page = repository.findByUsuarioEmailAndStatus(email, status, pageable);
        } else if (projetoId != null && status == null) {
            page = repository.findByUsuarioEmailAndProjetoId(email, projetoId, pageable);
        } else if (status != null) {
            page = repository.findByUsuarioEmailAndStatusAndProjetoId(email, status, projetoId, pageable);
        } else {
            page = repository.findByUsuarioEmail(email, pageable);
        }
        return PageResponse.from(page.map(TarefaResponse::from));
    }

    public TarefaResponse buscarPorId(Long id) {
        return TarefaResponse.from(getTarefa(id));
    }

    public TarefaResponse atualizar(Long id, TarefaRequest request) {
        Tarefa tarefa = getTarefa(id);
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        if (request.status() != null) {
            tarefa.setStatus(request.status());
        }
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
