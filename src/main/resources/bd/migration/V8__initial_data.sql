-- ==============================================================================
-- V8: Seeding - Inserção de 15 usuários (2 Professores, 13 Alunos) e turmas
-- ==============================================================================

-- 1. Inserir 15 Usuários com a hash fornecida
INSERT INTO usuario (nome, cpf, email, senha) VALUES 
('User 1', '000.000.000-01', 'user1@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 2', '000.000.000-02', 'user2@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 3', '000.000.000-03', 'user3@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 4', '000.000.000-04', 'user4@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 5', '000.000.000-05', 'user5@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 6', '000.000.000-06', 'user6@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 7', '000.000.000-07', 'user7@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 8', '000.000.000-08', 'user8@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 9', '000.000.000-09', 'user9@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 10', '000.000.000-10', 'user10@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 11', '000.000.000-11', 'user11@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 12', '000.000.000-12', 'user12@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 13', '000.000.000-13', 'user13@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 14', '000.000.000-14', 'user14@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe'),
('User 15', '000.000.000-15', 'user15@gmail.com', '$2a$10$yLS1jrdHbAUYmOYA2s3msuKTZh2uw9x1ZQJ7LE34122T87OFx1cVe');

-- 2. Inserir Professores (User 1 e User 2)
INSERT INTO professor (usuario_id, especialidade, matricula_siape) VALUES 
(1, 'Programação', 'SIAPE001'),
(2, 'Banco de Dados', 'SIAPE002');

-- 3. Inserir Alunos (User 3 a User 15)
INSERT INTO aluno (usuario_id, matricula) VALUES 
(3, 'MAT003'),
(4, 'MAT004'),
(5, 'MAT005'),
(6, 'MAT006'),
(7, 'MAT007'),
(8, 'MAT008'),
(9, 'MAT009'),
(10, 'MAT010'),
(11, 'MAT011'),
(12, 'MAT012'),
(13, 'MAT013'),
(14, 'MAT014'),
(15, 'MAT015');

-- 4. Inserir Disciplinas
INSERT INTO disciplina (nome, codigo, descricao, professor_id) VALUES 
('Programação Orientada a Objetos', 'POO', 'Turma de POO', 1),
('Tópicos Avançados em Banco de Dados', 'TABD', 'Turma de TABD', 2);

-- 5. Matricular os 13 Alunos (IDs de 1 a 13) nas 2 Disciplinas (IDs 1 e 2)
INSERT INTO matricula (aluno_id, disciplina_id, status) VALUES 
(1, 1, 'ATIVO'), (1, 2, 'ATIVO'),
(2, 1, 'ATIVO'), (2, 2, 'ATIVO'),
(3, 1, 'ATIVO'), (3, 2, 'ATIVO'),
(4, 1, 'ATIVO'), (4, 2, 'ATIVO'),
(5, 1, 'ATIVO'), (5, 2, 'ATIVO'),
(6, 1, 'ATIVO'), (6, 2, 'ATIVO'),
(7, 1, 'ATIVO'), (7, 2, 'ATIVO'),
(8, 1, 'ATIVO'), (8, 2, 'ATIVO'),
(9, 1, 'ATIVO'), (9, 2, 'ATIVO'),
(10, 1, 'ATIVO'), (10, 2, 'ATIVO'),
(11, 1, 'ATIVO'), (11, 2, 'ATIVO'),
(12, 1, 'ATIVO'), (12, 2, 'ATIVO'),
(13, 1, 'ATIVO'), (13, 2, 'ATIVO');