package DAO;

import java.util.List;

import Model.Message;

public interface MessageDAO {

    /**
     * Get list of all posted messages
     * @return list of posted messages, empty list if no messages
     */
    List<Message> getMessages();

    /**
     * Get list of messages posted by user at ID
     * @param id
     * @return list of messages posted by user at id, empty list of no messages found
     */
    List<Message> getMessagesForUser(int id);

    /**
     * Get posted message at ID
     * @param id
     * @return posted message at id, null if no message found
     */
    Message getMessageByID(int id);

    /**
     * Post a new message
     * @param message
     * @return new posted message, null if message didnt post
     */
    Message addMessage(Message message);

    /**
     * Update message
     * @param message
     * @return updated message, null if update failed
     */
    Message updateMessage(Message message);

    /**
     * Delete message at ID
     * @param id
     * @return true if message was successfully deleted, false otherwise
     */
    boolean deleteMessage(int id);
    
}
