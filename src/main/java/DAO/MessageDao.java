package DAO;

import java.util.List;
import java.util.Optional;

import Model.Message;

public interface MessageDao {
    Optional<Message> insertMessage(Message message);
    List<Message> getAllMessages();
    Optional<Message> getMessageById(int messageId);
    int deleteMessageById(int messageId);
    int patchMessageById(int messageId,String revisedMessageText);

}
