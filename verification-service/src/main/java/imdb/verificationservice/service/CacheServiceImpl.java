package imdb.verificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
@Slf4j
public class CacheServiceImpl implements CacheService{
    private final Cache cache;

    public CacheServiceImpl(CacheManager cacheManager) {
        cache = cacheManager.getCache("verificationCode");
    }

    @Override
    public void addCodeToCache(String key, Long code) {
        log.info("Adding to cache key:{}",key);
        log.info("Adding to cache code:{}", code);
        cache.put(key,code.toString());
        log.info("Data added to cache: Key = %s , Code = %d".formatted(key, code));
    }

    @Override
    public Optional<Long> getCodeFromCache(String key) {
        String cachedValue = cache.get(key, String.class);
        log.info("Get code from cache by key:{}", key);
        return Optional.of(Long.parseLong(cachedValue));
    }

    @Override
    public void evictCodeFromCache(String key) {
        log.info("Evicting code from cache. Key: {}", key);
        cache.evict(key);
    }
}
