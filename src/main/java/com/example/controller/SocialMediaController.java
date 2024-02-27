package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.myconfig_test.MyCustomRegisteredAccount;
import com.example.myconfig_test.MyCustomStatus;
import com.example.service.AccountService;
import com.example.service.MessageService;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;
    
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }


    // @PostMapping("/register") // old solution
    // public ResponseEntity<Account> registerAccount(@RequestBody Account account){
    //     try{
    //         Account new_account = accountService.registerAccount(account);
    //         if(new_account != null){
    //             return new ResponseEntity<Account>(new_account, HttpStatus.OK);
    //         }
    //         return new ResponseEntity<Account>(HttpStatus.CONFLICT);
    //     }
    //     catch(Exception ex){
    //         System.out.println(ex.getMessage());
    //         return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
    //     }
    // }


    @PostMapping("/register") // new solution
    public ResponseEntity<Account> registerAccount(@RequestBody Account account){
        try{
            MyCustomRegisteredAccount result = accountService.registerAccount(account);
            switch(result.getStatus()){
                case MyCustomStatus.ALREADY_EXIST:
                    return new ResponseEntity<Account>(HttpStatus.CONFLICT);

                case MyCustomStatus.INVALID_VALUES: // this case is not included in the test cases (password < 4 letters / blank username)
                    return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);

                case MyCustomStatus.SUCCESS:
                    return new ResponseEntity<Account>(result.getAccount(), HttpStatus.OK);
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
    }

    
    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account){
        try{
            Account logged_in_account = accountService.loginAccount(account);
            if(logged_in_account != null){
                return new ResponseEntity<Account>(logged_in_account, HttpStatus.OK);
            }
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/messages")
    public ResponseEntity<Message> addMessage(@RequestBody Message message){
        try{
            if(accountService.isRealUser(message.getPosted_by())){
                Message new_message = messageService.addMessage(message);
                if(new_message != null){
                    return new ResponseEntity<Message>(new_message, HttpStatus.OK);
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        try{
            List<Message> messages = messageService.getAllMessages();
            return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<List<Message>>(HttpStatus.BAD_REQUEST);
    }
    

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int message_id){
        try{
            Message message = messageService.getMessageById(message_id);
            if(message != null){
                return new ResponseEntity<Message>(message, HttpStatus.OK);
            }
            return new ResponseEntity<Message>(HttpStatus.OK);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int message_id){
        try{
            boolean deleted = messageService.deleteMessageById(message_id);
            if(deleted){
                return new ResponseEntity<Integer>(1, HttpStatus.OK); // always 1 (id is unique)
            }
            return new ResponseEntity<Integer>(HttpStatus.OK);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int message_id, @RequestBody Message message){
        try{
            boolean updated = messageService.updateMessageById(message_id, message.getMessage_text());
            if(updated){
                return new ResponseEntity<Integer>(1, HttpStatus.OK); // always 1 (id is unique)
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
    }

    
    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable int account_id){
        try{
            if(accountService.isRealUser(account_id)){
                List<Message> messages = messageService.getMessagesByUser(account_id);
                return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
            }
            return new ResponseEntity<List<Message>>(HttpStatus.NOT_FOUND);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return new ResponseEntity<List<Message>>(HttpStatus.BAD_REQUEST);
        }
    }
}
