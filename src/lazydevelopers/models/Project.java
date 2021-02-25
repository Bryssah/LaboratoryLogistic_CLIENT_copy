/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class of the Project entity.
 *
 * @author Aingeru
 */

@XmlRootElement
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Identification field for the Project.
     */

    private Integer id;
    /**
     * Name of the project.
     */

    private String name;

    /**
     * Description of the project.
     */
    private String description;
    /**
     * Start date of the project.
     */
  
    private Date startDate;

    /**
     * Finish date of the project.
     */
 
    private Date finishDate;
    /**
     * Relational field containing the list of users on the project.
     */
 
    private Set<User> users;

    public Project(Integer id, String name, String description, Date startDate, Date finishDate, Set<User> users) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.finishDate =finishDate;
        this.users =users;
    }

    public Project() {
    }
    

    /**
     *
     * @return the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id the id to be set.
     */
    public void setId(Integer id) {
        this.id=id;
    }

    /**
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name the name to be set.
     */
    public void setName(String name) {
        this.name=name;
    }

    /**
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description the description to be set.
     */
    public void setDescription(String description) {
        this.description=description;
    }

    /**
     *
     * @return the start date.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate the start date to be set.
     */
    public void setStartDate(Date startDate) {
        this.startDate=startDate;
    }

    /**
     *
     * @return the finish date.
     */
    public Date getFinishDate() {
        return finishDate;
    }

    /**
     *
     * @param finishDate the finish date to be set.
     */
    public void setFinishDate(Date finishDate) {
        this.finishDate=finishDate;
    }
   /**
    * Relational field containing the list of users on the project.
    * @return the users.
    */
        public Set<User> getUsers() {
        return users;
    }
   /**
    * Relational field containing the list of users on the Project.
    * @param users the users to set. 
    */
    public void setUsers(Set<User> users) {
        this.users =users;
    }

    /**
     * Integer representation for Project instance.
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compares two Project objects for equality. This method consider a Project equal to another Project if their id fields have the same value.
     *
     * @param object the other Project object to compare to.
     * @return a boolean that its true if ids are equals.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Obtains a string representation of the Project.
     *
     * @return the String representing the Project.
     */
    @Override
    public String toString() {
        return "lazydevelopers.entity.Project[ id=" + id + " ]";
    }

}
