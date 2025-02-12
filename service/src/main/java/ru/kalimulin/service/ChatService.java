package ru.kalimulin.service;

import jakarta.servlet.http.HttpSession;
import ru.kalimulin.entity_dto.chatAndMessageDTO.ChatResponseDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageCreateDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageResponseDTO;

import java.util.List;

public interface ChatService {
    ChatResponseDTO findOrCreateChat(HttpSession session, String participantEmail);

    List<ChatResponseDTO> getUserChats(HttpSession session);

    MessageResponseDTO sendMessage(MessageCreateDTO messageCreateDTO, String userEmail);

    List<MessageResponseDTO> getChatMessages(Long chatId, HttpSession session);

    void deleteChat(Long chatId, HttpSession session);
}

