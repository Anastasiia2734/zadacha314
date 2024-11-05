package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public List<User> getAllUsers();

    public User getUser(Long id);

    User findUserByName(String username);

    public void deleteUser(Long id);

    void createUser(String name, String firstName, String lastName, String email, String password);

    void updateUser(Long id, String name, String firstName, String lastName, String email, String password);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
