package ru.kalimulin.entity_dto.chatAndMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponseDTO {
    private Long chatId;
    private String user1Email;
    private String user2Email;
    private List<MessageResponseDTO> messages;
}
