package ru.kalimulin.webSocket_config;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(CustomHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpSession httpSession = ((ServletServerHttpRequest) request).getServletRequest().getSession(false);

        if (httpSession == null) {
            logger.warn("Сессия для подключения к WebSocket не найдена");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        String userEmail = (String) httpSession.getAttribute("userEmail");
        if (userEmail == null) {
            logger.warn("Пользователь не аутентифицирован: нет userEmail в сессии");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        logger.info("Пользователь {} подключился к WebSocket", userEmail);
        attributes.put("userEmail", userEmail);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Ничего не делаем после установления соединения
    }
}


