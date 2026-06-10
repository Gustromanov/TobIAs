-- adicionando a coluna foto de perfil na tabela usuario
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS foto VARCHAR(255) DEFAULT 'default.png';