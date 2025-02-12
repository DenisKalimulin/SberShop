package ru.kalimulin.mappers.chatAndMessageMappers;

import org.springframework.stereotype.Component;
import ru.kalimulin.entity_dto.chatAndMessageDTO.ChatCreateDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.ChatResponseDTO;
import ru.kalimulin.models.Chat;
import ru.kalimulin.models.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMapperImpl implements ChatMapper {
    private final MessageMapper messageMapper;

    public ChatMapperImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public ChatResponseDTO toChatResponseDTO(Chat chat) {
        if(chat == null) {
            return null;
        }

        return ChatResponseDTO.builder()
                .chatId(chat.getId())
                .user1Email(chat.getUser1().getEmail())
                .user2Email(chat.getUser2().getEmail())
                .messages(chat.getMessages()
                        .stream()
                        .map(messageMapper::toMessageResponseDTO)
                        .collect(Collectors.toList())
                )
                .build();

    }

    @Override
    public Chat toChat(User user1, User user2, ChatCreateDTO chatCreateDTO) {
        if(user1 == null || user2 == null || chatCreateDTO == null) {
            return null;
        }

        return Chat.builder()
                .user1(user1)
                .user2(user2)
                .messages(List.of())
                .build();
    }
}
