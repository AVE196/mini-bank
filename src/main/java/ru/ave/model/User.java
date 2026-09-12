package ru.ave.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private int id;
    private String login;
    private List<Account> accountList;

    public User(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<Account> getAccountList() {
        if (accountList == null) accountList = new ArrayList<>();
        return accountList;
    }
}
