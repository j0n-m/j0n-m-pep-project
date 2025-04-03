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

}
