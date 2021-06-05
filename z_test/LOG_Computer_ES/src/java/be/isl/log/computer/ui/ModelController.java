package be.isl.log.computer.ui;

import be.isl.log.computer.entity.Model;
import be.isl.log.computer.ui.util.JsfUtil;
import be.isl.log.computer.business.ModelFacade;
import be.isl.log.computer.viewmodel.ModelSearch;
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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("modelCtrl")
@SessionScoped
public class ModelController implements Serializable {

    private Model current;
    private ModelSearch search;
    private DataModel items = null;
    @EJB
    private ModelFacade ejbFacade;

    public ModelController() {
    }

    @PostConstruct
    public void init() {
        search = new ModelSearch();
        items = new ListDataModel(ejbFacade.find(search));
        current = (Model) items.getRowData();
    }

    public Model getModel(Integer id) {
        return ejbFacade.find(id);
    }

    public Model getCurrent() {
        return current;
    }

    public void setCurrent(Model current) {
        this.current = current;
    }

    private ModelFacade getFacade() {
        return ejbFacade;
    }

    public DataModel getItems() {
        return items;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.find(null), true);
    }

    public ModelSearch getSearch() {
        return search;
    }

    public void setSearch(ModelSearch search) {
        this.search = search;
    }

    public String doDelete() {
        ejbFacade.remove(current);
        items = new ListDataModel(ejbFacade.find(search));
        if (items != null && items.getRowCount() > 0) {
            items.setRowIndex(0);
            current = (Model) getItems().getRowData();
        } else {
            current = new Model();
        }
        return "Models.xhtml";
    }

    public String doNew() {
        try {
            current = new Model();
            return "Models.xhtml";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Problème lors de la création");
            return null;
        }   
    }

    public String doSearch() {
        List<Model> models = ejbFacade.find(this.search);
        if (models != null && models.size() > 0) {
            items = new ListDataModel(models);
            current = (Model) items.getRowData();
        } else {
            items = null;
            current = null;
        }
        return "Models.xhtml";
    }

    public String doUpdate() {
        try {
            getFacade().edit(current);
            items = new ListDataModel(getFacade().find(search));
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ModelUpdated"));
            return "Models.xhtml";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    public String prepareEdit() {
        current = (Model) getItems().getRowData();
        return "Models.xhtml";
    }

    @FacesConverter(forClass = Model.class)
    public static class ModelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ModelController controller = (ModelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "modelCtrl");
            return controller.getModel(getKey(value));
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
            if (object instanceof Model) {
                Model o = (Model) object;
                return getStringKey(o.getModelId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Model.class.getName());
            }
        }

    }

}
