package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.DTO.UserDto;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        Set<Role> roles = roleService.findRoleByIds(userDto.getRoleIds());
        if (roles.isEmpty()) {
            return ResponseEntity.badRequest().build();  // Handle the case where no valid roles were found
        }

        User user = new User(userDto.getUsername(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getPassword(),
                roles);

        User createdUser = userService.createUser(userDto.getUsername(), userDto.getFirstName(), userDto.getLastName(),
                userDto.getEmail(), userDto.getPassword(), roles);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User existingUser = userService.getUser(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        Set<Role> roles = roleService.findRoleByIds(userDto.getRoleIds());
        if (roles.isEmpty()) {
            return ResponseEntity.badRequest().build();  // Handle the case where no valid roles were found
        }

        User updatedUser = userService.updateUser(id,
                userDto.getUsername(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getPassword(),
                roles);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.findAllRoles();
    }
}
