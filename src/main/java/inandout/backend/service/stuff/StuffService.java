package inandout.backend.service.stuff;

import inandout.backend.common.exception.MemberException;
import inandout.backend.dto.post.InOutRequestDTO;
import inandout.backend.dto.post.InOutResponseDTO;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.post.InOut;
import inandout.backend.entity.post.Post;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.repository.post.InOutRepository;
import inandout.backend.repository.post.PostRepository;
import inandout.backend.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional
public class StuffService {
    private final InOutRepository inOutRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public InOutResponseDTO saveInOut(InOutRequestDTO inOutRequestDTO) {
        Post post = postRepository.getPostByPostId(inOutRequestDTO.getPostId());
        Member member = memberRepository.findById(inOutRequestDTO.getMemberId()).get();

        // 회원이면 이전에 어떤걸 선택했었는지 확인, 비회원이면 로직 건너 뜀
        if (inOutRequestDTO.getIsMember() && inOutRepository.getExistMember(inOutRequestDTO.getMemberId())) {
            // 이전에 선택한 정보가 있으면 InOut DB에 있는 정보 update
            InOut inOut = inOutRepository.getIsCheckedInfo(inOutRequestDTO.getMemberId(), inOutRequestDTO.getPostId());
            if (!inOut.isCheckIn() && !inOut.isCheckOut()) {
                if (inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
                    // false false → true false → in +1
                    inOut.updateIn(true);
                    inOut.updateOut(false);
                    post.updateInCount(post.getInCount()+1);
                } else if (!inOutRequestDTO.getIn() && inOutRequestDTO.getOut()) {
                    // false false → false true → out +1
                    inOut.updateIn(false);
                    inOut.updateOut(true);
                    post.updateOutCount(post.getOutCount()+1);
                }
            } else if (inOut.isCheckIn() && !inOut.isCheckOut()) {
                if (!inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
                    // true false → false false → in -1
                    inOut.updateIn(false);
                    inOut.updateOut(false);
                    post.updateInCount(post.getInCount()-1);
                } else if (!inOutRequestDTO.getIn() && inOutRequestDTO.getOut()) {
                    // true false → false true → in -1, out +1
                    inOut.updateIn(false);
                    inOut.updateOut(true);
                    post.updateInCount(post.getInCount()-1);
                    post.updateOutCount(post.getOutCount()+1);
                }
            } else if (!inOut.isCheckIn()) {
                if (!inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
                    // false true → false false 이면 → out -1
                    inOut.updateIn(false);
                    inOut.updateOut(false);
                    post.updateOutCount(post.getOutCount()-1);
                } else if (inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
                    // false true → true false 이면 → out -1, in +1
                    inOut.updateIn(true);
                    inOut.updateOut(false);
                    post.updateOutCount(post.getOutCount()-1);
                    post.updateInCount(post.getInCount()+1);
                }
            }
        } else if (inOutRequestDTO.getIsMember() && !inOutRepository.getExistMember(inOutRequestDTO.getMemberId())) {
            // 이전에 선택한 정보가 없으면 새로 넣음
            if (inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
                // false false → true false → in +1
                InOut newInOut = new InOut(true, false, inOutRequestDTO.getIsMember(), post, member);
                inOutRepository.save(newInOut);
                post.updateInCount(post.getInCount()+1);
            } else if (!inOutRequestDTO.getIn() && inOutRequestDTO.getOut()) {
                // false false → false true → out +1
                InOut newInOut = new InOut(false, true, inOutRequestDTO.getIsMember(), post, member);
                inOutRepository.save(newInOut);
                post.updateOutCount(post.getOutCount()+1);
            }
        } else if (!inOutRequestDTO.getIsMember()){
            // 비회원일때
//            if (inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
//                // false false → true false → in +1
//                post.updateInCount(post.getInCount()+1);
//            } else if (!inOutRequestDTO.getIn() && inOutRequestDTO.getOut()) {
//                // false false → false true → out +1
//                post.updateOutCount(post.getOutCount()+1);
//            } else if (inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
//                post.updateOutCount(post.getOutCount()+1);
//            }
        }
        return new InOutResponseDTO(post.getInCount(), post.getOutCount());
    }

}
