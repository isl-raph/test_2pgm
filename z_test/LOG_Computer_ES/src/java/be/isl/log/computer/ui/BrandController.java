package be.isl.log.computer.ui;

import be.isl.log.computer.entity.Brand;
import be.isl.log.computer.ui.util.JsfUtil;
import be.isl.log.computer.business.BrandFacade;
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

@Named("brandCtrl")
@SessionScoped
public class BrandController implements Serializable {

    private Brand current;
    private Brand search;
    private DataModel items = null;
    @EJB
    private BrandFacade ejbFacade;

    public BrandController() {
    }

    @PostConstruct
    public void init() {
        search = new Brand();
        items = new ListDataModel(ejbFacade.find(search));
        current = (Brand) items.getRowData();
    }

    public Brand getBrand(Integer id) {
        return ejbFacade.find(id);
    }

    public Brand getCurrent() {
        return current;
    }

    public void setCurrent(Brand current) {
        this.current = current;
    }

    private BrandFacade getFacade() {
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

    public Brand getSearch() {
        return search;
    }

    public void setSearch(Brand search) {
        this.search = search;
    }

    public String doDelete() {
        ejbFacade.remove(current);
        items = new ListDataModel(ejbFacade.find(search));
        if (items != null && items.getRowCount() > 0) {
            items.setRowIndex(0);
            current = (Brand) getItems().getRowData();
        } else {
            current = new Brand();
        }
        return "Brands.xhtml";
    }

    public String doNew() {
        try {
            current = new Brand();
            return "Brands.xhtml";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Problème lors de la création");
            return null;
        }
    }

    public String doSearch() {
        List<Brand> brands = ejbFacade.find(search);
        if (brands != null && brands.size() > 0) {
            items = new ListDataModel(brands);
            current = (Brand) items.getRowData();
        } else {
            items = null;
            current = null;
        }
        return "Brands.xhtml";
    }

    public String doUpdate() {
        try {
            getFacade().edit(current);
            items = new ListDataModel(getFacade().find(search));
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BrandUpdated"));
            return "Brands.xhtml";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Brand) getItems().getRowData();
        return "Brands.xhtml";
    }

    @FacesConverter(forClass = Brand.class)
    public static class BrandControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BrandController controller = (BrandController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "brandCtrl");
            return controller.getBrand(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Brand) {
                Brand o = (Brand) object;
                System.out.println(o.getName());
                return getStringKey(o.getBrandId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Brand.class.getName());
            }
        }
    }
}