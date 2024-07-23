package inandout.backend.controller.myroom;

import inandout.backend.dto.myroom.MyRoomAddStuffRequestDTO;
import inandout.backend.dto.myroom.MyRoomRequestDTO;
import inandout.backend.dto.myroom.MyRoomResponseDTO;
import inandout.backend.service.myroom.MyRoomService;
import inandout.backend.service.myroom.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/myroom")
public class MyRoomController {

    @Autowired
    public MyRoomService myRoomService;

    @Autowired
    public S3Service s3Service;

    @PostMapping("")
    public ResponseEntity<MyRoomResponseDTO> myRoomController(@RequestBody MyRoomRequestDTO myRoomRequestDTO) {

        MyRoomResponseDTO myRoomResponseDTO = myRoomService.getMyRoomInfo(myRoomRequestDTO);

        myRoomService.plusUserCount(myRoomRequestDTO);

        return ResponseEntity.ok(myRoomResponseDTO);
    }

    @PostMapping("/addstuff")
    public ResponseEntity<String> myRoomAddStuffController(@RequestPart(value = "request") MyRoomAddStuffRequestDTO myRoomAddStuffRequestDTO,
                                                           @RequestPart(value = "file") List<MultipartFile> multipartFile) {
        myRoomService.addStuff(myRoomAddStuffRequestDTO, multipartFile);

        return ResponseEntity.ok("addstuff success");
    }


}