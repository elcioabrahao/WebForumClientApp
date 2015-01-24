CREATE TABLE cliente (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    nome     VARCHAR,
    cpf      VARCHAR,
    telefone VARCHAR,
    email    VARCHAR
);
CREATE TABLE mensagem (
    id                INTEGER PRIMARY KEY,
    id_cliente        INTEGER,
    nome_cliente      VARCHAR,
    data              VARCHAR,
    mensagem          VARCHAR
);