package com.company.ui;

import com.company.domain.Message;
import com.company.service.ConversationManager;

import java.util.List;
import java.util.Scanner;

public class ConversationUi {
    private ConversationManager conversationManager;

    public ConversationUi(ConversationManager conversationManager) {
        this.conversationManager = conversationManager;
    }

    private void getConversationDetails(){
        System.out.println("----------------");
        System.out.println("Chat with "+ conversationManager.getReceiver().getFirstName() + " " +conversationManager.getReceiver().getLastName());
        System.out.println("----------------");
    }

    private void infoCommands(){
        System.out.println("To exit conversation: exit\n" +
                           "To replay: replay");
    }

    private void displayConversation(){
        List<Message> messageList = conversationManager.getMessageList();
        messageList.forEach(message -> System.out.println(messageList.indexOf(message) + ": " + message + "\n"));
    }

    private String writeMessageOrCommand(){
        System.out.println("Text a message or command:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void runConversation(){
        boolean isRunning = true;
        getConversationDetails();
        displayConversation();
        infoCommands();
        while(isRunning){
            String messageOrCommand = writeMessageOrCommand();
            if(messageOrCommand.equals("exit")){
                isRunning=false;
            }else if(messageOrCommand.equals("replay")){
                replay();
                displayConversation();
                infoCommands();
            }else{
                conversationManager.sendMessage(messageOrCommand);
                displayConversation();
                infoCommands();
            }

        }
    }

    private void replay() {
        System.out.println("Type the message number to replay to\n");
        Scanner scanner = new Scanner(System.in);
        int replayIndex = scanner.nextInt();
        System.out.println("Write the message\n");
        Scanner scanner2 = new Scanner(System.in);
        String message = scanner2.nextLine();
        conversationManager.replayToMessage(replayIndex, message);
    }
}
