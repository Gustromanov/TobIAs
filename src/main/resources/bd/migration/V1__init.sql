-- ============================================
-- Migration inicial do banco Tobias
-- Estrutura baseada no diagrama de classes e requisitos
-- ============================================

-- Usuário genérico (Professor ou Aluno)
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf BIGINT UNIQUE NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha VARCHAR(200) NOT NULL
);

-- Professor (especialização de Usuário)
CREATE TABLE professor (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    especialidade VARCHAR(100),
    matricula_siape VARCHAR(50) UNIQUE
);

-- Aluno (especialização de Usuário)
CREATE TABLE aluno (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    matricula VARCHAR(50) UNIQUE
);

-- Disciplina
CREATE TABLE disciplina (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    descricao TEXT
);

-- Matrícula (Aluno em Disciplina)
CREATE TABLE matricula (
    id SERIAL PRIMARY KEY,
    aluno_id INT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    disciplina_id INT NOT NULL REFERENCES disciplina(id) ON DELETE CASCADE,
    data_matricula DATE NOT NULL DEFAULT CURRENT_DATE,
    nota_final FLOAT,
    status VARCHAR(20)
);

-- Material de estudo
CREATE TABLE material (
    id SERIAL PRIMARY KEY,
    disciplina_id INT NOT NULL REFERENCES disciplina(id) ON DELETE CASCADE,
    caminho_arquivo VARCHAR(255) NOT NULL,
    data_upload TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Atividade
CREATE TABLE atividade (
    id SERIAL PRIMARY KEY,
    disciplina_id INT NOT NULL REFERENCES disciplina(id) ON DELETE CASCADE,
    titulo VARCHAR(100) NOT NULL,
    data_atribuicao DATE NOT NULL DEFAULT CURRENT_DATE,
    data_entrega DATE,
    peso FLOAT
);

-- Questão
CREATE TABLE questao (
    id SERIAL PRIMARY KEY,
    atividade_id INT NOT NULL REFERENCES atividade(id) ON DELETE CASCADE,
    enunciado TEXT NOT NULL,
    peso FLOAT
);

-- Resposta de Questão
CREATE TABLE resposta_questao (
    id SERIAL PRIMARY KEY,
    questao_id INT NOT NULL REFERENCES questao(id) ON DELETE CASCADE,
    aluno_id INT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    resposta TEXT NOT NULL
);

-- Submissão de Atividade
CREATE TABLE submissao (
    id SERIAL PRIMARY KEY,
    aluno_id INT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    atividade_id INT NOT NULL REFERENCES atividade(id) ON DELETE CASCADE,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nota FLOAT,
    feedback_professor TEXT,
    feedback_ia TEXT
);

-- Cronograma de Estudos
CREATE TABLE cronograma (
    id SERIAL PRIMARY KEY,
    aluno_id INT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    titulo VARCHAR(100),
    data_inicio DATE,
    data_limite DATE
);

-- Itens do Cronograma
CREATE TABLE item_cronograma (
    id SERIAL PRIMARY KEY,
    cronograma_id INT NOT NULL REFERENCES cronograma(id) ON DELETE CASCADE,
    data_estudo DATE,
    topico VARCHAR(200),
    status BOOLEAN DEFAULT FALSE
);

-- Relatório do Aluno
CREATE TABLE relatorio (
    id SERIAL PRIMARY KEY,
    aluno_id INT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    titulo VARCHAR(100),
    data DATE DEFAULT CURRENT_DATE,
    avaliacao TEXT
);
