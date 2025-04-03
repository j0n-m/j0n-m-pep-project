package Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import DAO.AccountDao;
import DAO.MessageDao;
import Model.Account;
import Model.Message;

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
    public Optional<Message> createMessage(Message message){
        //validate message text
        if(message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() >= 255){
            return Optional.empty();
        }
        //check if user exists in db
        Optional<Account> possibleUser = this.accountDao.getAccountById(message.getPosted_by());

        //guard check to return early if no valid user found
        if(possibleUser.isEmpty()){
            return Optional.empty();
        }

        //ensure that epochtime is always set if not provided
        long epochTime = message.getTime_posted_epoch() !=0 ? message.getTime_posted_epoch() : Instant.now().toEpochMilli();
        message.setTime_posted_epoch(epochTime);

        return this.messageDao.insertMessage(message);

    }
    public List<Message> getAllMessages(){
        return this.messageDao.getAllMessages();
    }
}
