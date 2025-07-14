package imdb.avatarservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation;
    private final String UPLOADS = "uploads";

    public FileStorageServiceImpl() throws IOException, URISyntaxException {
        URL resourcesUrl = getClass().getClassLoader().getResource("");
        if (resourcesUrl == null) {
            throw new IOException("Не удалось найти папку resources");
        }

        this.rootLocation = Paths.get(resourcesUrl.toURI())
                .resolve(UPLOADS)
                .toAbsolutePath();

        Files.createDirectories(this.rootLocation);
        System.out.println("Файлы сохраняются в: " + this.rootLocation);
    }

    @Override
    public String save(MultipartFile file, Long userId) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new FileNotFoundException("Failed to store empty file");
            }
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String filename = "avatar_" + userId + "_" + System.currentTimeMillis() + "." + (extension != null ? extension : "jpg");
            Path destination = rootLocation.resolve(filename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return "/" + UPLOADS + "/" + filename;
        } catch (IOException e) {
            throw new IOException("failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public void delete(String filePath) throws IOException {
        try {
            Path file = rootLocation.resolve(filePath.replace("/" + UPLOADS + "/", ""));
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new IOException("Failed to delete file", e);
        }
    }
}
