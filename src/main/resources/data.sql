-- Insere uma Unidade de Saúde (ID 1)
INSERT INTO TB_MEDI_UNIDADE_SAUDE (id_unidade_saude, deleted, dt_criacao, dt_atualizacao, nm_unidade, nr_cnpj, ds_endereco, nr_telefone, tp_unidade)
VALUES (1, 0, NOW(), NOW(), 'Hospital Central', '11111111000111', 'Rua Principal, 100', '11987654321', 'Hospital');

-- Insere um usuário (colaborador) na tabela pai (TB_MEDI_USUARIO)
-- A senha 'senha123' deve ser encodada com BCrypt
INSERT INTO TB_MEDI_USUARIO (id_usuario, deleted, dt_criacao, dt_atualizacao, nm_usuario, ds_email, ds_senha_hash, nr_cpf, tp_usuario)
VALUES (1, 0, NOW(), NOW(), 'Admin Colaborador', 'admin@medix.com', '$2a$10$6Ra0FhM5OIJ2RG8tfx3tWOtm0meVQsYbYr5CHra45/PMZkUGDMYN6', '12345678900', 'COLABORADOR');

-- Insere o colaborador na tabela filha (TB_MEDI_COLABORADOR)
INSERT INTO TB_MEDI_COLABORADOR (id_usuario, id_unidade_saude, ds_cargo, dt_admissao)
VALUES (1, 1, 'Administrador Geral', '2022-01-15');

-- REINICIA A SEQUÊNCIA DE ID PARA EVITAR CONFLITOS DE CHAVE PRIMÁRIA
ALTER TABLE TB_MEDI_USUARIO ALTER COLUMN id_usuario RESTART WITH 2;
ALTER TABLE TB_MEDI_UNIDADE_SAUDE ALTER COLUMN id_unidade_saude RESTART WITH 2;