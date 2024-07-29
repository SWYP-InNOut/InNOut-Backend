package inandout.backend.controller.post;

import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.post.InOutRequestDTO;
import inandout.backend.dto.post.InOutResponseDTO;
import inandout.backend.service.post.PostService;
import inandout.backend.service.stuff.StuffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    @Autowired
    public PostService postService;

    @Autowired
    public StuffService stuffService;

    @PostMapping("/inout")
    public BaseResponse<InOutResponseDTO> inOutController(@RequestBody InOutRequestDTO inOutRequestDTO) throws Exception {
        log.info("in/out");

        //inout 테이블에 저장
        InOutResponseDTO inOutResponseDTO = stuffService.saveInOut(inOutRequestDTO);

        return new BaseResponse<>(inOutResponseDTO);
    }

}
