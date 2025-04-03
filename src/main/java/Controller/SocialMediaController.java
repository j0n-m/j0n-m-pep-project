package Controller;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDaoImpl;
import DAO.MessageDaoImpl;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private SocialMediaService service;

    public SocialMediaController(){
        this.service = new SocialMediaService(new AccountDaoImpl(), new MessageDaoImpl());
    }
     
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/example", this::exampleHandler);
        app.post("/register",this::registerHandler);
        app.post("/login",this::loginHandler);

        app.post("/messages",this::createMessageHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context ctx) throws JsonProcessingException{
        //convert json to Account class
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(ctx.body(),Account.class);

        //pass user to service class
        Optional<Account> updatedUser = this.service.createAccount(user);

        //determine return based on if data is null or not
        if(updatedUser.isEmpty()){
            ctx.status(400);
        }else{
            ctx.json(updatedUser.get());
        }

    }
    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(ctx.body(),Account.class);

        Optional<Account> authUser = this.service.authenticate(user);
        if(authUser.isEmpty()){
            ctx.status(401);
        }else{
            ctx.json(authUser.get());
        }
    }
    private void createMessageHandler(Context ctx)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message messageReq = mapper.readValue(ctx.body(),Message.class);

        System.out.println("messagereq: " + messageReq);
        Optional<Message> messageRes = this.service.createMessage(messageReq);
        System.out.println("messageres: " + messageRes);
        

        if(messageRes.isEmpty()){
            ctx.status(400);
        }else{
            ctx.json(messageRes.get());
        }
    }


}