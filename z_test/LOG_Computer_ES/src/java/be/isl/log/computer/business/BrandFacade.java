/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.log.computer.business;

import be.isl.log.computer.entity.Brand;
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
public class BrandFacade extends AbstractFacade<Brand> {

    @PersistenceContext(unitName = "LOG_Computer_ESPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BrandFacade() {
        super(Brand.class);
    }

    public List<Brand> find(Brand b) {
        String jpql
                = " SELECT b "
                + "FROM Brand b ";
        String where = "";
        if (b != null) {
            if (b.getName() != null && b.getName().length() > 0) {
                where = "b.name LIKE :name";
            }

            if (where.length() > 0) {
                jpql += " WHERE " + where;
            }
        }
        jpql += " ORDER BY b.name";
        Query query = em.createQuery(jpql);
        if (b != null) {
            if (b.getName() != null && b.getName().length() > 0) {
                query.setParameter("name", "%" + b.getName() + "%");
            }
        }
        return query.getResultList();
    }
}
