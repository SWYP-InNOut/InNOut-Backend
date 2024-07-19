package inandout.backend.dto.chat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDTO {

    private Long chatId;
    private Long sender;
    private LocalDateTime createdAt;
    private String content;


}
