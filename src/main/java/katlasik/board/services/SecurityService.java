package katlasik.board.services;

import katlasik.board.model.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public User getLoggedInUser() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByEmail(email).orElseThrow(() -> new AccessDeniedException("You need to be logged in."));
    }

}
