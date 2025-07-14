CREATE TABLE IF NOT EXISTS avatars (
                         user_id BIGINT PRIMARY KEY,
                         file_path VARCHAR(255),
                         uploaded_at TIMESTAMP NOT NULL
);

COMMENT ON TABLE avatars IS 'Таблица для хранения аватарок пользователей';
COMMENT ON COLUMN avatars.user_id IS 'ID пользователя (внешний ключ)';
COMMENT ON COLUMN avatars.file_path IS 'Путь к файлу аватарки';
COMMENT ON COLUMN avatars.uploaded_at IS 'Дата и время загрузки аватарки';