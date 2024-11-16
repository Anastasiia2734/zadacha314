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


        /*if (userService.findUserByName("admin") == null) {
            Set<Role> adminRole = new HashSet<>();
            Role userRole = roleDao.findRoleByName("ROLE_USER");
            Role adminRoles = roleDao.findRoleByName("ROLE_ADMIN");
           adminRole.add(userRole);
            adminRole.add(adminRoles);
            userService.createUser("admin", "Евгений", "Кузнецов", "kuzya92admin.@mail.ru", "admin", adminRole);
        }


        if (userService.findUserByName("user") == null) {
            Set<Role> userRoles = new HashSet<>();
            Role userRole = roleDao.findRoleByName("ROLE_USER");
            userRoles.add(userRole);
            userService.createUser("user", "Илья", "Набоков", "i.nabokovuser@mail.ru", "user", userRoles);
        }*/
    }
}