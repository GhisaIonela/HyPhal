package com.company.events;


public class RequestChangeEvent implements Event{
    private ChangeEventType type;

    public RequestChangeEvent(ChangeEventType type) {
        this.type = type;

    }

    public ChangeEventType getType() {
        return type;
    }

}
