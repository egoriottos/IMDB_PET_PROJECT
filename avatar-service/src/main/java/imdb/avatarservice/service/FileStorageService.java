package imdb.avatarservice.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageService {
    String save(MultipartFile file, Long userId) throws IOException;
    void delete(String filePath) throws IOException;
}
