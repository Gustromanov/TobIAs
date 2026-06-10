CREATE TABLE aviso (
    id SERIAL PRIMARY KEY,
    disciplina_id INT NOT NULL REFERENCES disciplina(id) ON DELETE CASCADE,
    usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    conteudo TEXT NOT NULL,
    data_publicacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE comentario_aviso (
    id SERIAL PRIMARY KEY,
    aviso_id INT NOT NULL REFERENCES aviso(id) ON DELETE CASCADE,
    usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    conteudo TEXT NOT NULL,
    data_comentario TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);