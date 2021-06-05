/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.log.computer.business;

import be.isl.log.computer.entity.ComputerComponent;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Eric Schmitz
 */
@Stateless
public class ComputerComponentFacade extends AbstractFacade<ComputerComponent> {

    @PersistenceContext(unitName = "LOG_Computer_ESPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComputerComponentFacade() {
        super(ComputerComponent.class);
    }
    
}
