package imdb.avatarservice.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface AvatarService {
    String uploadAvatar(Long userId, MultipartFile file) throws IOException;
    String getAvatarPath(Long userId);
    void deleteAvatar(Long userId) throws IOException;
    void setDefaultAvatar(Long userId);
}
