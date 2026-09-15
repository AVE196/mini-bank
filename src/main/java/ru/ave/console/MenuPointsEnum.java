package ru.ave.console;

import java.util.Arrays;

public enum MenuPointsEnum {

    USER_CREATE(1),
    SHOW_ALL_USERS(2),
    ACCOUNT_CREATE(3),
    ACCOUNT_DEPOSIT(4),
    ACCOUNT_WITHDRAW(5),
    ACCOUNT_TRANSFER(6),
    ACCOUNT_CLOSE(7),
    EXIT(0);

    private final int itemNumber;

    MenuPointsEnum(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public static MenuPointsEnum fromNumber(int number) {
        return Arrays.stream(MenuPointsEnum.values())
                .filter(m -> m.getItemNumber() == number)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Отсутствует пункт меню с выбранным номером"));
    }

}
