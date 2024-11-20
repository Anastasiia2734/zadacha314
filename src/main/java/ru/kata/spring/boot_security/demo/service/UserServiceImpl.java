package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUser(Long id) {
        User user = userDao.read(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByName(String username) {
        return userDao.findUserByName(username);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userDao.read(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        userDao.delete(id);
    }


    @Override
    @Transactional
    public User createUser(String name, String firstName, String lastName, String email, String password, Set<Role> roles) {

        if (userDao.findUserByName(name) != null) {
            throw new EntityNotFoundException("Поле username должно бфть уникально");
        }

        User user = new User();
        user.setUsername(name);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        user.setRoles(roles);

        userDao.create(user);
        return user;
    }


    @Override
    @Transactional
    public User updateUser(Long id, String name, String firstName, String lastName, String email, String password, Set<Role> roles) {
        User user = getUser(id);

        if (user != null) {
            if (name != null && !name.equals(user.getUsername()) && userDao.findUserByName(name) != null) {
                throw new EntityNotFoundException("Поле username должно быть");
            }

            user.setUsername(name);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);

            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }

            user.setRoles(roles);
            userDao.update(user);
        }

        return user;
    }
}