package inandout.backend.dto.post;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UpdateStuffRequestDTO {

    private Integer postId;
    private String title;
    private String inContent;
    private String outContent;

}
