package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
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

    public SocialMediaController(){
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

        //ACCOUNTS
        app.get("accounts", this::getAllAccountsHandler);
        app.post("register", this::registrationHandler);
        app.post("login", this::loginHandler);

        //MESSAGES
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);
        app.post("messages", this::createNewMessageHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageByIdHandler);
      
        return app;
    }

    //ACCOUNTS

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    private void getAllAccountsHandler(Context context) {
        context.json(accountService.getAllAccounts());
    }

    private void registrationHandler(Context context) throws JsonMappingException, JsonProcessingException {
        //create mapper and convert request body into Account obj
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        //perform registration operation and store result
        Account result = accountService.register(account);
        
        //if obj returned is null, set ctx status to 400 (client error)
        if ( result == null ) {
            context.status(400);
        }
        //else, return the result
        else {
            context.status(200);
            context.json(result);
        }
    }

    private void loginHandler(Context context) throws JsonMappingException, JsonProcessingException {
        //create mapper and convert request body into Account obj
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        //perform login operation and store result
        Account result = accountService.login(account);

        //if obj returned is null, set ctx status to 401 (unauthorized)
        if ( result == null ) {
            context.status(401);
        }
        //else return the result
        else {
            context.status(200);
            context.json(result);
        }
        
    }


    //MESSAGES
    
    private void getAllMessagesHandler(Context context) {
    }
    private void getMessageByIdHandler(Context context) {
    }
    private void getAllMessagesByAccountIdHandler(Context context) {
    }
    private void deleteMessageByIdHandler(Context context) {
    }
    private void updateMessageByIdHandler(Context context) {
    }
    private void createNewMessageHandler(Context context) {
    }
  


}