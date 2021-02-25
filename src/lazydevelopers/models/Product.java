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

/**
 * Model representing Products and their attributes
 * @author Bryssa
 */
@XmlRootElement

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * The ID of the product which is generated automatically
     */
    private Integer productId;
    
    /**
     * Name of the product
     */
    private String name;
    
    /**
     * Description of the product
     */
    private String description;
    
    /**
     * Amount of the product in stock
     */
    private Integer amount;
    
    /**
     * Level of danger of the product {LOW/MEDIUM/HIGH/VERY_HIGH}
     */
    private Danger danger;
    
    /**
     * Relational field containing User used the utensils.
     */
    private Set<UserProduct> products;
    
    /**
     * Empty constructor
     */
    public Product(){
        
    }
    
    /**
     * Constructor with attributes
     * @param productId ID of Product
     * @param name Name of Product
     * @param desc Description of Product
     * @param amount Amount of Product in stock
     * @param danger Level of danger of Product
     */
    public Product(Integer productId, String name, String desc, Integer amount, Danger danger, Set<UserProduct> products){
        this.productId = productId;
        this.name = name;
        this.description = desc;
        this.amount = amount;
        this.danger = danger;
        this.products = products;
    }

    /**
     * @return the idProduct
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * @param idProduct the idProduct to set
     */
    public void setProductId(Integer idProduct) {
        this.productId = idProduct;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * @return the danger
     */
    public Danger getDanger() {
        return danger;
    }

    /**
     * @param danger the danger to set
     */
    public void setDanger(Danger danger) {
        this.danger = danger;
    }
    
    /**
     * 6Integer representation for Product instance
     * @return hash code
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.productId);
        return hash;
    }

    /**
     * Compares two Product objects for equality. This method
     * consider a Product equal to another Product if their ID
     * fields have the same value
     * @param obj to compare
     * @return boolean, true if IDs are equals
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.productId, other.productId)) {
            return false;
        }
        return true;
    }

    /**
     * Obtains a string representation of the Product
     * @return the String
     */
    @Override
    public String toString() {
        return "lazydevelopers.entity.Product[ id=" + getProductId() + " ]";
    }
}
