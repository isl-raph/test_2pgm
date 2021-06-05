package be.isl.log.computer.ui;

import be.isl.log.computer.business.ComputerComponentFacade;
import be.isl.log.computer.entity.Computer;
import be.isl.log.computer.ui.util.JsfUtil;
import be.isl.log.computer.business.ComputerFacade;
import be.isl.log.computer.business.ModelFacade;
import be.isl.log.computer.entity.ComputerComponent;
import be.isl.log.computer.viewmodel.ComputerSearch;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

@Named("computerCtrl")
@SessionScoped
public class ComputerController implements Serializable {

    private Computer current;
    private ComputerSearch search;
    private DataModel items = null;
    private SelectItem[] modelItems = null;
    @Inject
    private ComputerFacade ejbFacade;
    @Inject
    ComputerComponentFacade computerComponentFacade;
    @Inject
    ModelFacade modelFacade;

    public ComputerController() {
    }

    @PostConstruct
    public void init() {
        search = new ComputerSearch();
        items = new ListDataModel(ejbFacade.findAll());
        current = (Computer) items.getRowData();
        modelItemsRefresh();
    }
    // getter and setter
    public Computer getCurrent() {
        return current;
    }

    public void setCurrent(Computer current) {
        this.current = current;
    }

    private ComputerFacade getFacade() {
        return ejbFacade;
    }

    public SelectItem[] getModelItems() {
        return modelItems;
    }

    public DataModel getItems() {
        return items;
    }

    public ComputerSearch getSearch() {
        return search;
    }

    public void setSearch(ComputerSearch search) {
        this.search = search;
    }
    // action methods
    public String doComputerComponentNew() {
        ComputerComponent cc = new ComputerComponent(); // création d'un nouveau ComputerComponent
        cc.setComputer(current); // ne pas oublier d'initialiser l'attribut computer du Computercomponent créé
        if (current.getComputerComponents() == null) { // on vérifie si le Computer courant a déjà une liste de ComputerComponent
            Collection<ComputerComponent> ccs = new ArrayList<>(); //Si non, on la crée
            current.setComputerComponents(ccs); // et on l'affecte à Computer courant.
        }
        current.getComputerComponents().add(cc); // Enfin on ajoute le ComputerComponent nouvellement créé à la liste
        return "Computers.xhtml";
    }

    public String doComputerComponentRemove(ComputerComponent cc) {
        current.getComputerComponents().remove(cc);
        ejbFacade.edit(current);
        computerComponentFacade.remove(cc);
        return "Computers.xhtml";
    }

    public String doDelete() {
        ejbFacade.remove(current);
        items = new ListDataModel(ejbFacade.find(search));
        if (items != null && items.getRowCount() > 0) {
            items.setRowIndex(0);
            current = (Computer) getItems().getRowData();
        } else {
            current = new Computer();
        }
        return "Computers.xhtml";
    }

    public String doNew() {
        try {
            current = new Computer();
            return "Computers.xhtml";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Problème lors de la création");
            return null;
        }
    }

    public String doSearch() {
        List<Computer> computers = ejbFacade.find(this.search);
        if (computers != null && computers.size() > 0) {
            items = new ListDataModel(computers);
            current = (Computer) items.getRowData();
        } else {
            items = null;
            current = null;
        }
        return "Computers.xhtml";
    }

    public String doUpdate() {
        try {
            if (current.getComputerId() != null) {
                getFacade().edit(current);
            } else {
                getFacade().create(current);
            }
            items = new ListDataModel(getFacade().find(search));
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ComputerUpdated"));
            return "Computers.xhtml";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void listenerBrandSelected(AjaxBehaviorEvent event) {
        modelItemsRefresh();
    }

    public String prepareEdit() {
        current = (Computer) getItems().getRowData();
        modelItemsRefresh();
        return "Computers.xhtml";
    }
    // Internal (private) methods
    private void modelItemsRefresh() {
        if (current != null) {
            modelItems = JsfUtil.getSelectItems(
                    modelFacade.findByBrand(current.getBrand()), true);
        } else {
            modelItems = null;
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Computer getComputer(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Computer.class)
    public static class ComputerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ComputerController controller = (ComputerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "computerController");
            return controller.getComputer(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Computer) {
                Computer o = (Computer) object;
                return getStringKey(o.getComputerId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Computer.class.getName());
            }
        }
    }
}
