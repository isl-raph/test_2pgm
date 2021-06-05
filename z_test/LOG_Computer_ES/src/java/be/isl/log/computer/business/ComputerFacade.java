/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.log.computer.business;

import be.isl.log.computer.entity.Computer;
import be.isl.log.computer.viewmodel.ComputerSearch;
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
public class ComputerFacade extends AbstractFacade<Computer> {

    @PersistenceContext(unitName = "LOG_Computer_ESPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComputerFacade() {
        super(Computer.class);
    }
    
    public List<Computer> find(ComputerSearch c) {
        String jpql =
                " SELECT c "
                + "FROM Computer c ";
        String where = "";

        if (c.getComputerName() != null && c.getComputerName().length() > 0) {
            where = "c.name LIKE :computerName ";
        }
        if (c.getComputerBrand() != null && c.getComputerBrand().length() > 0) {
            if (where.length() > 0) {
                where += " AND ";
            }            
            where += "c.brand.name LIKE :brandName ";
        }        
        if (c.getComputerModel() != null && c.getComputerModel().length() > 0) {
            if (where.length() > 0) {
                where += " AND ";
            }            
            where += "c.model.name LIKE :modelName ";
        } 
        if (c.getComponentModel()!= null && c.getComponentModel().length() > 0) {
            if (where.length() > 0) {
                where += " AND ";
            }
            where += "c IN (SELECT co "
                + "FROM ComputerComponent cc "
                + "INNER JOIN cc.computer co "
                + "INNER JOIN cc.component cmp "
                + "INNER JOIN cmp.model m "
                + "WHERE m.name LIKE :componentModelName) ";
        }
/*        
        if (s.getEmail() != null && s.getEmail().length() > 0) {
            if (where.length() > 0) {
                where += " and ";
            }
            where += "s.email like :email";
        }
        if (s.getDateOfBirth() != null) {
            if (where.length() > 0) {
                where += " and ";
            }
            where += "s.dateOfBirth between :dayBefore AND :dayAfter";
        }
*/
        if (where.length() > 0) {
            jpql += " WHERE " + where;
        }
        jpql += " ORDER BY c.brand.name, c.model.name, c.name";
        Query query = em.createQuery(jpql);
        
        if (c.getComputerName() != null && c.getComputerName().length() > 0) {
            query.setParameter("computerName", "%" + c.getComputerName() + "%");
        }
        if (c.getComputerBrand() != null && c.getComputerBrand().length() > 0) {
            query.setParameter("brandName", "%" + c.getComputerBrand() + "%");            
        }        
        if (c.getComputerModel() != null && c.getComputerModel().length() > 0) {
            query.setParameter("modelName", "%" + c.getComputerModel() + "%");  
        }         
        if (c.getComponentModel()!= null && c.getComponentModel().length() > 0) {
            query.setParameter("componentModelName", "%" + c.getComponentModel() + "%");
        }
        
/*        
        if (s.getFirstName() != null && s.getFirstName().length() > 0) {
            quer.setParameter("firstName", "%" + s.getFirstName() + "%");
        }
        if (s.getEmail() != null && s.getEmail().length() > 0) {
            quer.setParameter("email", "%" + s.getEmail() + "%");
        }
        if (s.getDateOfBirth() != null) {
            Calendar cal = Calendar.getInstance();

            cal.setTime(s.getDateOfBirth());
            cal.add(Calendar.DAY_OF_YEAR, -1);
            quer.setParameter("dayBefore", cal.getTime());

            cal.setTime(s.getDateOfBirth());
            cal.add(Calendar.DAY_OF_YEAR, +1);
            quer.setParameter("dayAfter", cal.getTime());
        }
    */
        return query.getResultList();
    }
}
