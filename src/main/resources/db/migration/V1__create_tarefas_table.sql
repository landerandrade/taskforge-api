CREATE TABLE tarefas
(
    id               BIGSERIAL PRIMARY KEY,
    titulo           VARCHAR(255) NOT NULL,
    descricao        TEXT,
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDENTE',
    data_criacao     TIMESTAMP    NOT NULL DEFAULT NOW(),
    data_atualizacao TIMESTAMP    NOT NULL DEFAULT NOW()
);
