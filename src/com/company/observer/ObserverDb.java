package com.company.observer;

import com.company.events.Event;

public interface ObserverDb<E extends Event> {
    void updateFromDb(E e);
}
