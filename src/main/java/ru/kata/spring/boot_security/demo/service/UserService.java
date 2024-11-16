package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    public List<User> getAllUsers();

    public User getUser(Long id);

    User findUserByName(String username);

    public void deleteUser(Long id);

    User createUser(String name, String firstName, String lastName, String email, String password, Set<Role> roles);

    User updateUser(Long id, String name, String firstName, String lastName, String email, String password, Set<Role> roles);

}
