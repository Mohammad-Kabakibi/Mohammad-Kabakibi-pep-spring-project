package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message){
        if(!message.getMessage_text().isBlank() && message.getMessage_text().length() < 256){
            return messageRepository.save(message);
        }
        return null;
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int id){
        if(id >= 0){
            Optional<Message> message = messageRepository.findById(id);
            if(message.isPresent()){
                return message.get();
            }
        }
        return null;
    }


    public boolean deleteMessageById(int id){
        if(id >= 0){
            Optional<Message> message = messageRepository.findById(id);
            if(message.isPresent()){
                messageRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }


    public boolean updateMessageById(int message_id, String new_text){
        if(!new_text.isBlank() && new_text.length() < 256){
            Optional<Message> old_message = messageRepository.findById(message_id);
            if(old_message.isPresent()){
                Message new_message = old_message.get();
                new_message.setMessage_text(new_text);
                // return messageRepository.save(new_message);
                messageRepository.save(new_message);
                return true;
            }
        }
        return false;
    }

    public List<Message> getMessagesByUser(int user_id){
        if(user_id >= 0){
            return messageRepository.findByPosted_by(user_id);
        }
        return null;
    }

}
