package com.jit.authority.serviceinterface;

import com.jit.authority.domain.User;

public interface AuthService {
    User register(User userToAdd);
    String login(String username,String password);
    String refresh(String oldToken);
}

