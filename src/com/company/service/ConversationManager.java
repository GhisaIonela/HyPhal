package com.company.service;

import com.company.domain.Message;
import com.company.domain.User;

import java.util.ArrayList;
import java.util.List;

public class ConversationManager {
    private MessageService messageService;
    private User sender;
    private List<User> receivers;
    private List<Message> messageList;

    public ConversationManager(MessageService messageService, User sender, User receiver) {
        this.messageService = messageService;
        this.sender = sender;
        this.receivers = new ArrayList<>();
        this.receivers.add(receiver);
        messageList = null;
    }

    public User getReceiver(){
        return receivers.get(0);
    }

    public User getSender() {
        return sender;
    }

    public void loadMessageList(){
        messageList = new ArrayList<>();
        Iterable<Message> messages = messageService.getSortedMessagesByDateTwoUsers(sender.getId(), getReceiver().getId());
        messages.forEach(message -> messageList.add(message));
    }
    public List<Message> getMessageList(){
        loadMessageList();
        return messageList;
    }
    public void sendMessage(String message){
        messageService.save(sender, receivers, message, null);
    }

    public void replayToMessage(int replayTo, String message){
        try{
            Long idReplayTo = messageList.get(replayTo).getId();
            messageService.save(sender, receivers, message, idReplayTo);
        }catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage() + "There is no message with the given index");
        }
    }

    public Message findMessage(Long id){
        return messageService.findOne(id);
    }

}
