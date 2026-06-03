package com.forgile.taskforge.controller;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgile.taskforge.config.ApiException;
import com.forgile.taskforge.config.JwtUtil;
import com.forgile.taskforge.dto.PageResponse;
import com.forgile.taskforge.dto.TarefaRequest;
import com.forgile.taskforge.dto.TarefaResponse;
import com.forgile.taskforge.model.enums.TarefaStatus;
import com.forgile.taskforge.service.TarefaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TarefaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TarefaService tarefaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private TarefaResponse tarefaFake() {
        return new TarefaResponse(1L, "Tarefa teste", null,
                TarefaStatus.PENDENTE, null, null,
                null, null);
    }

    private PageResponse<TarefaResponse> pageResponseFake() {
        return new PageResponse<>(List.of(tarefaFake()), 0, 10,
                1, 1, true);
    }

    @Test
    void criar_deveRetornar201() throws Exception {
        when(tarefaService.criar(any())).thenReturn(tarefaFake());

        var body = objectMapper.writeValueAsString(
                new TarefaRequest("Tarefa teste", null, null));

        mockMvc.perform(post("/tarefas")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.titulo").value("Tarefa teste"));
    }

    @Test
    void criar_deveRetornar400_quandoTituloVazio() throws Exception {
        var body = objectMapper.writeValueAsString(
                new TarefaRequest("", null, null));

        mockMvc.perform(post("/tarefas")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listar_deveRetornar200() throws Exception {
        when(tarefaService.listar(any(), any(), any()))
                .thenReturn(pageResponseFake());

        mockMvc.perform(get("/tarefas")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].id").value(1L))
                .andExpect(jsonPath("$.data.content[0].titulo").value("Tarefa teste"));
    }

    @Test
    void buscarPorId_deveRetornar200() throws Exception {
        when(tarefaService.buscarPorId(1L)).thenReturn(tarefaFake());

        mockMvc.perform(get("/tarefas/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.titulo").value("Tarefa teste"));
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/tarefas/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletar_deveLancar404_quandoNaoExiste() throws Exception {
        doThrow(new ApiException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"))
                .when(tarefaService).deletar(99L);

        mockMvc.perform(delete("/tarefas/99"))
                .andExpect(status().isNotFound());
    }
}
