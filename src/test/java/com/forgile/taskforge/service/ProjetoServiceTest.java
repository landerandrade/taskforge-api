package com.forgile.taskforge.service;

import java.util.List;
import java.util.Optional;

import com.forgile.taskforge.config.ApiException;
import com.forgile.taskforge.dto.ProjetoRequest;
import com.forgile.taskforge.dto.ProjetoResponse;
import com.forgile.taskforge.model.Projeto;
import com.forgile.taskforge.model.Usuario;
import com.forgile.taskforge.repository.ProjetoRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjetoServiceTest {

    @InjectMocks
    private ProjetoService projetoService;

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Projeto projeto;
    private ProjetoRequest projetoRequest;

    @BeforeEach
    void setup() {
        var auth = new UsernamePasswordAuthenticationToken(
                "user", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("Projeto Teste");
        projeto.setDescricao("Descrição do projeto teste");

        projetoRequest = new ProjetoRequest("Projeto Teste", "Descrição do projeto teste");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void criar_deveSalvarProjeto() {
        when(usuarioRepository.findByEmail("user")).thenReturn(Optional.of(new Usuario()));
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        ProjetoResponse projetoResponse = projetoService.criar(projetoRequest);

        assertNotNull(projetoResponse);
        assertEquals("Projeto Teste", projetoResponse.nome());
        assertEquals("Descrição do projeto teste", projetoResponse.descricao());
    }

    @Test
    void listar_deveRetornarListaProjeto() {
        when(projetoRepository.findByUsuarioEmail(anyString())).thenReturn(List.of(projeto));

        List<ProjetoResponse> projetosResponse = projetoService.listar();

        assertNotNull(projetosResponse);
        assertEquals(1, projetosResponse.size());
        assertEquals("Projeto Teste", projetosResponse.getFirst().nome());
        assertEquals("Descrição do projeto teste", projetosResponse.getFirst().descricao());
    }

    @Test
    void listar_deveRetornarListaVazia_quandoNaoHaProjetos() {
        List<ProjetoResponse> projetosResponse = projetoService.listar();

        assertNotNull(projetosResponse);
        assertTrue(projetosResponse.isEmpty());
    }

    @Test
    void buscarPorId_deveRetornarProjeto() {
        when(projetoRepository.findById(anyLong())).thenReturn(Optional.of(projeto));

        ProjetoResponse projetoResponse = projetoService.buscarPorId(99L);

        assertNotNull(projetoResponse);
        assertEquals("Projeto Teste", projetoResponse.nome());
        assertEquals("Descrição do projeto teste", projetoResponse.descricao());
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoProjetoNaoExiste() {
        when(projetoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> projetoService.buscarPorId(99L));
    }

    @Test
    void deletar_deveLancarExcecao_quandoProjetoNaoExiste() {
        when(projetoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> projetoService.deletar(99L));
    }

    @Test
    void deletar_deveDeletarProjeto() {
        when(projetoRepository.findById(anyLong())).thenReturn(Optional.of(projeto));

        projetoService.deletar(99L);

        verify(projetoRepository).deleteById(99L);
    }
}
