package com.company.service;

import com.company.domain.Message;
import com.company.domain.User;
import com.company.repository.db.MessageDbRepository;

import java.util.List;

public class MessageService {
    private MessageDbRepository messageDbRepository;

    public MessageService(MessageDbRepository messageDbRepository) {
        this.messageDbRepository = messageDbRepository;
    }

    public Message findOne(Long id){
        return messageDbRepository.findOne(id);
    }

    public Iterable<Message> findAll(){
        return messageDbRepository.findAll();
    }

    public Message save(User from, List<User> to, String message, Long id_replayed_to){
        Message message2 = new Message(from, to, message);
        if(id_replayed_to!=null){
                message2.setReplay(messageDbRepository.findOne(id_replayed_to));
            }
        return messageDbRepository.save(message2);
    }

    public Iterable<Message> getSortedMessagesByDateTwoUsers(Long idUser1, Long idUser2){
        return messageDbRepository.getSortedMessagesByDateTwoUsers(idUser1, idUser2);
    }
}


