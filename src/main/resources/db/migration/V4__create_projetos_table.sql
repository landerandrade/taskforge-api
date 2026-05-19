CREATE TABLE projetos
(
    id               BIGSERIAL PRIMARY KEY,
    nome             VARCHAR(150) NOT NULL,
    descricao        TEXT,
    usuario_id       BIGINT       NOT NULL REFERENCES usuarios (id),
    data_criacao     TIMESTAMP    NOT NULL DEFAULT NOW(),
    data_atualizacao TIMESTAMP    NOT NULL DEFAULT NOW()
);
