package inandout.backend.service.login;

import inandout.backend.dto.login.CustomMemberDetails;
import inandout.backend.entity.member.Member;
import inandout.backend.repository.login.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findGeneralMemberByEmail(email);
        if (!member.isEmpty()) {
            // UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomMemberDetails(member.get());
        }
        return null;
    }
}
