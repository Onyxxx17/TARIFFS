package com.tariff.service;

import java.util.List;
import java.util.Optional;

import com.tariff.entity.User;

public interface UserService {

    List<User> listUser();

    User getUser(Long id);

    Optional<User> getUserByUsername(String username);

    User addUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
