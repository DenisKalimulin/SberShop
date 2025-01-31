package ru.kalimulin.aspect;

import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kalimulin.annotation.RoleRequired;
import ru.kalimulin.models.User;
import ru.kalimulin.models.Role;
import ru.kalimulin.repositories.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class RoleAspect {
    private HttpSession session;

    private UserRepository userRepository;

    @Autowired
    public RoleAspect(HttpSession session, UserRepository userRepository) {
        this.session = session;
        this.userRepository = userRepository;
    }

    @Before("@annotation(roleRequired)")
    public void checkRole(JoinPoint joinPoint, RoleRequired roleRequired) throws Throwable {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            throw new AccessDeniedException("Неавторизованный доступ");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AccessDeniedException("Пользователь не найден"));

        Set<String> userRoles = user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toSet());


        boolean hasRole = false;
        for (String requiredRole : roleRequired.value()) {
            if (userRoles.contains(requiredRole)) {
                hasRole = true;
                break;
            }
        }

        if (!hasRole) {
            throw new AccessDeniedException("Доступ запрещён");
        }
    }

    public static class AccessDeniedException extends RuntimeException {
        public AccessDeniedException(String message) {
            super(message);
        }
    }
}