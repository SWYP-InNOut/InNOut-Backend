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
        if (inOutRepository.getExistMember(inOutRequestDTO.getMemberId())) {
            InOut inOut = inOutRepository.getIsCheckedInfo(inOutRequestDTO.getMemberId(), inOutRequestDTO.getPostId());
            if (!inOut.isCheckIn() && !inOut.isCheckOut()) {
                if (inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
                    // false false → true false → in +1
                    InOut newInOut = new InOut(true, false, inOutRequestDTO.getIsMember(), post, member);
                    inOutRepository.save(inOut);
                    post.updateInCount(post.getInCount()+1);
                } else if (!inOutRequestDTO.getIn() && inOutRequestDTO.getOut()) {
                    // false false → false true → out +1
                    InOut newInOut = new InOut(false, true, inOutRequestDTO.getIsMember(), post, member);
                    inOutRepository.save(inOut);
                    post.updateOutCount(post.getOutCount()+1);
                }
            } else if (inOut.isCheckIn() && !inOut.isCheckOut()) {
                if (!inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
                    // true false → false false → in -1
                    InOut newInOut = new InOut(false, false, inOutRequestDTO.getIsMember(), post, member);
                    inOutRepository.save(inOut);
                    post.updateInCount(post.getInCount()-1);
                } else if (!inOutRequestDTO.getIn() && inOutRequestDTO.getOut()) {
                    // true false → false true → in -1, out +1
                    InOut newInOut = new InOut(false, true, inOutRequestDTO.getIsMember(), post, member);
                    inOutRepository.save(inOut);
                    post.updateInCount(post.getInCount()-1);
                    post.updateOutCount(post.getOutCount()+1);
                }
            } else if (!inOut.isCheckIn()) {
                if (!inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
                    // false true → false false 이면 → out -1
                    InOut newInOut = new InOut(false, false, inOutRequestDTO.getIsMember(), post, member);
                    inOutRepository.save(inOut);
                    post.updateOutCount(post.getOutCount()-1);
                } else if (inOutRequestDTO.getIn() && !inOutRequestDTO.getOut()) {
                    // false true → true false 이면 → out -1, in +1
                    InOut newInOut = new InOut(true, false, inOutRequestDTO.getIsMember(), post, member);
                    inOutRepository.save(inOut);
                    post.updateOutCount(post.getOutCount()-1);
                    post.updateInCount(post.getInCount()+1);
                }
            }
        }
        return new InOutResponseDTO(post.getInCount(), post.getOutCount());
    }

//    public void saveIn(InOutRequestDTO inOutRequestDTO) throws Exception {
//        System.out.println("saveIn");
//        System.out.println(inOutRequestDTO.getIsMember());
//        Post post = postRepository.getPostByPostId(inOutRequestDTO.getPostId());
//
//        Optional<Member> member;
//        if (inOutRequestDTO.getIsMember()) { // 회원이 누른거면
//            System.out.println("회원임");
//            try {
//                member = memberRepository.findById(inOutRequestDTO.getMemberId());
//            } catch (Exception e) {
//                member = null;
//                throw new MemberException(NOT_FOUND_MEMBER);
//            }
//
//        }else{
//            member = null;
//        }
//
//        InOut inOut = new InOut(true, false, inOutRequestDTO.getIsMember(), post, member.get());
//
//        inOutRepository.save(inOut);
//
//    }
//
//    public void saveOut(InOutRequestDTO inOutRequestDTO) throws Exception {
//        System.out.println("saveOut");
//        System.out.println(inOutRequestDTO.getIsMember());
//        Post post = postRepository.getPostByPostId(inOutRequestDTO.getPostId());
//
//        Optional<Member> member;
//        if (inOutRequestDTO.getIsMember()) { // 회원이 누른거면
//            System.out.println("회원임");
//            try {
//                member = memberRepository.findById(inOutRequestDTO.getMemberId());
//            } catch (Exception e) {
//                member = null;
//                throw new MemberException(NOT_FOUND_MEMBER);
//            }
//
//        }else{
//            member = null;
//        }
//
//        InOut inOut = new InOut(false, true, inOutRequestDTO.getIsMember(), post, member.get());
//
//        inOutRepository.save(inOut);
//
//    }

}
