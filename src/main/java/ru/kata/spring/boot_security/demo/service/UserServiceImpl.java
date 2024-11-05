package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.entity.User;

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
        return userDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByName(String username) {
        return userDao.findUserByName(username);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDao.delete(id);
    }


    @Override
    @Transactional
    public void createUser(String name, String firstName, String lastName, String email, String password) {
        User user = new User();
        user.setUsername(name);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if (name.equals("admin")) {
            user.setRoles(Set.of(roleDao.findRoleByName("ROLE_ADMIN")));
        } else {
            user.setRoles(Set.of(roleDao.findRoleByName("ROLE_USER")));
        }
        userDao.create(user);
    }

    @Override
    @Transactional
    public void updateUser(Long id, String name, String firstName, String lastName, String email, String password) {
        User user = getUser(id);
        if (user != null) {
            user.setUsername(name);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);

            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }

            userDao.update(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findUserWithRolesByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}