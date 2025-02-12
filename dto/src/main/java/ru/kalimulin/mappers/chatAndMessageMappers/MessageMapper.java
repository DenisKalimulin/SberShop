package ru.kalimulin.mappers.chatAndMessageMappers;

import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageCreateDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageResponseDTO;
import ru.kalimulin.models.Chat;
import ru.kalimulin.models.Message;
import ru.kalimulin.models.User;

public interface MessageMapper {
    MessageResponseDTO toMessageResponseDTO(Message message);

    Message toMessage(MessageCreateDTO messageCreateDTO, Chat chat, User sender);
}
