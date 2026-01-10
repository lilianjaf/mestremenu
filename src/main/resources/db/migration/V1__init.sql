-- Flyway migration: initial schema
-- Creates table "usuario" with required fields and constraints

CREATE TABLE IF NOT EXISTS public.usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(100) NOT NULL,
    data_da_ultima_alteracao TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    senha VARCHAR(200) NOT NULL,
    endereco VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(255),
    cep VARCHAR(20),
    cidade VARCHAR(120),
    pais VARCHAR(120),
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT uk_usuario_login UNIQUE (login)
);
