package com.company.observer;


import com.company.events.Event;

public interface ObservableDb<E extends Event> {
    void addObserver(ObserverDb<E> e);
    void removeObserver(ObserverDb<E> e);
    void notifyObservers(E e);
}
