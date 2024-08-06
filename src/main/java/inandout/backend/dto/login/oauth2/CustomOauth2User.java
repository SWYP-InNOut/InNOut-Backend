package inandout.backend.dto.login.oauth2;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
public class CustomOauth2User implements OAuth2User {
    private final UserDTO userDTO;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {

        return userDTO.getEmail();
    }

    public Integer getMemberId() {

        return userDTO.getMemberId();
    }

    public Boolean getIsActive() {

        return userDTO.getIsActive();
    }
}
