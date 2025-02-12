package ru.kalimulin.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.kalimulin.custum_exceptions.userException.UnauthorizedException;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageCreateDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageResponseDTO;
import ru.kalimulin.service.ChatService;

@Controller
public class ChatWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketController.class);

    @Autowired
    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageCreateDTO messageCreateDTO, SimpMessageHeaderAccessor headerAccessor) {
        logger.info("Получено сообщение message: {}", messageCreateDTO);
        // Достаём email пользователя из WebSocket-сессии
        String userEmail = (String) headerAccessor.getSessionAttributes().get("userEmail");

        if (userEmail == null) {
            throw new UnauthorizedException("Пользователь не аутентифицирован!");
        }

        logger.info("Сообщение отправлено пользователю: {}", userEmail);

        // Отправляем сообщение
        MessageResponseDTO message = chatService.sendMessage(messageCreateDTO, userEmail);

        // Рассылаем сообщение всем участникам чата
        messagingTemplate.convertAndSend("/topic/chat/" + message.getChatId(), message);
        logger.info("Сообщение отправлено в /topic/chat/{}", message.getChatId());

    }
}