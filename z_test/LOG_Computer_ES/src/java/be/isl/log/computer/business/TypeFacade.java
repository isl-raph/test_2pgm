/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.log.computer.business;

import be.isl.log.computer.entity.Type;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Eric Schmitz
 */
@Stateless
public class TypeFacade extends AbstractFacade<Type> {

    @PersistenceContext(unitName = "LOG_Computer_ESPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypeFacade() {
        super(Type.class);
    }

    @Override
    public void remove(Type entity) {
        if (entity.getComputers() != null && entity.getComputers().size() > 0) {
            getEntityManager().remove(getEntityManager().merge(entity));
        }
    }

    public List<Type> find(Type t) {
        String jpql
                = " SELECT t "
                + "FROM Type t ";
        String where = "";
        if (t != null) {
            if (t.getName() != null && t.getName().length() > 0) {
                where = "t.name LIKE :name";
            }
        }
        if (where.length() > 0) {
            jpql += " WHERE " + where;
        }
        jpql += " ORDER BY t.name";
        Query query = em.createQuery(jpql);
        if (t != null) {
            if (t.getName() != null && t.getName().length() > 0) {
                query.setParameter("name", "%" + t.getName() + "%");
            }
        }
        return query.getResultList();
    }
}
