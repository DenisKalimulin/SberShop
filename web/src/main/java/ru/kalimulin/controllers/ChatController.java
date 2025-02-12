package ru.kalimulin.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kalimulin.entity_dto.chatAndMessageDTO.ChatCreateDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.ChatResponseDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageCreateDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageResponseDTO;
import ru.kalimulin.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/shop/chats")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public ResponseEntity<ChatResponseDTO> createOrGetChat(@RequestBody ChatCreateDTO chatCreateDTO, HttpSession session) {
        ChatResponseDTO chatResponseDTO1 = chatService.findOrCreateChat(session, chatCreateDTO.getParticipantEmail());
        return ResponseEntity.ok(chatResponseDTO1);
    }

    @GetMapping
    public ResponseEntity<List<ChatResponseDTO>> getUserChats(HttpSession session) {
        List<ChatResponseDTO> chats = chatService.getUserChats(session);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageResponseDTO>> getChatMessages(@PathVariable Long chatId, HttpSession session) {
        List<MessageResponseDTO> messages = chatService.getChatMessages(chatId, session);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<String> deleteChat(@PathVariable Long chatId, HttpSession session) {
        chatService.deleteChat(chatId, session);
        return ResponseEntity.ok("Чат успешно удален");
    }

    @PostMapping("/test")
    public ResponseEntity<MessageResponseDTO> testSendMessage() {
        MessageCreateDTO dto = new MessageCreateDTO();
        dto.setChatId(1L);
        dto.setContent("Тестовое сообщение");
        MessageResponseDTO response = chatService.sendMessage(dto, "deniskalimulin12@mail.ru");
        return ResponseEntity.ok(response);
    }

}
