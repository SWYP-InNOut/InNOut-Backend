package inandout.backend.controller.post;

import inandout.backend.argumentresolver.MemberId;
import inandout.backend.common.exception.BaseException;
import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.myroom.MyRoomAddStuffRequestDTO;
import inandout.backend.dto.post.InOutRequestDTO;
import inandout.backend.dto.post.InOutResponseDTO;
import inandout.backend.dto.post.UpdateStuffRequestDTO;
import inandout.backend.entity.post.Post;
import inandout.backend.entity.post.PostImage;
import inandout.backend.repository.post.PostJPARepository;
import inandout.backend.service.myroom.S3Service;
import inandout.backend.service.post.PostService;
import inandout.backend.service.stuff.StuffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    @Autowired
    public PostService postService;

    @Autowired
    public StuffService stuffService;

    @Autowired
    public PostJPARepository postJPARepository;

    @Autowired
    public S3Service s3Service;

    @PostMapping("/inout")
    public BaseResponse<InOutResponseDTO> inOutController(@MemberId Integer memberId, @RequestBody InOutRequestDTO inOutRequestDTO) throws Exception {
        log.info("in/out");

        //inout 테이블에 저장
        InOutResponseDTO inOutResponseDTO = stuffService.saveInOut(memberId, inOutRequestDTO);

        return new BaseResponse<>(inOutResponseDTO);
    }

    @PostMapping("/myroom/updatestuff")
    public BaseResponse<String> updateStuffController(@RequestPart(value = "request") UpdateStuffRequestDTO updateStuffRequestDTO,
                                                      @RequestPart(value = "file") List<MultipartFile> multipartFile){
        System.out.println("PostController/updateStuffController");

        //게시물 찾기
        Optional<Post> post = postJPARepository.findById(updateStuffRequestDTO.getPostId());
        if(!post.isPresent()){
            throw new BaseException(BAD_REQUEST); // 게시물 없음
        }

        //이미지 빼고 수정
        postService.updatePost(post.get(), updateStuffRequestDTO);

        //S3, DB에서 기존 이미지 삭제
        postService.deleteImage(post.get());

        //S3에 새로운 이미지 삽입, urls 받아오기 & DB에 저장
        List<String> imageUrls = s3Service.uploadFile(multipartFile, String.valueOf(post.get().getId()));
        postService.updatePostImages(post.get(), imageUrls);


        return new BaseResponse<>("success");
    }

}
