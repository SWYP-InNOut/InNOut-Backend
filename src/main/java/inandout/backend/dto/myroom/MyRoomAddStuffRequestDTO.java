package inandout.backend.dto.myroom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyRoomAddStuffRequestDTO {

    private Integer memberId;
    private String title;
  //  private List<String> images;
    private String inContent;
    private String outContent;

}
