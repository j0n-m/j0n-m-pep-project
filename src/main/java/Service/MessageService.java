package Service;

import DAO.MessageDao;
import Model.Account;
import Model.Message;
import java.util.*;
import java.time.Instant;

public class MessageService {
    private MessageDao messageDao;

    public MessageService(MessageDao messageDao){
        this.messageDao = messageDao;
        
    }

    public Optional<Message> createMessage(Message message, Account possibleUser){
        if(possibleUser == null){
            return Optional.empty();
        }
        //validate message text
        if(message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255){
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
    public Optional<Message> getMessage(String messageIdParam){
        int messageId;
        try {
            messageId = Integer.parseInt(messageIdParam);
            
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
        return this.messageDao.getMessageById(messageId);
    }

    public Optional<Message> deleteMessage(String messageIdParam){
        int messageId;
        try {
            messageId = Integer.parseInt(messageIdParam);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
        Optional<Message> messageToBeRemoved = this.messageDao.getMessageById(messageId);

        int rowsAffected = this.messageDao.deleteMessageById(messageId);

        if(rowsAffected > 0){
            return messageToBeRemoved;
        }

        return Optional.empty();
    }

    public Optional<Message> patchMessageTextById(String messageIdParam,String revisedMessageText){
        int messageId;
        try {
            messageId = Integer.parseInt(messageIdParam);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        if(revisedMessageText == null || revisedMessageText.isBlank() || revisedMessageText.length() > 255){
            return Optional.empty();
        }
        //query old message to use as return value if a row was affected when patching
        Optional<Message> messageToBeRevised = this.messageDao.getMessageById(messageId);

        int rowsAffected = this.messageDao.patchMessageById(messageId,revisedMessageText);
        
        if(rowsAffected > 0){
            Message oldMessage = messageToBeRevised.get();
            return Optional.of(new Message(oldMessage.getMessage_id(),oldMessage.getPosted_by(),revisedMessageText,oldMessage.getTime_posted_epoch()));
        }

        return Optional.empty();
    }
    public List<Message> getAllMessagesByUser(String accountIdParam){
        int accountId;
        try {
            accountId = Integer.parseInt(accountIdParam);
            
        } catch (NumberFormatException e) {
            return new ArrayList<Message>();
        }
        return this.messageDao.getAllMessagesByUser(accountId);
    }

}
