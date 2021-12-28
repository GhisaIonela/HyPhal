package com.company.service;

import com.company.domain.Message;
import com.company.domain.User;
import com.company.events.MessageChangeEvent;
import com.company.observer.Observable;
import com.company.observer.Observer;
import com.company.repository.db.MessageDbRepository;
import com.company.repository.db.UserDbRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService {
    private MessageDbRepository messageDbRepository;
    private UserDbRepository userDbRepository;


    public MessageService(MessageDbRepository messageDbRepository, UserDbRepository userDbRepository) {
        this.messageDbRepository = messageDbRepository;
        this.userDbRepository = userDbRepository;
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

//    public Iterable<Message> getSortedMessagesByDateTwoUsers(Long idUser1, Long idUser2){
//        return messageDbRepository.getSortedMessagesByDateTwoUsers(idUser1, idUser2);
//    }

    public Iterable<Message> getSortedMessagesByDateTwoUsers(Long idUser1, Long idUser2) {
            List<Message> messageList = new ArrayList<>();
            User user1 = userDbRepository.findOne(idUser1);
            User user2 = userDbRepository.findOne(idUser2);
            Predicate<Message> predicate = message -> (message.getFrom().equals(user1) && message.getTo().contains(user2)) || (message.getFrom().equals(user2) && message.getTo().contains(user1));

            messageList = StreamSupport.stream(findAll().spliterator(), false).filter(predicate).collect(Collectors.toList());
            //messageList.stream().sorted(Message.dateComparator);
        return messageList;
    }


}


