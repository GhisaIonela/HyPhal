package com.company.observer;


import com.company.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}