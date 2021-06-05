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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Eric Schmitz
 */
@Entity
@Table(name = "computer")
@XmlRootElement
public class Computer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "computer_computer_id_seq")    
    @SequenceGenerator(name = "computer_computer_id_seq",
            sequenceName = "computer_computer_id_seq", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "computer_id")
    private Integer computerId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id")
    @ManyToOne(optional = false)
    private Brand brand;
    @JoinColumn(name = "model_id", referencedColumnName = "model_id")
    @ManyToOne
    private Model model;
    @JoinColumn(name = "type_id", referencedColumnName = "type_id")
    @ManyToOne(optional = false)
    private Type type;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "computer", fetch = FetchType.EAGER)
    private Collection<ComputerComponent> computerComponents;

    public Computer() {
    }

    public Computer(Integer computerId) {
        this.computerId = computerId;
    }

    public Computer(Integer computerId, String name) {
        this.computerId = computerId;
        this.name = name;
    }

    public Integer getComputerId() {
        return computerId;
    }

    public void setComputerId(Integer computerId) {
        this.computerId = computerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @XmlTransient
    public Collection<ComputerComponent> getComputerComponents() {
        return computerComponents;
    }

    public void setComputerComponents(Collection<ComputerComponent> computerComponents) {
        this.computerComponents = computerComponents;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (computerId != null ? computerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Computer)) {
            return false;
        }
        Computer other = (Computer) object;
        if ((this.computerId == null && other.computerId != null) || (this.computerId != null && !this.computerId.equals(other.computerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "be.isl.log.computer.entity.Computer[ computerId=" + computerId + " ]";
    }
    
}
