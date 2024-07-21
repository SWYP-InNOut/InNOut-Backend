package inandout.backend.controller.myroom;

import inandout.backend.dto.myroom.MyRoomRequestDTO;
import inandout.backend.dto.myroom.MyRoomResponseDTO;
import inandout.backend.service.myroom.MyRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/myroom")
public class MyRoomController {

    @Autowired
    public MyRoomService myRoomService;

    @PostMapping("")
    public ResponseEntity<MyRoomResponseDTO> myRoomController(@RequestBody MyRoomRequestDTO myRoomRequestDTO) {

        MyRoomResponseDTO myRoomResponseDTO = myRoomService.getMyRoomInfo(myRoomRequestDTO);

        myRoomService.plusUserCount(myRoomRequestDTO);

        return ResponseEntity.ok(myRoomResponseDTO);
    }


}
