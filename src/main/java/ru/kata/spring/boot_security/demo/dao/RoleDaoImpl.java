package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
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
    public Role findRoleByName(String name) {
        try {
            return entityManager.createQuery(
                            "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Role with name " + name + " not found");
        }
    }

    @Override
    public Set<Role> findRolesByIds(List<Long> roleIds) {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r WHERE r.id IN :roleIds", Role.class);
        query.setParameter("roleIds", roleIds);
        return new HashSet<>(query.getResultList());
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r", Role.class);
        return query.getResultList();
    }
}
