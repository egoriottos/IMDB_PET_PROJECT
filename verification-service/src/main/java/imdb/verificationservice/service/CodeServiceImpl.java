package imdb.verificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final CacheService cacheService;

    @Override
    public Long generateCode(String email) {
        int code = 1000 + new Random().nextInt(9000);
        log.info("Cache: {}:{}", email,code);
        cacheService.addCodeToCache(email, (long) code);
        return (long) code;
    }

    @Override
    public boolean validateCode(String email,String code) {
        try {
            Optional<Long> cachedCode = cacheService.getCodeFromCache(email);
            if (cachedCode.isEmpty()) {
                log.warn("No code found in cache");
                return false;
            }

            // Сравниваем введенный код с кодом из кэша
            boolean isValid = cachedCode.get().toString().equals(code);
            log.info("Code validation result: {}", isValid);

            // Если код верный, очищаем кэш
            if (isValid) {
                cacheService.evictCodeFromCache(email);
            }

            return isValid;
        } catch (
                NumberFormatException e) {
            log.error("Invalid code format: {}", code);
            return false;
        }
    }
}
