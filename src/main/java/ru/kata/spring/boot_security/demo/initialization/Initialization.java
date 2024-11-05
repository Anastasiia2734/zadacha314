package ru.kata.spring.boot_security.demo.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;

@Component
public class Initialization {
    private final UserService userService;
    private final RoleDao roleDao;

    @Autowired
    public Initialization(UserService userService, RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
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
            userService.createUser("admin", "Евгений", "Кузнецов", "kuzya92admin.@mail.ru", "admin");
        }
        if (userService.findUserByName("user") == null) {
            userService.createUser("user", "Илья", "Набоков", "i.nabokovuser@mail.ru", "user");
        }
    }
}



