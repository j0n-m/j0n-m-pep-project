package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDaoImpl implements AccountDao{
    public Optional<Account> insertAccount(Account user){
        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account VALUES(default,?,?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,user.getUsername());
            ps.setString(2,user.getPassword());

            int rowsAffected = ps.executeUpdate();

            if(rowsAffected < 1){
                return Optional.empty();
            }
            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()){
                int accountId = rs.getInt(1);
                return Optional.of(new Account(accountId,user.getUsername(),user.getPassword()));
            }

            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Account> getAccountByUsername(String username){
        Connection conn = ConnectionUtil.getConnection();
        String query = "SELECT account_id,username,password FROM account WHERE username = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,username);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                int id = rs.getInt(1);
                String uname = rs.getString("username");
                String pass = rs.getString("password");
                return Optional.of(new Account(id,uname,pass));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }
    public Optional<Account> getAccountById(int userId){
        Connection conn = ConnectionUtil.getConnection();
        String query = "SELECT account_id,username,password FROM account WHERE account_id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1,userId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                int id = rs.getInt(1);
                String uname = rs.getString("username");
                String pass = rs.getString("password");
                return Optional.of(new Account(id,uname,pass));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

}
