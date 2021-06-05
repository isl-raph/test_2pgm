/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.log.computer.business;

import be.isl.log.computer.entity.Component;
import be.isl.log.computer.viewmodel.ComponentSearch;
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
public class ComponentFacade extends AbstractFacade<Component> {

    @PersistenceContext(unitName = "LOG_Computer_ESPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComponentFacade() {
        super(Component.class);
    }

    public List<Component> find(ComponentSearch c) {
        String jpql
                = " SELECT c "
                + "FROM Component c ";
        String where = "";
        if (c != null) {
            if (c.getComponentName() != null && c.getComponentName().length() > 0) {
                where = "c.name LIKE :componentName ";
            }
            if (c.getComponentBrand() != null && c.getComponentBrand().length() > 0) {
                if (where.length() > 0) {
                    where += " AND ";
                }
                where += "c.brand.name LIKE :brandName ";
            }
            if (c.getComponentModel() != null && c.getComponentModel().length() > 0) {
                if (where.length() > 0) {
                    where += " AND ";
                }
                where += "c.model.name LIKE :modelName ";
            }
            if (where.length() > 0) {
                jpql += " WHERE " + where;
            }
        }
        jpql += " ORDER BY c.brand.name, c.model.name, c.name";
        Query query = em.createQuery(jpql);
        if (c != null) {
            if (c.getComponentName() != null && c.getComponentName().length() > 0) {
                query.setParameter("componentName", "%" + c.getComponentName() + "%");
            }
            if (c.getComponentBrand() != null && c.getComponentBrand().length() > 0) {
                query.setParameter("brandName", "%" + c.getComponentBrand() + "%");
            }
            if (c.getComponentModel() != null && c.getComponentModel().length() > 0) {
                query.setParameter("modelName", "%" + c.getComponentModel() + "%");
            }
        }
        return query.getResultList();
    }
}
