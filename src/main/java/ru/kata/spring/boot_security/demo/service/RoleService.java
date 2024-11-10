package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> findAllRoles();

    Set<Role> findRoleByIds(List<Long> roleIds);

    public void saveRole(Role role);

    Role findRoleByName(String name);
}
