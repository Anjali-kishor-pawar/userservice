package com.akp.services;

import java.util.List;

import com.akp.entities.User;

public interface UserService {
    public User saveUser(User user);

    public List<User> getAllUsers();
    
    User getUser(String userId);


}
