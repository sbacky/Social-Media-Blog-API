package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAOImpl implements AccountDAO {

    private Connection conn;
    private PreparedStatement ps;

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account";
            ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int accountID = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                Account account = new Account(accountID, username, password);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return accounts;
    }

    @Override
    public Account getAccountByID(int id) {
        Account account = null;

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE account_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int accountID = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                account = new Account(accountID, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return account;
    }

    @Override
    public Account addAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);


            if (ps.executeUpdate() != 0) {
                ResultSet keys = ps.getGeneratedKeys();
                while (keys.next()) {
                    int accountID = keys.getInt("account_id");
                    account.setAccount_id(accountID);
                    return account;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    private void closeResources() {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
