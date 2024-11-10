package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;
import java.util.Set;


public interface RoleDao {
    Role findRoleByName(String name);

    Set<Role> findRolesByIds(List<Long> roleIds);

    void saveRole(Role role);

    List<Role> getAllRoles();
}
