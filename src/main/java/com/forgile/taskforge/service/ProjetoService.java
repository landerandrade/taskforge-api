package com.forgile.taskforge.service;

import java.util.List;
import java.util.Objects;

import com.forgile.taskforge.config.ApiException;
import com.forgile.taskforge.dto.ProjetoRequest;
import com.forgile.taskforge.dto.ProjetoResponse;
import com.forgile.taskforge.model.Projeto;
import com.forgile.taskforge.model.Usuario;
import com.forgile.taskforge.repository.ProjetoRepository;
import com.forgile.taskforge.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProjetoResponse criar(ProjetoRequest request) {
        Usuario usuario = getUsuarioAutenticado();
        Projeto projeto = new Projeto();
        projeto.setNome(request.nome());
        projeto.setDescricao(request.descricao());
        projeto.setUsuario(usuario);
        return ProjetoResponse.from(projetoRepository.save(projeto));
    }

    public List<ProjetoResponse> listar() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return projetoRepository.findByUsuarioEmail(email)
                .stream()
                .map(ProjetoResponse::from)
                .toList();
    }

    public ProjetoResponse buscarPorId(Long id) {
        return ProjetoResponse.from(getProjeto(id));
    }

    public ProjetoResponse atualizar(Long id, ProjetoRequest request) {
        Projeto projeto = getProjeto(id);
        projeto.setNome(request.nome());
        projeto.setDescricao(request.descricao());
        return ProjetoResponse.from(projetoRepository.save(projeto));
    }

    public void deletar(Long id) {
        getProjeto(id);
        projetoRepository.deleteById(id);
    }

    private Projeto getProjeto(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Projeto não encontrado"));
    }

    private Usuario getUsuarioAutenticado() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
    }
}
