package imdb.userservice.config;

import imdb.userservice.entity.User;
import imdb.userservice.entity.enums.Role;
import imdb.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            List<User> users = new ArrayList<>();
            users.add(createUser("admin1", "admin1@example.com", "+1111111111", 30, Role.ADMIN));
            users.add(createUser("admin2", "admin2@example.com", "+1111111112", 35, Role.ADMIN));

            for (int i = 1; i <= 8; i++) {
                users.add(createUser(
                        "user" + i,
                        "user" + i + "@example.com",
                        "+122222222" + i,
                        20 + (i * 2),
                        Role.USER
                ));
            }

            repository.saveAll(users);
        }
    }

    private User createUser(String username, String email, String number, int age, Role role) {
        return User.builder()
                .username(username)
                .password(encoder.encode("123"))
                .email(email)
                .number(number)
                .age((long) age)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
