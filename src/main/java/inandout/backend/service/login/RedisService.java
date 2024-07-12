package inandout.backend.service.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setValues(String key, String value) {
        System.out.println("RedisService/setValues");
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value);
    }

}
