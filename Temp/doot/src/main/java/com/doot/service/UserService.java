package com.doot.service;

import com.doot.models.User;

import java.util.Optional;

public interface UserService {
    public User findUserByEmail(String email);
    public Optional<User> findUserByResetToken(String resetToken);
    public void save(User user);
}
