package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.entity.Role;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> findAllRoles() {
        return roleDao.getAllRoles();
    }

    @Override
    public Set<Role> findRoleByIds(List<Long> roleIds) {
        Set<Role> roles = roleDao.findRolesByIds(roleIds);
        if (roles == null) {
            throw new EntityNotFoundException("Roles not found");
        }
        return roles;
    }

    @Override
    public Role findRoleByName(String name) {
        Role role = roleDao.findRoleByName(name);
        if (role == null) {
            throw new EntityNotFoundException("Role not found");
        }
        return role;
    }

    @Override
    public void saveRole(Role role) {
        roleDao.saveRole(role);
    }
}
