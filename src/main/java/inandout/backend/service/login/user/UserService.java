package inandout.backend.service.login.user;

import inandout.backend.dto.login.LoginDTO;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.member.MemberStatus;
import inandout.backend.repository.login.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService{

    @Autowired
    private MemberRepository memberRepository;
    //회원가입
    public void save(LoginDTO loginDTO) {
        // 기본은 status='ACTIVE', isPublic=true
        Member member = new Member(loginDTO.getName(), loginDTO.getEmail(), "Temp_memberImgUrl",loginDTO.getPassword(), loginDTO.getPlatform(),
                loginDTO.getPlatformId(), LocalDateTime.now(), LocalDateTime.now(), MemberStatus.ACTIVE, true);

        memberRepository.save(member);
    }

    public Optional<Member> findUser(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);

        return member;
    }

}
