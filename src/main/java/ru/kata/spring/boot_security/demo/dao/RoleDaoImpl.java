package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Repository
@Transactional
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Role findRoleByName(String roleName) {
        try {
            return entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", roleName)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Log the error and return null or a default role

            return null;  // Return null if the role is not found
        }
    }

    @Override
    public Set<Role> findRolesByIds(List<Long> roleIds) {
        List<Role> query = entityManager.createQuery("SELECT r FROM Role r WHERE r.id IN :roleIds", Role.class)
                        .setParameter("roleIds", roleIds)
        .setParameter("roleIds", roleIds)
                .getResultList();
        return new HashSet<>(query);
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        entityManager.merge(role);
    }

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r", Role.class);
        return query.getResultList();
    }
}
