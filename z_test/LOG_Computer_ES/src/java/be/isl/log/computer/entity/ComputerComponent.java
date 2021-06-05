/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.log.computer.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Eric Schmitz
 */
@Entity
@Table(name = "computer_component")
@XmlRootElement
public class ComputerComponent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "computer_component_computer_component_id_seq")    
    @SequenceGenerator(name = "computer_component_computer_component_id_seq",
            sequenceName = "computer_component_computer_component_id_seq", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "computer_component_id")
    private Integer computerComponentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_option")
    private boolean isOption;
    
    @JoinColumn(name = "component_id", referencedColumnName = "component_id")
    @ManyToOne(optional = false)
    private Component component;
    @JoinColumn(name = "computer_id", referencedColumnName = "computer_id")
    @ManyToOne(optional = false)
    private Computer computer;

    public ComputerComponent() {
    }

    public ComputerComponent(Integer computerComponentId) {
        this.computerComponentId = computerComponentId;
    }

    public ComputerComponent(Integer computerComponentId, int quantity) {
        this.computerComponentId = computerComponentId;
        this.quantity = quantity;
    }

    public Integer getComputerComponentId() {
        return computerComponentId;
    }

    public void setComputerComponentId(Integer computerComponentId) {
        this.computerComponentId = computerComponentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean getIsOption() {
        return isOption;
    }

    public void setIsOption(boolean isOption) {
        this.isOption = isOption;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (computerComponentId != null ? computerComponentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComputerComponent)) {
            return false;
        }
        ComputerComponent other = (ComputerComponent) object;
        if ((this.computerComponentId == null && other.computerComponentId != null) 
                || (this.computerComponentId != null && !this.computerComponentId.equals(other.computerComponentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "be.isl.log.computer.entity.ComputerComponent[ computerComponentId=" + computerComponentId + " ]";
    }
    
}
