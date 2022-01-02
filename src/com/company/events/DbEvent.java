package com.company.events;

public class DbEvent implements Event{
    private ChangeEventType type;

    public DbEvent(ChangeEventType type) {
        this.type = type;

    }

    public ChangeEventType getType() {
        return type;
    }
}
