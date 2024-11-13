package ru.kata.spring.boot_security.demo.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Initialization {
    private final UserService userService;
    private final RoleDao roleDao;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public Initialization(UserService userService, RoleDao roleDao, RoleServiceImpl roleServiceImpl) {
        this.userService = userService;
        this.roleDao = roleDao;
        this.roleServiceImpl = roleServiceImpl;
    }

    @PostConstruct
    public void initUsers() {
        if (roleDao.findRoleByName("ROLE_ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleDao.saveRole(adminRole);
        }
        if (roleDao.findRoleByName("ROLE_USER") == null) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleDao.saveRole(userRole);
        }

        if (userService.findUserByName("admin") == null) {
            Role adminRole = roleServiceImpl.findRoleByName("ROLE_ADMIN");
            Set<Role> roles = new HashSet<>();
            userService.createUser("admin", "Евгений", "Кузнецов", "kuzya92admin.@mail.ru", "admin", roles);
        }
        if (userService.findUserByName("user") == null) {
            Role userRole = roleServiceImpl.findRoleByName("ROLE_USER");
            Set<Role> roles = new HashSet<>();
            userService.createUser("user", "Илья", "Набоков", "i.nabokovuser@mail.ru", "user", roles);
        }
    }
}