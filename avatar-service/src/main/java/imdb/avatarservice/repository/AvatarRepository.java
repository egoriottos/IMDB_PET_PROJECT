package imdb.avatarservice.repository;

import imdb.avatarservice.entity.Avatar;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AvatarRepository {
    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Avatar> getAvatarRowMapper() {
        return (rs, rowNum) -> {
            Avatar avatar = new Avatar();
            avatar.setUserId(rs.getLong("user_id"));
            avatar.setFilePath(rs.getString("file_path"));
            avatar.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
            return avatar;
        };
    }

    public void save(Long id, String filePath) {
        jdbcTemplate.update("INSERT INTO avatars (user_id,file_path,uploaded_at) VALUES (?,?,?)", id,
                filePath, Timestamp.valueOf(LocalDateTime.now()));
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM avatars where user_id = ?", id);
    }

    public Optional<Avatar> findByUserId(Long userId) {
        try {
            Avatar avatar = jdbcTemplate.queryForObject(
                    "SELECT * FROM avatars WHERE user_id = ?",
                    getAvatarRowMapper(),
                    userId
            );
            return Optional.of(Objects.requireNonNull(avatar));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Avatar> updateByUserId(Long userId, String newFilePath) {
        int updatedRows = jdbcTemplate.update("UPDATE avatars SET file_path = ?, uploaded_at = ? WHERE user_id = ?",
                newFilePath, Timestamp.valueOf(LocalDateTime.now()),
                userId);
        if(updatedRows > 0) {
            Avatar avatar = jdbcTemplate.queryForObject("SELECT * FROM avatars WHERE user_id = ?",getAvatarRowMapper(),userId);
            return Optional.of(Objects.requireNonNull(avatar));
        }
        return Optional.empty();
    }
}
