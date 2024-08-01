package inandout.backend.service.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void setValues(String refreshToken, Integer memberId) {
        System.out.println("RedisService/setValues");
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(refreshToken, String.valueOf(memberId));
    }

    public String getRefreshToken(String email){
        return (String) redisTemplate.opsForValue().get(email);
    }

    public String deleteRefreshToken(String refreshToken){
        return (String) redisTemplate.opsForValue().getAndDelete(refreshToken);
    }

    public Integer getMemberId(String refreshToken){
        return Integer.parseInt((String) redisTemplate.opsForValue().get(refreshToken));
    }

//    public void saveTokenWithClientIpAndId(String refreshToken, String clientIp, int memberId) throws JsonProcessingException {
//        System.out.println("RedisService/saveTokenWithClientIpAndId");
//        TokenToIpWithId tokenToIpWithId = new TokenToIpWithId(clientIp, memberId);
//        redisTemplate.opsForValue().set(refreshToken, objectMapper.writeValueAsString(tokenToIpWithId));
//    }
//
//    public Object getMemberIdByRefreshToken(String refreshToken) throws IOException {
//        System.out.println("RedisService/getMemberIdByRefreshToken");
//        TokenToIpWithId tokenToIpWithId = objectMapper.convertValue(redisTemplate.opsForValue().get(refreshToken).toString(), TokenToIpWithId.class);
//
//        if (tokenToIpWithId != null) {
//            return tokenToIpWithId.getMemberId();
//        }
//        return null;
//    }

}
