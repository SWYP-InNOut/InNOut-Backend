package inandout.backend.service.login.user;

import inandout.backend.dto.login.LoginDTO;
import inandout.backend.entity.member.Member;
import inandout.backend.repository.login.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{

    @Autowired
    private MemberRepository memberRepository;
    //회원가입

    @Transactional
    public void save(LoginDTO loginDTO) {
        // 기본은 status='ACTIVE', isPublic=true
        Member member = Member.createSocialMember(loginDTO.getName(), loginDTO.getEmail(), loginDTO.getPassword(), loginDTO.getPlatform(),"1", loginDTO.getStatus(), loginDTO.getMemberImageId());
        memberRepository.save(member);
    }

    public Optional<Member> findKakaoUser(String email) {
        Optional<Member> member = memberRepository.findActiveKakaoMemberByEmail(email);

        return member;
    }




}
