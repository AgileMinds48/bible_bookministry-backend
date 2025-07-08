package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.serviceInterfaces.BookUserService;

public class UserService<T> implements BookUserService {
    @Override
    public void addItem(Object item) {

    }

    @Override
    public T getItem(Long itemId) {
        return null;
    }

    @Override
    public void removeItem(Long itemId) {

    }
}
