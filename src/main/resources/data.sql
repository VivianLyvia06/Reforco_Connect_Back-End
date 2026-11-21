-- =====================================================================
-- Dados de exemplo para a demonstração (Reforço Connect)
--
-- Roda a cada inicialização (spring.sql.init.mode=always), DEPOIS do
-- Hibernate criar/atualizar as tabelas (defer-datasource-initialization).
-- Cada INSERT só acontece se a tabela estiver VAZIA (WHERE NOT EXISTS)
-- e se as linhas referenciadas pela FK existirem,
-- então reiniciar a aplicação não duplica dados, e o que você apagar
-- durante a demonstração continua apagado.
--
-- Totais esperados no relatório: 8 professores, 42 agendamentos, 63 h.
-- André Ribeiro NÃO tem agendamentos: prova do LEFT JOIN (aparece com 0).
-- =====================================================================

-- Instituições
INSERT INTO instituicao (id, nome, telefone, endereco)
SELECT * FROM (VALUES
    (1, 'Instituto Caminhos', '(12) 3621-4410', 'Rua das Palmeiras, 120 - Centro, Taubaté/SP'),
    (2, 'ONG Aprender Juntos', '(12) 3632-1187', 'Av. Independência, 845 - Vila Mariana, Taubaté/SP'),
    (3, 'Centro Comunitário Sementes', '(12) 3608-2295', 'Rua Quatro de Março, 310 - Jardim das Nações, Taubaté/SP')
) AS dados(id, nome, telefone, endereco)
WHERE NOT EXISTS (SELECT 1 FROM instituicao);

-- Professores
INSERT INTO professor (id, nome, disciplina, foto_url, instituicao_id)
SELECT * FROM (VALUES
    (1, 'Rafael Mendes', 'Matemática', 'https://exemplo.org/fotos/rafael.jpg', 1),
    (2, 'Juliana Costa', 'Português', 'https://exemplo.org/fotos/juliana.jpg', 2),
    (3, 'Beatriz Lima', 'Matemática', 'https://exemplo.org/fotos/beatriz.jpg', 3),
    (4, 'Lucas Almeida', 'Ciências', 'https://exemplo.org/fotos/lucas.jpg', 1),
    (5, 'Camila Santos', 'Inglês', 'https://exemplo.org/fotos/camila.jpg', 2),
    (6, 'Pedro Oliveira', 'História', 'https://exemplo.org/fotos/pedro.jpg', 3),
    (7, 'Mariana Souza', 'Geografia', 'https://exemplo.org/fotos/mariana.jpg', 1),
    (8, 'André Ribeiro', 'Física', 'https://exemplo.org/fotos/andre.jpg', 2)
) AS dados(id, nome, disciplina, foto_url, instituicao_id)
WHERE NOT EXISTS (SELECT 1 FROM professor)
  AND (SELECT COUNT(*) FROM instituicao WHERE id IN (1, 2, 3)) = 3;

-- Agendamentos
INSERT INTO agendamento (id, aluno_nome, data_hora, duracao_minutos, status, professor_id)
SELECT * FROM (VALUES
    (1, 'Ana Clara Souza', TIMESTAMP '2026-10-05 09:00:00', 60, 'Concluído', 1),
    (2, 'Bruno Henrique', TIMESTAMP '2026-10-08 14:00:00', 120, 'Concluído', 1),
    (3, 'Carla Mendonça', TIMESTAMP '2026-10-11 10:00:00', 60, 'Concluído', 1),
    (4, 'Diego Ferreira', TIMESTAMP '2026-10-14 15:00:00', 120, 'Concluído', 1),
    (5, 'Eduarda Rocha', TIMESTAMP '2026-10-17 11:00:00', 60, 'Concluído', 1),
    (6, 'Felipe Nogueira', TIMESTAMP '2026-10-20 16:00:00', 120, 'Concluído', 1),
    (7, 'Gabriela Prado', TIMESTAMP '2026-10-23 12:00:00', 60, 'Concluído', 1),
    (8, 'Heitor Campos', TIMESTAMP '2026-10-26 17:00:00', 120, 'Concluído', 1),
    (9, 'Isabela Martins', TIMESTAMP '2026-10-29 13:00:00', 60, 'Concluído', 1),
    (10, 'João Pedro Lima', TIMESTAMP '2026-11-01 09:00:00', 120, 'Concluído', 1),
    (11, 'Larissa Teixeira', TIMESTAMP '2026-11-04 14:00:00', 60, 'Concluído', 1),
    (12, 'Matheus Ribeiro', TIMESTAMP '2026-11-07 10:00:00', 120, 'Concluído', 1),
    (13, 'Natália Gomes', TIMESTAMP '2026-11-10 15:00:00', 60, 'Concluído', 2),
    (14, 'Otávio Barros', TIMESTAMP '2026-11-13 11:00:00', 120, 'Concluído', 2),
    (15, 'Ana Clara Souza', TIMESTAMP '2026-11-16 16:00:00', 60, 'Concluído', 2),
    (16, 'Bruno Henrique', TIMESTAMP '2026-11-19 12:00:00', 120, 'Concluído', 2),
    (17, 'Carla Mendonça', TIMESTAMP '2026-11-22 17:00:00', 60, 'Agendado', 2),
    (18, 'Diego Ferreira', TIMESTAMP '2026-11-25 13:00:00', 120, 'Confirmado', 2),
    (19, 'Eduarda Rocha', TIMESTAMP '2026-11-28 09:00:00', 60, 'Agendado', 2),
    (20, 'Felipe Nogueira', TIMESTAMP '2026-10-07 14:00:00', 120, 'Concluído', 2),
    (21, 'Gabriela Prado', TIMESTAMP '2026-10-10 10:00:00', 90, 'Concluído', 2),
    (22, 'Heitor Campos', TIMESTAMP '2026-10-13 15:00:00', 60, 'Concluído', 3),
    (23, 'Isabela Martins', TIMESTAMP '2026-10-16 11:00:00', 120, 'Concluído', 3),
    (24, 'João Pedro Lima', TIMESTAMP '2026-10-19 16:00:00', 60, 'Concluído', 3),
    (25, 'Larissa Teixeira', TIMESTAMP '2026-10-22 12:00:00', 120, 'Concluído', 3),
    (26, 'Matheus Ribeiro', TIMESTAMP '2026-10-25 17:00:00', 60, 'Concluído', 3),
    (27, 'Natália Gomes', TIMESTAMP '2026-10-28 13:00:00', 120, 'Concluído', 3),
    (28, 'Otávio Barros', TIMESTAMP '2026-10-31 09:00:00', 90, 'Concluído', 3),
    (29, 'Ana Clara Souza', TIMESTAMP '2026-11-03 14:00:00', 60, 'Concluído', 4),
    (30, 'Bruno Henrique', TIMESTAMP '2026-11-06 10:00:00', 120, 'Concluído', 4),
    (31, 'Carla Mendonça', TIMESTAMP '2026-11-09 15:00:00', 60, 'Concluído', 4),
    (32, 'Diego Ferreira', TIMESTAMP '2026-11-12 11:00:00', 120, 'Concluído', 4),
    (33, 'Eduarda Rocha', TIMESTAMP '2026-11-15 16:00:00', 60, 'Concluído', 4),
    (34, 'Felipe Nogueira', TIMESTAMP '2026-11-18 12:00:00', 120, 'Concluído', 4),
    (35, 'Gabriela Prado', TIMESTAMP '2026-11-21 17:00:00', 60, 'Agendado', 5),
    (36, 'Heitor Campos', TIMESTAMP '2026-11-24 13:00:00', 120, 'Confirmado', 5),
    (37, 'Isabela Martins', TIMESTAMP '2026-11-27 09:00:00', 60, 'Agendado', 5),
    (38, 'João Pedro Lima', TIMESTAMP '2026-10-06 14:00:00', 120, 'Concluído', 5),
    (39, 'Larissa Teixeira', TIMESTAMP '2026-10-09 10:00:00', 60, 'Concluído', 6),
    (40, 'Matheus Ribeiro', TIMESTAMP '2026-10-12 15:00:00', 120, 'Concluído', 6),
    (41, 'Natália Gomes', TIMESTAMP '2026-10-15 11:00:00', 90, 'Concluído', 6),
    (42, 'Otávio Barros', TIMESTAMP '2026-10-18 16:00:00', 90, 'Concluído', 7)
) AS dados(id, aluno_nome, data_hora, duracao_minutos, status, professor_id)
WHERE NOT EXISTS (SELECT 1 FROM agendamento)
  AND (SELECT COUNT(*) FROM professor WHERE id BETWEEN 1 AND 8) = 8;

-- Ajusta os contadores de id (IDENTITY). Como os INSERTs acima informam o id
-- explicitamente, sem isto o próximo POST tentaria usar id = 1 e daria conflito.
-- O COALESCE cobre tabela vazia; o terceiro parâmetro (false) faz o próximo id ser 1.
SELECT setval(pg_get_serial_sequence('instituicao', 'id'), COALESCE((SELECT MAX(id) FROM instituicao), 0) + 1, false);
SELECT setval(pg_get_serial_sequence('professor', 'id'), COALESCE((SELECT MAX(id) FROM professor), 0) + 1, false);
SELECT setval(pg_get_serial_sequence('agendamento', 'id'), COALESCE((SELECT MAX(id) FROM agendamento), 0) + 1, false);
