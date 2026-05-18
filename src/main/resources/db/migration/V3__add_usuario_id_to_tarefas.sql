ALTER TABLE tarefas
    ADD COLUMN usuario_id BIGINT NULL REFERENCES usuarios (id);

UPDATE tarefas
SET usuario_id = (SELECT id FROM usuarios LIMIT 1)
WHERE usuario_id IS NULL;

ALTER TABLE tarefas
    ALTER COLUMN usuario_id SET NOT NULL;
