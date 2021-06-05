package be.isl.log.computer.ui;

import be.isl.log.computer.entity.Component;
import be.isl.log.computer.ui.util.JsfUtil;
import be.isl.log.computer.business.ComponentFacade;
import be.isl.log.computer.business.ModelFacade;
import be.isl.log.computer.viewmodel.ComponentSearch;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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

@Named("componentCtrl")
@SessionScoped
public class ComponentController implements Serializable {

    private Component current;
    private ComponentSearch search;
    private DataModel items = null;
    private SelectItem[] modelItems = null;
    @Inject
    private ComponentFacade ejbFacade;
    @Inject
    ModelFacade modelFacade;

    public ComponentController() {
    }

    @PostConstruct
    public void init() {
        search = new ComponentSearch();
        items = new ListDataModel(ejbFacade.find(search));
        current = (Component) items.getRowData();
        modelItemsRefresh();
    }

    public Component getCurrent() {
        return current;
    }

    public void setCurrent(Component current) {
        this.current = current;
    }

    public ComponentSearch getSearch() {
        return search;
    }

    public void setSearch(ComponentSearch search) {
        this.search = search;
    }

    private ComponentFacade getFacade() {
        return ejbFacade;
    }

    public SelectItem[] getModelItems() {
        return modelItems;
    }

    public DataModel getItems() {
        return items;
    }

    public String doDelete() {
        ejbFacade.remove(current);
        items = new ListDataModel(ejbFacade.findAll());
        if (items != null && items.getRowCount() > 0) {
            items.setRowIndex(0);
            current = (Component) getItems().getRowData();
        } else {
            current = new Component();
        }
        return "Components.xhtml";
    }

    public String doNew() {
        try {
            current = new Component();
            return "Components.xhtml";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Problème lors de la création");
            return null;
        }
    }

    public String doSearch() {
        List<Component> components = ejbFacade.find(search);
        if (components != null && components.size() > 0) {
            items = new ListDataModel(components);
            current = (Component) items.getRowData();
        } else {
            items = null;
            current = null;
        }
        return "Components.xhtml";
    }

    public String doUpdate() {
        try {
            if (current.getComponentId() != null) {
                getFacade().edit(current);
            } else {
                getFacade().create(current);
            }
            items = new ListDataModel(getFacade().find(search));
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ComponentUpdated"));
            return "Components.xhtml";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void listenerBrandSelected(AjaxBehaviorEvent event) {
        modelItemsRefresh();
    }

    private void modelItemsRefresh() {
        if (current != null) {
            modelItems = JsfUtil.getSelectItems(
                    modelFacade.findByBrand(current.getBrand()), true);
        } else {
            modelItems = null;
        }
    }

    public String prepareEdit() {
        current = (Component) getItems().getRowData();
        modelItemsRefresh();
        return "Components.xhtml";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.find(null), true);
    }

    public Component getComponent(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Component.class)
    public static class ComponentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ComponentController controller = (ComponentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "componentCtrl");
            return controller.getComponent(getKey(value));
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
            if (object instanceof Component) {
                Component o = (Component) object;
                return getStringKey(o.getComponentId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Component.class.getName());
            }
        }

    }

}
