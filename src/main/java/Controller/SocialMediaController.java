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
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // ACCOUNTS
        app.get("accounts", this::getAllAccountsHandler);
        app.post("register", this::registrationHandler);
        app.post("login", this::loginHandler);

        // MESSAGES
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);
        app.post("messages", this::createNewMessageHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageByIdHandler);

        return app;
    }

    /*************************** ACCOUNTS ********************************/

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    // private void exampleHandler(Context context) {
    // context.json("sample text");
    // }

    private void getAllAccountsHandler(Context context) {
        context.json(accountService.getAllAccounts());
    }

    private void registrationHandler(Context context) throws JsonMappingException, JsonProcessingException {
        // create mapper and convert request body into Account obj
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        // perform registration operation and store result
        Account result = accountService.register(account);

        // if obj returned is null, set ctx status to 400 (client error)
        if (result == null) {
            context.status(400);
        }
        // else, return the result
        else {
            context.status(200);
            context.json(result);
        }
    }

    private void loginHandler(Context context) throws JsonMappingException, JsonProcessingException {
        // create mapper and convert request body into Account obj
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        // perform login operation and store result
        Account result = accountService.login(account);

        // if obj returned is null, set ctx status to 401 (unauthorized)
        if (result == null) {
            context.status(401);
        }
        // else return the result
        else {
            context.status(200);
            context.json(result);
        }

    }

    /*************************** MESSAGES ********************************/

    private void getAllMessagesHandler(Context context) throws JsonMappingException, JsonProcessingException {
        // perform operation to retrieve message list
        List<Message> result = messageService.getAllMessages();

        // 200 status default
        context.status(200);

        context.json(result);

    }

    private void getMessageByIdHandler(Context context) throws JsonMappingException, JsonProcessingException {
        // grab id from path param and store in int id
        int id = Integer.parseInt(context.pathParam("message_id"));

        // perform operation to retrieve message by a given ID
        Message result = messageService.getMessageById(id);

        // 200 status default
        context.status(200);

        // if result is NOT null, response should have the message result
        if (result != null) {
            context.json(result);
        }

    }

    private void getAllMessagesByAccountIdHandler(Context context) {
        // grab id from path param and store in int id
        int id = Integer.parseInt(context.pathParam("account_id"));

        // perform operation to retrieve message list
        List<Message> result = messageService.getAllMessagesByAccountId(id);

        // 200 status default
        context.status(200);

        // if result list has NO elements, response should be empty
        if (result != null) {
            context.json(result);
        }

    }

    private void deleteMessageByIdHandler(Context context) {

        // grab id from path param and store in int id
        int id = Integer.parseInt(context.pathParam("message_id"));

        // perform operation to delete message by a given ID
        Message result = messageService.deleteMessageById(id);

        // 200 status default
        context.status(200);

        // if result is NOT null, response should have the message result
        if (result != null) {
            context.json(result);
        }
    }

    private void updateMessageByIdHandler(Context context) throws JsonMappingException, JsonProcessingException {
        // grab id from path param and store in int id
        int id = Integer.parseInt(context.pathParam("message_id"));

        // create mapper and convert request body into Message obj
        ObjectMapper mapper = new ObjectMapper();
        Message m = mapper.readValue(context.body(), Message.class);

        //assign id from path variable to the created message obj
        m.setMessage_id(id);

        // perform operation to delete message by a given ID
        Message result = messageService.updateMessageById(m);

        // if result is NOT null, response should have the message result
        if (result != null) {
            context.json(result);
            context.status(200);
        } else {
            context.status(400);
        }

    }

    private void createNewMessageHandler(Context context) throws JsonMappingException, JsonProcessingException {
        // create mapper and convert request body into Message obj
        ObjectMapper mapper = new ObjectMapper();
        Message m = mapper.readValue(context.body(), Message.class);

        // perform operation to delete message by a given ID
        Message result = messageService.createNewMessage(m);

        // if result is NOT null, response should have the message result
        if (result != null) {
            context.json(result);
            context.status(200);
        } else {
            context.status(400);
        }
    }

}