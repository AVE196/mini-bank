package ru.ave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ave.config.AccountProperties;
import ru.ave.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class AccountService {

    private final Map<Integer, Account> accountRepository = new HashMap<>();;

    private int lastIndex = 0;

    private final AccountProperties properties;

    @Autowired
    public AccountService(AccountProperties properties) {
        this.properties = properties;
    }

    public Account createAccount(int userId) {
        Account account = new Account(userId);
        account.setId(lastIndex++);
        account.setMoneyAmount(properties.getDefaultAmount());
        accountRepository.put(account.getId(), account);
        return account;
    }

    public Account depositAmount(int id, int amount) {
        Account account = getAccount(id);
        depositAmountByAccount(account, amount);
        return account;
    }

    public Account withdrawAmount(int id, int amount) {
        Account account = getAccount(id);
        withdrawAmountByAccount(account, amount);
        return account;
    }

    public void transferAmount(int senderAccountId, int recipientAccountId, int amount) {
        Account senderAccount = getAccount(senderAccountId);
        Account recipientAccount = getAccount(recipientAccountId);
        withdrawAmountByAccount(senderAccount, amount);
        if (senderAccount.getUserId() == recipientAccount.getUserId()) {
            depositAmountByAccount(recipientAccount, amount);
        } else {
            depositAmountByAccount(recipientAccount, commissionAmount(amount));
        }
        // ToDo return/logging transfer/commission
    }

    public Account closeAccount(int id) {
        Account closedAccount = getAccount(id);
        Account recipientAccount = accountRepository.values().stream()
                .filter(a -> (a != closedAccount && a.getUserId() == closedAccount.getUserId()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Данный счет единственный у пользователя. Невозможно закрыть счет: " + id));
        depositAmountByAccount(recipientAccount, closedAccount.getMoneyAmount());
        accountRepository.remove(id);
        return closedAccount;
    }

    private void depositAmountByAccount(Account account, int amount) {
        account.setMoneyAmount(account.getMoneyAmount() + amount);
    }

    private void withdrawAmountByAccount(Account account, int amount) {
        if (account.getMoneyAmount() < amount)
            throw new IllegalArgumentException(
                    String.format("Недостаточно средств на счете: %d для снятия суммы: %d. Текущий баланс счета: %d",
                            account.getId(), amount, account.getMoneyAmount()));
        account.setMoneyAmount(account.getMoneyAmount() - amount);
    }

    private int commissionAmount(int amount) {
        return (int) (1 - properties.getTransferCommission()) * amount;
    }

    private Account getAccount(int id) {
        Account account = accountRepository.get(id);
        if (account == null) throw new NoSuchElementException("Отсутствует счет с указанным ID: " + id);
        else return account;
    }

}
