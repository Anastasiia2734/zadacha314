package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DTO.UserDto;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;


public interface UserService {
    public List<User> getAllUsers();

    public User getUser(Long id);

    User findUserByName(String username);

    public void deleteUser(Long id);


    @Transactional
    User createUser(UserDto userDto);

    @Transactional
    User updateUser(Long id, UserDto userDto);
}
