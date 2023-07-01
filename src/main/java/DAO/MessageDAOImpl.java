package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAOImpl implements MessageDAO {

    private Connection conn = null;
    private PreparedStatement ps = null;

    @Override
    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<>();

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message";
            ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int messageID = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");

                Message message = new Message(messageID, postedBy, messageText, timePostedEpoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return messages;
    }

    @Override
    public List<Message> getMessagesForUser(int id) {
        List<Message> messages = new ArrayList<>();

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int messageID = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");

                Message message = new Message(messageID, postedBy, messageText, timePostedEpoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return messages;
    }

    @Override
    public Message getMessageByID(int id) {
        Message message = null;

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int messageID = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");

                message = new Message(messageID, postedBy, messageText, timePostedEpoch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return message;
    }

    @Override
    public Message addMessage(Message message) {
        int postedBy = message.getPosted_by();
        String messageText = message.getMessage_text();
        long timePostedEpoch = message.getTime_posted_epoch();

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, postedBy);
            ps.setString(2, messageText);
            ps.setLong(3, timePostedEpoch);

            if (ps.executeUpdate() != 0) {
                ResultSet keys = ps.getGeneratedKeys();
                while (keys.next()) {
                    int messageID = keys.getInt("message_id");
                    message.setMessage_id(messageID);
                    return message;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    @Override
    public Message updateMessage(Message message) {
        int messageID = message.getMessage_id();
        // int postedBy = message.getPosted_by();
        String messageText = message.getMessage_text();
        // long timePostedEpoch = message.getTime_posted_epoch();

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, messageText);
            ps.setInt(2, messageID);

            if (ps.executeUpdate() != 0) {
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    @Override
    public boolean deleteMessage(int id) {
        try {
            conn = ConnectionUtil.getConnection();
            String sql = "DELETE FROM message WHERE message_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            if (ps.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    private void closeResources() {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
