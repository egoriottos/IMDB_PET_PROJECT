package imdb.avatarservice.service;

import imdb.avatarservice.entity.Avatar;
import imdb.avatarservice.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService{
    private final AvatarRepository repository;
    private final FileStorageService fileStorageService;
    private static final String DEFAULT_AVATAR_PATH = "/images/default_avatar.jpg";

    @Override
    public void setDefaultAvatar(Long userId) {
        Avatar avatar = new Avatar(userId,DEFAULT_AVATAR_PATH, LocalDateTime.now());
        repository.save(avatar.getUserId(), avatar.getFilePath());
    }

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) throws IOException {
        String newFilePath = fileStorageService.save(file,userId);

        Optional<Avatar> oldAvatar = repository.findByUserId(userId);

        oldAvatar.ifPresent(avatar -> {
            if (!avatar.getFilePath().equals(DEFAULT_AVATAR_PATH)) {
                try {
                    fileStorageService.delete(avatar.getFilePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        repository.updateByUserId(userId, newFilePath)
                .orElseThrow(() -> new IllegalStateException("Avatar not found. Call setDefaultAvatar() first."));

        return newFilePath;
    }

    @Override
    public String getAvatarPath(Long userId) {
        Optional<Avatar> avatar = repository.findByUserId(userId);
        if(avatar.isPresent() && avatar.get().getFilePath() != null) {
            return avatar.get().getFilePath();
        } else {
            return DEFAULT_AVATAR_PATH;
        }
    }

    @Override
    @Transactional
    public void deleteAvatar(Long userId) throws IOException {
        Avatar avatar = repository.findByUserId(userId).orElseThrow(IOException::new);
        fileStorageService.delete(getAvatarPath(userId));
        repository.delete(avatar.getUserId());
    }
}
