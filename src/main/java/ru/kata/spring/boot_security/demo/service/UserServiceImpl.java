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
        User user = userDao.findUserByName(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
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
    public void createUser(String name, String firstName, String lastName, String email, String password, Set<Role> roles) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        User user = new User();
        user.setUsername(name);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        if (name.equals("admin")) {
            user.setRoles(Set.of(roleDao.findRoleByName("ROLE_ADMIN")));
        }
        user.setRoles(roles);
        userDao.create(user);
    }

    @Override
    @Transactional
    public void updateUser(Long id, String name, String firstName, String lastName, String email, String password, Set<Role> roles) {
        User user = getUser(id);
        if (user != null) {
            user.setUsername(name);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            //user.setPassword(password);

            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }
            user.setRoles(roles);
            userDao.update(user);
        }
    }
}