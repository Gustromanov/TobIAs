ALTER TABLE material
    ADD COLUMN IF NOT EXISTS nome_arquivo VARCHAR(255),
    ADD COLUMN IF NOT EXISTS tipo_arquivo VARCHAR(150),
    ADD COLUMN IF NOT EXISTS tamanho_arquivo BIGINT;

CREATE INDEX IF NOT EXISTS idx_material_disciplina_id ON material(disciplina_id);
