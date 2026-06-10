--modificando a coluna cpf para string
ALTER TABLE usuario ALTER COLUMN cpf TYPE VARCHAR(20);

ALTER TABLE disciplina
DROP CONSTRAINT IF EXISTS disciplina_professor_id_fkey;

ALTER TABLE disciplina
ADD CONSTRAINT disciplina_professor_id_fkey
FOREIGN KEY (professor_id)
REFERENCES professor(id)
ON DELETE CASCADE;