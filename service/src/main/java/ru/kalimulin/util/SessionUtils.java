package ru.kalimulin.util;

import jakarta.servlet.http.HttpSession;
import ru.kalimulin.custum_exceptions.userException.UnauthorizedException;

public class SessionUtils {

    public static String getUserEmail(HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail == null) {
            throw new UnauthorizedException("Вы не авторизованы. Войдите в систему!");
        }

        return userEmail;
    }
}
