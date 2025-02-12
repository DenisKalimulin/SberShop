package ru.kalimulin.mappers.chatAndMessageMappers;

import ru.kalimulin.entity_dto.chatAndMessageDTO.ChatCreateDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.ChatResponseDTO;
import ru.kalimulin.models.Chat;
import ru.kalimulin.models.User;

public interface ChatMapper {
    ChatResponseDTO toChatResponseDTO(Chat chat);

    Chat toChat(User sender, User recipient, ChatCreateDTO chatCreateDTO);

}
