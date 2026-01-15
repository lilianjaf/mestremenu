CREATE TABLE IF NOT EXISTS public.usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(100) NOT NULL,
    senha VARCHAR(200) NOT NULL,
    tipo_usuario VARCHAR(50),
    data_ultima_alteracao TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    logradouro VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(255),
    bairro VARCHAR(100),
    cep VARCHAR(20),
    cidade VARCHAR(120),
    uf VARCHAR(2),
    CONSTRAINT uk_tb_usuario_email UNIQUE (email),
    CONSTRAINT uk_tb_usuario_login UNIQUE (login)
);
