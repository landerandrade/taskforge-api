package com.forgile.taskforge.service;

import java.util.List;
import java.util.Optional;

import com.forgile.taskforge.config.ApiException;
import com.forgile.taskforge.dto.TarefaRequest;
import com.forgile.taskforge.dto.TarefaResponse;
import com.forgile.taskforge.model.Tarefa;
import com.forgile.taskforge.model.Usuario;
import com.forgile.taskforge.model.enums.TarefaStatus;
import com.forgile.taskforge.repository.ProjetoRepository;
import com.forgile.taskforge.repository.TarefaRepository;
import com.forgile.taskforge.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @InjectMocks
    private TarefaService tarefaService;

    @Mock private TarefaRepository tarefaRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ProjetoRepository projetoRepository;

    @BeforeEach
    void setup() {
        var auth = new UsernamePasswordAuthenticationToken(
                "user", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private Tarefa tarefaFake() {
        Tarefa t = new Tarefa();
        t.setId(1L);
        t.setTitulo("Tarefa teste");
        t.setStatus(TarefaStatus.PENDENTE);
        return t;
    }

    @Test
    void criar_deveSalvarTarefa() {
        var request = new TarefaRequest("Tarefa teste", null, null);

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(new Usuario()));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaFake());

        TarefaResponse response = tarefaService.criar(request);

        assertNotNull(response);
        assertEquals("Tarefa teste", response.titulo());
    }

    @Test
    void criar_deveLancarExcecao_quandoProjetoNaoExiste() {
        var request = new TarefaRequest("Tarefa teste", null, 99L);

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(new Usuario()));
        when(projetoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> tarefaService.criar(request));
    }

    @Test
    void buscarPorId_deveRetornarTarefa() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.of(tarefaFake()));

        TarefaResponse response = tarefaService.buscarPorId(99L);

        assertNotNull(response);
        assertEquals("Tarefa teste", response.titulo());
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoTarefaNaoExiste() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> tarefaService.buscarPorId(99L));
    }

    @Test
    void deletar_deveDeletarTarefa() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.of(tarefaFake()));

        tarefaService.deletar(99L);

        verify(tarefaRepository).deleteById(99L);
    }

    @Test
    void deletar_deveLancarExcecao_quandoNaoExiste() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> tarefaService.deletar(99L));
    }
}
