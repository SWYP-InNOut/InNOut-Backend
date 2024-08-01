package inandout.backend.dto.post;


import lombok.Getter;

@Getter
public class UpdateStuffRequestDTO {

    private Integer postId;
    private String title;
    private String inContent;
    private String outContent;

}
