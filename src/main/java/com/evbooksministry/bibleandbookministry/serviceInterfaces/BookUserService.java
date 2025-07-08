package com.evbooksministry.bibleandbookministry.serviceInterfaces;

public interface BookUserService <T>{
    void addItem(T item);
    T getItem(Long itemId);
    void removeItem(Long itemId);

}
