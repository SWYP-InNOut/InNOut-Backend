package inandout.backend.dto.myroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyRoomAddStuffRequestDTO {

    private Integer memberId;
    private String title;
  //  private List<String> images;
    private String inContent;
    private String outContent;

}
