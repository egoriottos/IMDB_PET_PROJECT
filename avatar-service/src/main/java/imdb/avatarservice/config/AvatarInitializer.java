package imdb.avatarservice.config;

import imdb.avatarservice.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AvatarInitializer implements CommandLineRunner, DisposableBean {
    private final AvatarRepository avatarRepository;
    private final List<Long> testUsersIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
    private static final String TEST_AVATAR_PATH = "/images/default_avatar.jpg";

    @Override
    public void run(String... args) throws Exception {
        testUsersIds.forEach(userId -> {
            if (avatarRepository.findByUserId(userId).isEmpty()) {
                avatarRepository.save(userId, TEST_AVATAR_PATH);
                System.out.println("Добавлен тестовый аватар для user_id: " + userId);
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        testUsersIds.forEach(userId -> {
            avatarRepository.delete(userId);
            System.out.println("Удалён тестовый аватар для user_id: " + userId);
        });
    }
}
