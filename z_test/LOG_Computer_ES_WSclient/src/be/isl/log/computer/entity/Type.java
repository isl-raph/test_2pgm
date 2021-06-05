/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.log.computer.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Eric Schmitz
 */
@Entity
@Table(name = "type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Type.findAll", query = "SELECT t FROM Type t"),
    @NamedQuery(name = "Type.findByTypeId", query = "SELECT t FROM Type t WHERE t.typeId = :typeId"),
    @NamedQuery(name = "Type.findByName", query = "SELECT t FROM Type t WHERE t.name = :name"),
    @NamedQuery(name = "Type.findByIsComputerType", query = "SELECT t FROM Type t WHERE t.isComputerType = :isComputerType")})
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "type_type_id_seq")    
    @SequenceGenerator(name = "type_type_id_seq",
            sequenceName = "type_type_id_seq", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "type_id")
    private Integer typeId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "is_computer_type")
    private boolean isComputerType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private Collection<Component> components;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private Collection<Computer> computers;

    public Type() {
    }

    public Type(Integer typeId) {
        this.typeId = typeId;
    }

    public Type(Integer typeId, String name, boolean isComputerType) {
        this.typeId = typeId;
        this.name = name;
        this.isComputerType = isComputerType;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsComputerType() {
        return isComputerType;
    }

    public void setIsComputerType(boolean isComputerType) {
        this.isComputerType = isComputerType;
    }

    @XmlTransient
    public Collection<Component> getComponents() {
        return components;
    }

    public void setComponents(Collection<Component> components) {
        this.components = components;
    }

    @XmlTransient
    public Collection<Computer> getComputers() {
        return computers;
    }

    public void setComputers(Collection<Computer> computers) {
        this.computers = computers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeId != null ? typeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Type)) {
            return false;
        }
        Type other = (Type) object;
        if ((this.typeId == null && other.typeId != null) || (this.typeId != null && !this.typeId.equals(other.typeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
