package inandout.backend.service.member;

import inandout.backend.dto.member.IsPublicDTO;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.member.MemberStatus;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final PasswordEncoder passwordEncoder;

    public void updateNickname(Integer memberId, String nickname) {
        memberValidator.validateDuplicateUsername(nickname);
        System.out.println("memberId: "+memberId);
        Optional<Member> member = memberRepository.findById(memberId);
        System.out.println(member.get().getId());

        member.ifPresent(value -> value.updateNickname(nickname));
        if (member.isPresent()) {
            System.out.println("member존재함!");
            member.get().updateStatus(MemberStatus.ACTIVE);
            System.out.println(member.get().getStatus());
        }

    }

    public void validatePassword(Integer memberId, String password) {
        memberValidator.validatePassword(memberId, password);
    }

    public void updatePassword(Integer memberId, String password) {
        Optional<Member> member = memberRepository.findById(memberId);;
        member.ifPresent(value -> value.updatePassword(passwordEncoder.encode(password)));
    }

    public IsPublicDTO updateIsPublic(Integer memberId) {
        Member member = memberValidator.validateMember(memberId);
        member.updateIsPublic(!member.getIsPublic());
        IsPublicDTO isPublicDTO = new IsPublicDTO(member.getIsPublic());
        return isPublicDTO;
    }
}
