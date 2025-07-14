package imdb.avatarservice.controller;

import imdb.avatarservice.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/avatar-service")
@RequiredArgsConstructor
public class AvatarController {
    private final AvatarService avatarService;

    // Установка дефолтного аватара (POST /api/avatars/default/{userId})
    @PostMapping("/default/{userId}")
    public ResponseEntity<String> setDefaultAvatar(@PathVariable Long userId) {
        avatarService.setDefaultAvatar(userId);
        return ResponseEntity.ok("Дефолтный аватар установлен для user_id: " + userId);
    }

    // Загрузка аватара (POST /api/avatars/{userId})
    @PostMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String filePath = avatarService.uploadAvatar(userId, file);
            return ResponseEntity.ok("Аватар загружен. Путь: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Ошибка загрузки файла");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Получение пути к аватару (GET /api/avatars/{userId})
    @GetMapping("/{userId}")
    public ResponseEntity<String> getAvatarPath(@PathVariable Long userId) {
        String filePath = avatarService.getAvatarPath(userId);
        return ResponseEntity.ok(filePath);
    }

    // Удаление аватара (DELETE /api/avatars/{userId})
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteAvatar(@PathVariable Long userId) {
        try {
            avatarService.deleteAvatar(userId);
            return ResponseEntity.ok("Аватар удалён");
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
