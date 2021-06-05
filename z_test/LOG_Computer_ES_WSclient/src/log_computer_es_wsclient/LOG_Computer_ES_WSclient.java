/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log_computer_es_wsclient;

import be.isl.log.computer.entity.Brand;
import be.isl.log.computer.entity.Computer;
import java.util.List;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 *
 * @author esc
 */
public class LOG_Computer_ES_WSclient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BrandWSclient wsBrand = new BrandWSclient();

        System.out.println("Nombre de marques : " + wsBrand.countREST());

        GenericType<List<Brand>> gType = new GenericType<List<Brand>>() {
        };
/*
        Response response = wsBrand.findAll_JSON(Response.class);
        List<Brand> brands = response.readEntity(gType);
        System.out.println("Liste des marques reçues");
        System.out.println("------------------------");
        for (Brand c : brands) {
//            if (c.getBrandId() == 1335){
//                c.setName("Sony update from WS");
//                wsBrand.edit_JSON(c, c.getBrandId().toString());
//            }
            System.out.println(c.getName() + " - " + c.getBrandId());
        }
*/
//        Brand nb = new Brand();        
//        nb.setName("Test from WS client");
//        wsBrand.create_XML(nb);
        ComputerWSclient wsComputer = new ComputerWSclient();
        // get the total number of computers
        System.out.println("Nombre d'ordinateurs : " + wsComputer.countREST());

        GenericType<List<Computer>> gTypeComputer = new GenericType<List<Computer>>() {
        };
        // get the full list of computer
        Response response = wsComputer.findAll_JSON(Response.class);
        List<Computer> computers = response.readEntity(gTypeComputer);
        System.out.println("Liste des ordinateurs reçus");
        System.out.println("---------------------------");
        for (Computer c : computers) {
            System.out.println(c.getName() + " - "
                    + c.getBrand() + " - "
                    + c.getModel() + " - "
                    + c.getType());
        }
        
        // get the list of computers corresponding to a brand and/or a model
        String brand;
        String model;
        System.out.print("Entrez la marque : ");
        brand = Clavier.lireString();
        System.out.print("Entrez le modèle : ");
        model = Clavier.lireString();
        while (brand.length() > 0 || model.length() > 0) {
            response = wsComputer.find_JSON(Response.class, brand, model);
            computers = response.readEntity(gTypeComputer);
            System.out.println("Liste des ordinateurs reçus");
            System.out.println("---------------------------");
            for (Computer c : computers) {
                System.out.println(c.getName() + " - "
                        + c.getBrand() + " - "
                        + c.getModel() + " - "
                        + c.getType());
            }

            System.out.print("Entrez la marque : ");
            brand = Clavier.lireString();
            System.out.print("Entrez le modèle : ");
            model = Clavier.lireString();
        }
    }
}
