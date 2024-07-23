package inandout.backend.controller.others;


import inandout.backend.dto.myroom.PostResponseDTO;
import inandout.backend.dto.others.OthersResponseDTO;
import inandout.backend.service.others.OthersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/others")
public class OthersController {

    @Autowired
    public OthersService othersService;

    @GetMapping("")
    public ResponseEntity<List<OthersResponseDTO>> getPostController() {
        List<OthersResponseDTO> othersResponseDTOList = othersService.getOthersList();
        return ResponseEntity.ok(othersResponseDTOList);
    }

}
