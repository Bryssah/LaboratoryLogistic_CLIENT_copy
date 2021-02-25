/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Model of the relation between User and Product with a Date attribute
 *
 * @author Bryssa
 */
@XmlRootElement
public class UserProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User ID
     */
    private User user;

    /**
     * Product ID
     */
    private Product product;

    /**
     * The date when a user 'uses' a product
     */
    private Date lastDateProduct;

    public UserProduct(User user, Product product, Date lastDateProduct){
        this.user = user;
        this.product = product;
        this.lastDateProduct = lastDateProduct;
    }
    
    /**
     *
     * @return the user.
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user user to be set.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the lastDateProduct
     */
    public Date getLastDateProduct() {
        return lastDateProduct;
    }

    /**
     * @param lastDateProduct the lastDateProduct to set
     */
    public void setLastDateProduct(Date lastDateProduct) {
        this.lastDateProduct = lastDateProduct;
    }

    /**
     * Integer representation for UserProduct instance
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    /**
     * Compares two UserProduct objects for equality. This method consider a UserProduct equal to another UserUtensil if their ID fields have the same value
     *
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
        final UserProduct other = (UserProduct) obj;
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        return true;
    }

    /**
     * Obtains a string representation of the UserUtensil
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "UserProduct{" + "user=" + getUser() + ", product=" + getProduct() + ", lastDateProduct=" + getLastDateProduct() + '}';
    }

}
