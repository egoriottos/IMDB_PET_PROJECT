package imdb.verificationservice.service;

import java.util.Optional;

public interface CacheService {
    void addCodeToCache(String key, Long code);
    Optional<Long> getCodeFromCache(String key);
    void evictCodeFromCache(String key);
}
