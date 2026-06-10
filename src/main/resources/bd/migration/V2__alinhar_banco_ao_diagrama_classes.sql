-- ============================================
-- Alinhamento incremental com o diagrama de classes
-- ============================================

-- Professor ministra disciplinas.
ALTER TABLE disciplina
    ADD COLUMN professor_id INT REFERENCES professor(id) ON DELETE SET NULL;

-- O diagrama modela Material com titulo e conteudo.
-- Mantemos caminho_arquivo para compatibilidade com a V1, mas ele deixa de ser obrigatorio.
ALTER TABLE material
    ADD COLUMN titulo VARCHAR(100),
    ADD COLUMN conteudo TEXT,
    ALTER COLUMN caminho_arquivo DROP NOT NULL;

-- A submissao agrega uma ou mais respostas de questoes.
ALTER TABLE resposta_questao
    ADD COLUMN submissao_id INT REFERENCES submissao(id) ON DELETE CASCADE;

-- Especializacoes de Questao no diagrama.
CREATE TABLE multipla_escolha (
    questao_id INT PRIMARY KEY REFERENCES questao(id) ON DELETE CASCADE,
    opcao_correta CHAR(1) NOT NULL
);

CREATE TABLE dissertativa (
    questao_id INT PRIMARY KEY REFERENCES questao(id) ON DELETE CASCADE,
    resposta TEXT
);

-- Cronograma possui uma lista de materiais.
CREATE TABLE cronograma_material (
    cronograma_id INT NOT NULL REFERENCES cronograma(id) ON DELETE CASCADE,
    material_id INT NOT NULL REFERENCES material(id) ON DELETE CASCADE,
    PRIMARY KEY (cronograma_id, material_id)
);

CREATE INDEX idx_disciplina_professor_id ON disciplina(professor_id);
CREATE INDEX idx_resposta_questao_submissao_id ON resposta_questao(submissao_id);
CREATE INDEX idx_cronograma_material_material_id ON cronograma_material(material_id);
