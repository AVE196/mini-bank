package ru.ave.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ave.service.AccountService;
import ru.ave.service.UserService;

import java.util.Scanner;

@Component
public class ConsoleListener {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public ConsoleListener(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

}
