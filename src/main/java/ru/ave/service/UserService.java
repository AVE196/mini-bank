package ru.ave.service;

import org.springframework.stereotype.Component;
import ru.ave.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserService {

    private final Map<Integer, User> userRepository = new HashMap<>();
    private int lastIndex = 0;

    public User createUser(String login) {
        if (userRepository.values().stream().anyMatch(u -> u.getLogin().equals(login)))
            throw new IllegalArgumentException(String.format("Пользователь с логином %s уже существует", login));
        User user = new User(login);
        user.setId(lastIndex++);
        return user;
    }

    public Map<Integer, User> getUsers() {
        return userRepository;
    }

}
