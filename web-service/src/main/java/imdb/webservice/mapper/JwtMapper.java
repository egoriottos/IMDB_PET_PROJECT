package imdb.webservice.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtMapper {
    private final ObjectMapper mapper;


    /**
     * Извлекает JWT из JSON-строки вида {"jwt": "...."}
     *
     * @param json строка из сессии
     * @return jwt или null, если не удалось
     */
    public String extractToken(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }

        try {
            Map<String, String> map = mapper.readValue(json, Map.class);
            return map.get("token");
        } catch (IOException e) {
            System.err.println("Ошибка разбора JWT JSON: " + e.getMessage());
            return null;
        }
    }
}
