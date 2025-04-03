package DAO;

import java.util.Optional;

import Model.Account;

public interface AccountDao {

    Optional<Account> insertAccount(Account user);
    Optional<Account> getAccountByUsername(String username);
    Optional<Account> getAccountById(int userId);

}
