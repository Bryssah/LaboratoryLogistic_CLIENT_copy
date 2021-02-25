/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity representing laboratory utensil. Contains utensil data.
 *
 * @author Garikoitz
 */
@XmlRootElement
public class Utensil implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributes
    /**
     * Identification field for the Utensil.
     */
    private SimpleIntegerProperty id;

    /**
     * Name of the utensil.
     */
    private SimpleStringProperty name;

    /**
     * Type of the utensil.
     */
    private ObjectProperty<Type> type;

    /**
     * Current amount of the utensils.
     */
    private SimpleIntegerProperty amount;

    // Constructors
    public Utensil(Integer id, String name, Type type, Integer amount) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleObjectProperty<Type>(type);
        this.amount = new SimpleIntegerProperty(amount);
    }

    public Utensil() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.type = new SimpleObjectProperty<Type>();
        this.amount = new SimpleIntegerProperty();
    }

    // Getters and setters
    /**
     *
     * @return the id.
     */
    public Integer getId() {
        return id.get();
    }

    /**
     *
     * @param id id the id to be set.
     */
    public void setId(Integer id) {
        this.id.set(id);
    }

    /**
     *
     * @return the name.
     */
    public String getName() {
        return name.get();
    }

    /**
     *
     * @param name name the name to be set.
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     *
     * @return the type.
     */
    public Type getType() {
        return type.get();
    }

    /**
     *
     * @param type the type to be set.
     */
    public void setType(Type type) {
        this.type.set(type);
    }

    /**
     *
     * @return the amount.
     */
    public Integer getAmount() {
        return amount.get();
    }

    /**
     *
     * @param amount the amount to be set.
     */
    public void setAmount(Integer amount) {
        this.amount.set(amount);
    }


    // Methods
    /**
     * Integer representation for Utensil instance.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    /**
     * Compares two Utensil objects for equality. This method consider a Utensil equal to another Utensil if their id fields have the same value.
     *
     * @return a boolean that its true if ids are equals.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Utensil other = (Utensil) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    /**
     * Obtains a string representation of the Utensil.
     *
     * @return the String representing the Utensil.
     */
    @Override
    public String toString() {
        return "Utensil{" + "id=" + id + ", name=" + name + ", type=" + type + ", amount=" + amount +"}";
    }

}
