package com.company.events;


public class MessageChangeEvent implements Event{
    private ChangeEventType type;

    public MessageChangeEvent(ChangeEventType type) {
        this.type = type;

    }

    public ChangeEventType getType() {
        return type;
    }

}
