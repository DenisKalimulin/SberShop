package ru.kalimulin.mappers.chatAndMessageMappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageCreateDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageResponseDTO;
import ru.kalimulin.models.Chat;
import ru.kalimulin.models.Message;
import ru.kalimulin.models.User;

@Component
public class MessageMapperImpl implements MessageMapper {
    private static final Logger logger = LoggerFactory.getLogger(MessageMapperImpl.class);


    @Override
    public MessageResponseDTO toMessageResponseDTO(Message message) {
        if (message == null) {
            return null;
        }
        return MessageResponseDTO.builder()
                .messageId(message.getId())
                .chatId(message.getChat().getId())
                .senderEmail(message.getSender().getEmail())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }


    @Override
    public Message toMessage(MessageCreateDTO messageCreateDTO, Chat chat, User sender) {
        if (messageCreateDTO == null || chat == null || sender == null) {
            return null;
        }

        logger.info("Маппинг MessageCreateDTO в Message: {}", messageCreateDTO);

        return Message.builder()
                .chat(chat)
                .sender(sender)
                .content(messageCreateDTO.getContent())
                .build();
    }
}

