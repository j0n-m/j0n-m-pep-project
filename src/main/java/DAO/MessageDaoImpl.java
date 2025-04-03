package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDaoImpl implements MessageDao{
    public Optional<Message> insertMessage(Message message){
        Connection conn = ConnectionUtil.getConnection();
        String query = "INSERT INTO message VALUES(default, ?,?,?)";

        try {
            PreparedStatement ps = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1,message.getPosted_by());
            ps.setString(2,message.getMessage_text());
            ps.setLong(3,message.getTime_posted_epoch());


            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            

            if(rs.next()){
                int messageId = rs.getInt(1);
                return Optional.of(new Message(messageId,message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch()));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }
    public List<Message> getAllMessages(){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        String query = "SELECT * FROM message";

        try {
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                //(int message_id, int posted_by, String message_text, long time_posted_epoch)
                int messageId = rs.getInt(1);
                int postById = rs.getInt(2);
                String messageText = rs.getString(3);
                long epochTime = rs.getLong(4);

                messages.add(new Message(messageId,postById,messageText,epochTime));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }
    public Optional<Message> getMessageById(int messageId){
        Connection conn = ConnectionUtil.getConnection();
        String query = "SELECT * FROM message WHERE message_id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1,messageId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                //(int message_id, int posted_by, String message_text, long time_posted_epoch)
                int id = rs.getInt(1);
                int postBy = rs.getInt(2);
                String messageText = rs.getString(3);
                long epochTime = rs.getLong(4);

                return Optional.of(new Message(id,postBy,messageText,epochTime));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public int deleteMessageById(int messageId){
        Connection conn = ConnectionUtil.getConnection();
        String query = "DELETE FROM message WHERE message_id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1,messageId);

            int numOfRowsAffected = ps.executeUpdate();
            return numOfRowsAffected;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;

    }

    public int patchMessageById(int messageId,String revisedMessageText){
        Connection conn = ConnectionUtil.getConnection();
        String query = "UPDATE message SET message_text = ? WHERE message_id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,revisedMessageText);
            ps.setInt(2,messageId);

            int numOfRowsAffected = ps.executeUpdate();
            return numOfRowsAffected;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public List<Message> getAllMessagesByUser(int accountId){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messagesByUser = new ArrayList<>();

        String query = "SELECT * FROM message WHERE posted_by = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1,accountId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int messageId = rs.getInt(1);
                int postedBy = rs.getInt(2);
                String messageText = rs.getString(3);
                long epochTime = rs.getLong(4);

                messagesByUser.add(new Message(messageId,postedBy,messageText,epochTime));
            }

            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messagesByUser;
    }
}
