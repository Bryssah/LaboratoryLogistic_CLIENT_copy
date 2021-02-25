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
 * Representation of the relation between utensils and users
 * @author Garikoitz
 */

@XmlRootElement
public class UserUtensil implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributes   
    /**
     * The user.
     */
    private User user;
    
    /**
     * The utensil.
     */
    private Utensil utensil;
    
    /**
     * The date when an User used an Utensil.
     */
    private Date lastDateUtensil;
    
    // Constructors

    public UserUtensil(User user, Utensil utensil, Date lastDateUtensil) {
        this.user = user;
        this.utensil = utensil;
        this.lastDateUtensil = lastDateUtensil;
    }
    
    
    // Getters and setters 
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
     * 
     * @return  the utensil.
     */
    @XmlTransient
    public Utensil getUtensil() {
        return utensil;
    }
    
    /**
     * 
     * @param utensil utensil to be set.
     */
    public void setUtensil(Utensil utensil) {
        this.utensil = utensil;
    }
    
    /**
     * 
     * @return the last when an User used an Utensil.
     */
    public Date getLastDateUtensil() {
        return lastDateUtensil;
    }
    
    /**
     * 
     * @param lastDateUtensil lastDateUtensil to be set.
     */
    public void setLastDateUtensil(Date lastDateUtensil) {
        this.lastDateUtensil = lastDateUtensil;
    }

    // Methods
    /**
     * Integer representation for UserUtensil instance.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    /**
     * Compares two UserUtensil objects for equality. This method consider a UserUtensil equal to another UserUtensil if their id fields have the same value.
     *
     * @param obj the other UserUtensil object to compare to.
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
        final UserUtensil other = (UserUtensil) obj;
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.utensil, other.utensil)) {
            return false;
        }
        return true;
    }
    
    /**
     * Obtains a string representation of the UserUtensil.
     *
     * @return the String representing the UserUtensil.
     */
    @Override
    public String toString() {
        return "UserUtensil{" + "user=" + user + ", utensil=" + utensil + ", lastDateUtensil=" + lastDateUtensil + '}';
    }   
    
}
