CREATE TABLE tb_cliente
(
    id            BIGSERIAL PRIMARY KEY,

    nome_completo VARCHAR(100) NOT NULL,
    apelido       VARCHAR(100),

    cpf           VARCHAR(11)  NOT NULL UNIQUE,

    endereco      VARCHAR(255),

    telefone      VARCHAR(20)  NOT NULL
);