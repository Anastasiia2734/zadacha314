package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DTO.UserDto;
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
    public User createUser(UserDto userDto) {
        if (userDao.findUserByName(userDto.getUsername()) != null) {
            throw new EntityNotFoundException("Логин должен быть уникальным");
        }


        Set<Role> roles = findRolesByIds(userDto.getRoleIds());
        if (roles.isEmpty()) {
            throw new EntityNotFoundException("Roles not found");
        }


        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(roles);

        userDao.create(user);
        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long id, UserDto userDto) {
        User user = getUser(id);

        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }


        if (userDto.getUsername() != null && !userDto.getUsername().equals(user.getUsername())
                && userDao.findUserByName(userDto.getUsername()) != null) {
            throw new EntityNotFoundException("Логин должен быть уникальным");
        }


        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }


        Set<Role> roles = findRolesByIds(userDto.getRoleIds());
        user.setRoles(roles);

        userDao.update(user);
        return user;
    }

    private Set<Role> findRolesByIds(List<Long> roleIds) {
        Set<Role> roles = roleDao.findRolesByIds(roleIds);
        if (roles == null || roles.isEmpty()) {
            throw new EntityNotFoundException("Roles not found");
        }
        return roles;
    }

    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }
}
