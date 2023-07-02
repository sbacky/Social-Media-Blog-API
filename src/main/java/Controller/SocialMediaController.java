package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::postAccountHandler);
        app.post("login", this::loginAccountHandler);
        app.post("messages", this::postMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIDHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.patch("messages/{message_id}", this::patchMessageHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesForUserHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    // Account Handlers
    
    /**
     * The request body will contain a JSON representation of an Account,
     * not containing an account_id.<br><br>
     * The login is successful if username and password provided in
     * request body JSON match existing account. Response body contains
     * JSON of account, including its account_id. The response status
     * should be 200 OK, which is the default.<br><br>
     * If the login is not successful, the response status should be 401.
     * (Unauthorized)
     * @param ctx
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void loginAccountHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.loginAccount(account);
        if (loggedInAccount != null) {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
        } else {
            ctx.status(401);
        }
    }

    /**
     * The body will contain a representation of a JSON Account, but
     * will not contain an account_id.<br><br>
     * The registration is successful if username is not blank,
     * password at least 4 characters long, and Account with that
     * username does not already exist. Response body contains JSON of
     * Account, including its account_id. Response status should be 200 OK,
     * which is the default. New account should persist in database.<br><br>
     * If the registration unsuccessful, response status should be 400.
     * (Client error)
     * @param ctx
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void postAccountHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.createAccount(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }
    }

    // Message Handlers

    /**
     * Response body should contain JSON representation of a list
     * containing all messages retrieved from the database. Response body
     * should be empty if there are no messages. The response status
     * should always be 200, which is the default.
     * @param ctx
     */
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    /**
     * Response body should contain JSON representation of a list
     * containing all messages posted by a particular user, which is
     * retrieved from the database. It is expected for the list to be
     * empty if there are no messages. Response status should always be
     * 200, which is the default.
     * @param ctx
     */
    private void getMessagesForUserHandler(Context ctx) {
        int accountID = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesForUser(accountID);
        ctx.json(messages);
    }

    /**
     * Response body should contain JSON representation of the
     * message identified by the message_id. Response body should be
     * empty if there is no such message. The response status should
     * always be 200, which is the default.
     * @param ctx
     */
    private void getMessageByIDHandler(Context ctx) {
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByID(messageID);
        ctx.json(message);
    }

    /**
     * The request body will contain a JSON representation of a message,
     * which should be persisted to the database, but will not contain a
     * message_id.<br><br>
     * The creation of the message will be successful if message_text is
     * not blank, is under 255 characters, and posted_by refers to a real,
     * existing user. Response body should contain JSON of message,
     * including message_id. The response status should be 200, which is
     * the default. New message persist in database.<br><br>
     * If the creation of the message unsuccessful, response status
     * should be 400. (Client error)
     * @param ctx
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void postMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.createMessage(message);
        if (addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
        } else {
            ctx.status(400);
        }
    }

    /**
     * Request body should contain new message_text values to replace
     * the message identified by message_id. The request body can not be
     * guaranteed to contain any other information. <br><br>
     * Update message should be successful if message id already exists
     * and new message_text is not blank and is not over 255 characters.
     * If update is successful, response body should contain full updated
     * message (including message_id, posted_by, message_text, and
     * time_posted_epoch), and response status should be 200, which is
     * the default. Message existing on database should have updated
     * message_text. <br><br>
     * If update message unsuccessful for any reason, response status
     * should be 400. (Client error)
     * @param ctx
     * @throws JsonProcessingException
     */
    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        String messageText = ctx.body();

        Message message = new Message();
        message.setMessage_id(messageID);
        message.setMessage_text(messageText);

        Message updatedMessage = messageService.updateMessage(message);
        if (updatedMessage != null) {
            ObjectMapper mapper = new ObjectMapper();
            ctx.json(mapper.writeValueAsString(updatedMessage));
        } else {
            ctx.status(400);
        }
    }

    /**
     * Deleting an existing message should remove existing message from
     * database. If message existed, response body should contain deleted
     * message. Response status should be 200, which is the default.<br><br>
     * If message did not exist, the response status should be 200,
     * but response body should be empty. DELETE is intended to be
     * idempotent, ie, multiple calls to the DELETE endpoint should
     * respond with same type of response.
     * @param ctx
     * @throws JsonProcessingException
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageID);
        ObjectMapper mapper = new ObjectMapper();
        ctx.json(mapper.writeValueAsString(deletedMessage));
    }

}