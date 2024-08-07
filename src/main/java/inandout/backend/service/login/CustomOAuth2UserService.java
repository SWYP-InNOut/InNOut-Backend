package inandout.backend.service.login;

import inandout.backend.dto.login.oauth2.CustomOauth2User;
import inandout.backend.dto.login.oauth2.GoogleResponse;
import inandout.backend.dto.login.oauth2.OAuth2Response;
import inandout.backend.dto.login.oauth2.UserDTO;
import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.member.MemberStatus;
import inandout.backend.repository.login.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        Optional<Member> member = memberRepository.findGoogleMemberByEmail(oAuth2Response.getEmail());
        if (member.isEmpty()) {
            Member newMember = Member.createSocialMember("google", oAuth2Response.getEmail(), "", Platform.GOOGLE,"2", MemberStatus.INACTIVE, (int) ((Math.random()*6)+1));
            memberRepository.save(newMember);
            Optional<Member> savedMember = memberRepository.findGoogleMemberByEmail(oAuth2Response.getEmail());

            UserDTO userDTO = new UserDTO(savedMember.get().getId(), oAuth2Response.getEmail(), false, savedMember.get().getMemberImageId());
            return new CustomOauth2User(userDTO);
        } else {
            UserDTO userDTO = new UserDTO(member.get().getId(), oAuth2Response.getEmail(), true, member.get().getMemberImageId());
            return new CustomOauth2User(userDTO);
        }
    }
}
