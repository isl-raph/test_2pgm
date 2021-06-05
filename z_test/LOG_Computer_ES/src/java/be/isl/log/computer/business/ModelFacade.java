/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.log.computer.business;

import be.isl.log.computer.entity.Brand;
import be.isl.log.computer.entity.Model;
import be.isl.log.computer.viewmodel.ModelSearch;
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
public class ModelFacade extends AbstractFacade<Model> {

    @PersistenceContext(unitName = "LOG_Computer_ESPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ModelFacade() {
        super(be.isl.log.computer.entity.Model.class);
    }

    public List<Model> find(ModelSearch m) {
        String jpql
                = "SELECT m "
                + "FROM Model m ";
        String where = "";
        if (m != null) {
            if (m.getModelName() != null && m.getModelName().length() > 0) {
                where = "m.name LIKE :name";
            }
            if (m.getBrandName() != null && m.getBrandName().length() > 0) {
                if (where.length() > 0) {
                    where += " AND ";
                }
                where += "m.brand.name LIKE :brandName ";
            }
            if (m.getDescription() != null && m.getDescription().length() > 0) {
                if (where.length() > 0) {
                    where += " AND ";
                }
                where += "m.description LIKE :description ";
            }
        }
        if (where.length() > 0) {
            jpql += " WHERE " + where;
        }
        jpql += " ORDER BY m.brand.name, m.name";
        Query query = em.createQuery(jpql);
        if (m != null) {
            if (m.getModelName() != null && m.getModelName().length() > 0) {
                query.setParameter("name", "%" + m.getModelName() + "%");
            }
            if (m.getBrandName() != null && m.getBrandName().length() > 0) {
                query.setParameter("brandName", "%" + m.getBrandName() + "%");
            }
            if (m.getDescription() != null && m.getDescription().length() > 0) {
                query.setParameter("description", "%" + m.getDescription() + "%");
            }        }
        return query.getResultList();
    }

    public List<Model> findByBrand(Brand b) {
        String jpql
                = "SELECT m "
                + "FROM Model m "
                + "WHERE m.brand.brandId = :brandId";
        jpql += " ORDER BY m.name";
        Query query = em.createQuery(jpql);

        query.setParameter("brandId", b.getBrandId());

        return query.getResultList();
    }
}
