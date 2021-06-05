/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.log.computer.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author esc
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(be.isl.log.computer.service.BrandFacadeREST.class);
        resources.add(be.isl.log.computer.service.ComponentFacadeREST.class);
        resources.add(be.isl.log.computer.service.ComputerComponentFacadeREST.class);
        resources.add(be.isl.log.computer.service.ComputerFacadeREST.class);
        resources.add(be.isl.log.computer.service.ModelFacadeREST.class);
        resources.add(be.isl.log.computer.service.TypeFacadeREST.class);
    }
    
}
