package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.AccountDAOImpl;
import DAO.MessageDAO;
import DAO.MessageDAOImpl;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAOImpl();
        this.accountDAO = new AccountDAOImpl();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = new AccountDAOImpl();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public MessageService(AccountDAO accountDAO) {
        this.messageDAO = new MessageDAOImpl();
        this.accountDAO = accountDAO;
    }

    /**
     * Get list of all messages
     * @return List of messages or empty list if no messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getMessages();
    }

    /**
     * Get list of messages for user at accountID
     * @param accountID
     * @return list of messages for accountID or empty list if no messages
     * for accountID
     */
    public List<Message> getMessagesForUser(int accountID) {
        return messageDAO.getMessagesForUser(accountID);
    }

    /**
     * Get message at messageID
     * @param messageID
     * @return message at messageID or null if no message at messageID found
     */
    public Message getMessageByID(int messageID) {
        return messageDAO.getMessageByID(messageID);
    }

    /**
     * Create new message if postedBy and messageText are valid.<br><br>
     * postBy is valid if it refers to a real, existing user.
     * messageText is valid if it is not blank and less than 255 characters.
     * @param message (postedBy, messageText and timePostedEpoch only!)
     * @return added message with messageID or null if message wasnt added
     */
    public Message createMessage(Message message) {
        int postedBy = message.getPosted_by();
        String messageText = message.getMessage_text();
        // Check if message is blank or longer than 254 characters
        if (messageText.isBlank() || messageText.length() > 254) {
            return null;
        }
        // Check if postedBy refers to a real, existing user
        if (accountDAO.getAccountByID(postedBy) == null) {
            return null;
        }
        // Add and return message with messageID
        return messageDAO.addMessage(message);
    }

    /**
     * Update message if messageID and messageText are valid.<br><br>
     * messageID is valid if it refers to an existing message.
     * messageText is valid if it is not blank and is less than 255 characters.
     * @param message (messageID and messageText only!)
     * @return message with updated messageText or null if message was
     * not updated.
     */
    public Message updateMessage(Message message) {
        int messageID = message.getMessage_id();
        String messageText = message.getMessage_text();
        // Check if messageID refers to an existing message
        if (messageDAO.getMessageByID(messageID) == null) {
            return null;
        }
        // Check if messageText is blank or longer than 254 characters
        if (messageText.isBlank() || messageText.length() > 254) {
            return null;
        }
        return messageDAO.updateMessage(message);
    }

    /**
     * Delete message at messageID if a message exists at messageID.
     * @param messageID
     * @return true if message at messageID is deleted, false otherwise.
     */
    public Message deleteMessage(int messageID) {
        Message checkMessage = messageDAO.getMessageByID(messageID);
        // Check if messageID refers to an existing message
        if (checkMessage == null) {
            return null;
        }
        boolean checkDeletedMessage = messageDAO.deleteMessage(messageID);
        if (checkDeletedMessage) {
            return checkMessage;
        } else {
            return null;
        }
    }

}
