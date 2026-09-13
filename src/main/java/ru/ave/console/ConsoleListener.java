package ru.ave.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ave.model.Account;
import ru.ave.model.User;
import ru.ave.service.AccountService;
import ru.ave.service.UserService;

import java.util.Scanner;

@Component
public class ConsoleListener {

    private final UserService userService;
    private final AccountService accountService;
    private final Scanner scanner;

    private final String menu = """
        Введите номер команды из меню. Доступные команды:\s
        1. USER_CREATE,\s
        2. SHOW_ALL_USERS,\s
        3. ACCOUNT_CREATE,
        4. ACCOUNT_DEPOSIT,\s
        5. ACCOUNT_WITHDRAW,\s
        6. ACCOUNT_TRANSFER,\s
        7. ACCOUNT_CLOSE,\s
        0. EXIT
        """;
    private final String error = "Некорректный ввод. ";

    @Autowired
    public ConsoleListener(UserService userService, AccountService accountService, Scanner scanner) {
        this.userService = userService;
        this.accountService = accountService;
        this.scanner = scanner;
    }

    public void start() {
        boolean isNotExit = true;
        while (isNotExit) {
            System.out.println(menu);
            MenuPointsEnum pointMenu = MenuPointsEnum.fromNumber(getNotNegativeNumber(scanner.nextLine()));
            try {
                switch (pointMenu) {
                    case USER_CREATE:
                        // USER_CREATE
                        System.out.println("Введите логин:");
                        User user = userService.createUser(scanner.nextLine());
                        user.getAccountList().add(accountService.createAccount(user.getId()));
                        System.out.println("Добавлен новый пользователь: " + user);
                        break;
                    case SHOW_ALL_USERS:
                        // SHOW_ALL_USERS
                        System.out.println("Список пользователей:");
                        System.out.println(userService.getUsers());
                        break;
                    case ACCOUNT_CREATE:
                        // ACCOUNT_CREATE
                        System.out.println("Введите id пользователя для добавления нового счета:");
                        int userId = getNotNegativeNumber(scanner.nextLine());
                        if (!userService.getUsers().containsKey(userId))
                            throw new IllegalArgumentException("Не найден пользователь с id: " + userId);
                        Account account = accountService.createAccount(userId);
                        userService.getUsers().get(account.getUserId()).getAccountList().add(account);
                        System.out.println("Добавлен новый аккаунт: " + account);
                        break;
                    case ACCOUNT_DEPOSIT:
                        // ACCOUNT_DEPOSIT
                        System.out.println("Введите id-номер счета: ");
                        int idDeposit = getNotNegativeNumber(scanner.nextLine());
                        System.out.println("Введите сумму пополнения: ");
                        int amountDeposit = getPositiveNumber(scanner.nextLine());
                        Account accountDeposit = accountService.depositAmount(idDeposit, amountDeposit);
                        System.out.println("Внесены средства на счет. Счет: " + accountDeposit);
                        break;
                    case ACCOUNT_WITHDRAW:
                        // ACCOUNT_WITHDRAW
                        System.out.println("Введите id-номер счета: ");
                        int idWithdraw = getNotNegativeNumber(scanner.nextLine());
                        System.out.println("Введите сумму списания: ");
                        int amountWithdraw = getPositiveNumber(scanner.nextLine());
                        Account accountWithdraw = accountService.withdrawAmount(idWithdraw, amountWithdraw);
                        System.out.println("списаны средства со счета. Счет: " + accountWithdraw);
                        break;
                    case ACCOUNT_TRANSFER:
                        // ACCOUNT_TRANSFER
                        System.out.println("Введите id-номер счета отправителя: ");
                        int idSender = getNotNegativeNumber(scanner.nextLine());
                        System.out.println("Введите id-номер счета получателя: ");
                        int idRecipient = getNotNegativeNumber(scanner.nextLine());
                        System.out.println("Введите сумму перевода: ");
                        int amountTransfer = getPositiveNumber(scanner.nextLine());
                        int amountAfterCommission = accountService.transferAmount(idSender, idRecipient, amountTransfer);

                        System.out.println(String.format("Выполнен перевод со счета %d на счет %d. Сумма: %d",
                                idSender, idRecipient, amountTransfer) +
                                (amountAfterCommission == -1 ? " (комиссия отсутствует, один пользователь)" :
                                        String.format(", комиссия: %d, на счет получателя зачислено: %d",
                                                amountTransfer - amountAfterCommission, amountAfterCommission )));
                        break;
                    case ACCOUNT_CLOSE:
                        // ACCOUNT_CLOSE
                        System.out.println("Введите id-номер счета для закрытия: ");
                        int idClosed = getNotNegativeNumber(scanner.nextLine());
                        Account[] accountTransferring = accountService.closeAccount(idClosed);
                        userService.getUsers().get(accountTransferring[0].getUserId()).getAccountList().remove(accountTransferring[0]);
                        System.out.printf("Аккаунт %d закрыт. Оставшийся баланс %d перенесен на счет %d%n",
                                accountTransferring[0].getId(), accountTransferring[0].getMoneyAmount(), accountTransferring[1].getId());
                        break;
                    case EXIT:
                        // EXIT
                        isNotExit = false;
                        break;
                    default:
                        System.out.println(error);
                        break;
                }
            } catch (RuntimeException e) {
                System.out.println(error + e.getMessage());
            }
        }
    }

    private int getNotNegativeNumber(String str) {
        try {
            int number = Integer.parseInt(str);
            if (number < 0) throw new IllegalArgumentException("Введено отрицательное число");
            else return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Введено не число: " + str);
        }
    }

    private int getPositiveNumber(String str) {
        try {
            int number = Integer.parseInt(str);
            if (number <= 0) throw new IllegalArgumentException("Число должно быть больше ноля");
            else return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Введено не число: " + str);
        }
    }

}
