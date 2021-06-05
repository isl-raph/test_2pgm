package be.isl.log.computer.ui;

import be.isl.log.computer.entity.Type;
import be.isl.log.computer.ui.util.JsfUtil;
import be.isl.log.computer.business.TypeFacade;
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

@Named("typeCtrl")
@SessionScoped
public class TypeController implements Serializable {

    private Type current;
    private Type search;
    private DataModel items = null;
    @EJB
    private be.isl.log.computer.business.TypeFacade ejbFacade;

    public TypeController() {
    }

    @PostConstruct
    public void init() {
        search = new Type();
        items = new ListDataModel(ejbFacade.find(search));
        current = (Type) items.getRowData();
    }

    public Type getType(Integer id) {
        return ejbFacade.find(id);
    }

    public Type getCurrent() {
        return current;
    }

    public void setCurrent(Type current) {
        this.current = current;
    }

    private TypeFacade getFacade() {
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

    public Type getSearch() {
        return search;
    }

    public void setSearch(Type search) {
        this.search = search;
    }

    public String doDelete() {
        try {
            ejbFacade.remove(current);
            items = new ListDataModel(ejbFacade.find(search));
            if (items != null && items.getRowCount() > 0) {
                items.setRowIndex(0);
                current = (Type) getItems().getRowData();
            } else {
                current = new Type();
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Problème lors de la suppression");
        }
        return "Types.xhtml";
    }

    public String doNew() {
        try {
            current = new Type();
            return "Types.xhtml";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Problème lors de la création");
            return null;
        }
    }

    public String doSearch() {
        List<Type> types = ejbFacade.find(this.search);
        if (types != null && types.size() > 0) {
            items = new ListDataModel(types);
            current = (Type) items.getRowData();
        } else {
            items = null;
            current = null;
        }
        return "Types.xhtml";
    }

    public String doUpdate() {
        try {
            if (current.getTypeId() != null) {
                getFacade().edit(current);
            } else {
                getFacade().create(current);
            }
            items = new ListDataModel(getFacade().find(search));
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TypeUpdated"));
            return "Types.xhtml";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Type) getItems().getRowData();
        return "Types.xhtml";
    }

    @FacesConverter(forClass = Type.class)
    public static class TypeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TypeController controller = (TypeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "typeCtrl");
            return controller.getType(getKey(value));
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
            if (object instanceof Type) {
                Type o = (Type) object;
                return getStringKey(o.getTypeId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Type.class.getName());
            }
        }
    }
}
