package DAO;

import java.util.Optional;

import Model.Message;

public interface MessageDao {
    Optional<Message> insertMessage(Message message);

}
