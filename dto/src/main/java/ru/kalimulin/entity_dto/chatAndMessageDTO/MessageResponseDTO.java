package ru.kalimulin.entity_dto.chatAndMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDTO {
    private Long messageId;
    private Long chatId;
    private String senderEmail;
    private String content;
    private LocalDateTime timestamp;
}
