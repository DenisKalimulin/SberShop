package ru.kalimulin.entity_dto.chatAndMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageCreateDTO {
    private Long chatId;
    private String content;
}
