package Service;

import java.util.Optional;

import DAO.AccountDao;
import DAO.MessageDao;
import Model.Account;

public class SocialMediaService {
    private AccountDao accountDao;
    private MessageDao messageDao;

    public SocialMediaService(AccountDao accountDao, MessageDao messageDao){
        this.accountDao = accountDao;
        this.messageDao = messageDao;
    }

    public Optional<Account> createAccount(Account user){
        //validate
        if(user.getUsername() == null || user.getPassword() == null || user.getPassword().length() < 4 || user.getUsername().isBlank()){
            return Optional.empty();
        }
        //make username lowercase
        user.setUsername(user.getUsername().toLowerCase());

        //check duplicate by searching for account by username
        Optional<Account> duplicateUser = this.accountDao.getAccountByUsername(user.getUsername());
        if(duplicateUser.isPresent()){
            return Optional.empty();
        }


        //return user account
        return this.accountDao.insertAccount(user);

    }
    public Optional<Account> authenticate(Account user){
        if(user.getUsername() == null || user.getPassword() == null){
            return Optional.empty();
        }

        //make username lowercase
        user.setUsername(user.getUsername().toLowerCase());

        Optional<Account> potentialUserOptional = this.accountDao.getAccountByUsername(user.getUsername());
        if(potentialUserOptional.isEmpty()){
            return Optional.empty();
        }

        Account potentialUser = potentialUserOptional.get();
        //check if provided credentials match potential user
        if(user.getUsername().matches(potentialUser.getUsername()) && user.getPassword().matches(potentialUser.getPassword())){
            return potentialUserOptional;
        }
        return Optional.empty();

    }
}
