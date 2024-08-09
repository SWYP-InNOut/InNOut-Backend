package inandout.backend.service.member;

import inandout.backend.dto.member.IsPublicDTO;
import inandout.backend.dto.member.ModifyUserRequestDTO;
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

    public void setNickname(Integer memberId, String nickname) {
        memberValidator.validateDuplicateUsername(nickname);
        memberValidator.validateMember(memberId);
        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isPresent()) {
            member.get().updateNickname(nickname);
            member.get().updateStatus(MemberStatus.ACTIVE);
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

    public boolean isDuplicateNickname(String nickname, Integer memberId) {
        if (memberRepository.isDuplicateNickname(nickname, memberId)) { // 중복이면
            System.out.println("중복되는 닉네임");
            return true;
        }
        return false;


    }

    public void updateProfile(ModifyUserRequestDTO modifyUserRequestDTO) {
        //닉네임 없데이트
        memberRepository.updateMemberNameImageId(modifyUserRequestDTO.getMemberId(), modifyUserRequestDTO.getNickname(), modifyUserRequestDTO.getMemberImageId());

    }
}
