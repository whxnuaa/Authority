package com.jit.skiad.serviceinterface;

import com.jit.skiad.domain.User;
import com.jit.skiad.dto.UserDTO;

public interface AuthService {
    User register(User userToAdd);
    UserDTO login(String username, String password);
    String refresh(String oldToken);
}

