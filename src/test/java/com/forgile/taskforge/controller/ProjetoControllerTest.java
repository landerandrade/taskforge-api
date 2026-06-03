package com.forgile.taskforge.controller;

import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgile.taskforge.config.ApiException;
import com.forgile.taskforge.config.JwtUtil;
import com.forgile.taskforge.dto.PageResponse;
import com.forgile.taskforge.dto.ProjetoRequest;
import com.forgile.taskforge.dto.ProjetoResponse;
import com.forgile.taskforge.service.ProjetoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjetoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjetoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjetoService projetoService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private ProjetoResponse projetoResponse;

    private PageResponse<ProjetoResponse> pageResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        projetoResponse = new ProjetoResponse(1L, "Projeto Teste",
                "Descrição Projeto Teste", null, null);

        pageResponse = new PageResponse<>(List.of(projetoResponse), 0, 10,
                1, 1, true);
    }

    @Test
    void criar_deveRetornar201() throws Exception {
        when(projetoService.criar(any()))
                .thenReturn(projetoResponse);

        var body = objectMapper.writeValueAsString(
                new ProjetoRequest("Projeto Teste", "Descrição Projeto Teste"));

        mockMvc.perform(post("/projetos")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nome").value("Projeto Teste"))
                .andExpect(jsonPath("$.data.descricao").value("Descrição Projeto Teste"));
    }

    @Test
    void criar_deveRetornar400_quandoNomeVazio() throws Exception {
        var body = objectMapper.writeValueAsString(
                new ProjetoRequest("", "Descrição Projeto Teste"));

        mockMvc.perform(post("/projetos")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listar_deveRetornar200() throws Exception {
        when(projetoService.listar()).thenReturn(List.of(projetoResponse));

        mockMvc.perform(get("/projetos")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].nome").value("Projeto Teste"))
                .andExpect(jsonPath("$.data[0].descricao").value("Descrição Projeto Teste"));
    }

    @Test
    void buscarPorId_deveRetornar200() throws Exception {
        when(projetoService.buscarPorId(1L)).thenReturn(projetoResponse);

        mockMvc.perform(get("/projetos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nome").value("Projeto Teste"));
    }

    @Test
    void buscarPorId_deveLancar404_quandoNaoExiste() throws Exception {
        doThrow(new ApiException(HttpStatus.NOT_FOUND, "Projeto não encontrada"))
                .when(projetoService).buscarPorId(99L);

        mockMvc.perform(get("/projetos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_deveLancar404_quandoNaoExiste() throws Exception {
        doThrow(new ApiException(HttpStatus.NOT_FOUND, "Projeto não encontrada"))
                .when(projetoService).deletar(99L);

        mockMvc.perform(delete("/projetos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        doNothing().when(projetoService).deletar(1L);

        mockMvc.perform(delete("/projetos/1"))
                .andExpect(status().isNoContent());
    }
}
