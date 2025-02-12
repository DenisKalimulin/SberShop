package ru.kalimulin.service_Impl;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kalimulin.custum_exceptions.chatAndMessageException.ChatNotFoundException;
import ru.kalimulin.custum_exceptions.userException.UnauthorizedException;
import ru.kalimulin.custum_exceptions.userException.UserNotFoundException;
import ru.kalimulin.entity_dto.chatAndMessageDTO.ChatCreateDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.ChatResponseDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageCreateDTO;
import ru.kalimulin.entity_dto.chatAndMessageDTO.MessageResponseDTO;
import ru.kalimulin.mappers.chatAndMessageMappers.ChatMapper;
import ru.kalimulin.mappers.chatAndMessageMappers.MessageMapper;
import ru.kalimulin.models.Chat;
import ru.kalimulin.models.Message;
import ru.kalimulin.models.User;
import ru.kalimulin.repositories.ChatRepository;
import ru.kalimulin.repositories.MessageRepository;
import ru.kalimulin.repositories.UserRepository;
import ru.kalimulin.service.ChatService;
import ru.kalimulin.util.SessionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository, UserRepository userRepository,
                           MessageRepository messageRepository, ChatMapper chatMapper,
                           MessageMapper messageMapper) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatMapper = chatMapper;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Transactional
    @Override
    public ChatResponseDTO findOrCreateChat(HttpSession session, String participantEmail) {
        String senderEmail = SessionUtils.getUserEmail(session);

        User user1 = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + senderEmail + " не найден"));
        User user2 = userRepository.findByEmail(participantEmail)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + participantEmail + " не найден"));

        Optional<Chat> optionalChat = chatRepository.findByUser1AndUser2(user1, user2);

        Chat chat;
        if(optionalChat.isPresent()) {
            chat = optionalChat.get();
        } else {
            ChatCreateDTO chatCreateDTO = ChatCreateDTO.builder()
                    .participantEmail(participantEmail)
                    .build();
            chat = chatMapper.toChat(user1, user2, chatCreateDTO);
            chatRepository.save(chat);
        }
        return chatMapper.toChatResponseDTO(chat);
    }

    @Override
    public List<ChatResponseDTO> getUserChats(HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + userEmail + " не найден"));

        List<Chat> chats = chatRepository.findByUser(user);
        return chats.stream()
                .map(chatMapper::toChatResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MessageResponseDTO sendMessage(MessageCreateDTO messageCreateDTO, String userEmail) {

        logger.info("Началась отправка сообщения: {}", messageCreateDTO);

        Chat chat = chatRepository.findById(messageCreateDTO.getChatId())
                .orElseThrow(() -> new ChatNotFoundException("Чат не найден"));

        User sender = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Отправитель не найден"));

        if (!(chat.getUser1().equals(sender) || chat.getUser2().equals(sender))) {
            throw new UnauthorizedException("Пользователь не участвует в чате");
        }

        Message message = messageMapper.toMessage(messageCreateDTO, chat, sender);
        message.setChat(chat);
        message.setSender(sender);

        chat.getMessages().add(message);
        messageRepository.save(message); // Сохраняем сообщение в базе

        logger.info("Сообщение сохранилось: {}", message);

        return messageMapper.toMessageResponseDTO(message);
    }


    @Override
    public List<MessageResponseDTO> getChatMessages(Long chatId, HttpSession session) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Чат не найден"));

        String currentUserEmail = SessionUtils.getUserEmail(session);

        if (!chat.getUser1().getEmail().equals(currentUserEmail)
                && !chat.getUser2().getEmail().equals(currentUserEmail)) {
            throw new UnauthorizedException("Пользователь не является участником этого чата");
        }

        List<Message> messages = messageRepository.findByChatOrderByTimestamp(chat);

        return messages.stream()
                .map(messageMapper::toMessageResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteChat(Long chatId, HttpSession session) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Чат не найден"));

        String currentUserEmail = SessionUtils.getUserEmail(session);

        if (!chat.getUser1().getEmail().equals(currentUserEmail)
                && !chat.getUser2().getEmail().equals(currentUserEmail)) {
            throw new UnauthorizedException("Нельзя удалять чат, в котором пользователь не участвует");
        }

        chatRepository.delete(chat);
    }
}