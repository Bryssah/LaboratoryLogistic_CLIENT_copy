/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * User entity
 * @author paola
 */

@XmlRootElement
public class User implements Serializable {

    private static long serialVersionUID = 1L;
    /**
     * The user userId
     */
    
    private Integer userId;
    /**
     * The login of the user
     */
    
    private String login;
    
    /**
     * The password of the user account
     */
   
    private String password;
    
    /**
     * The full name of the user
     */
    
    private String fullName;
    
    /**
     * The email of the user
     */
   
    private String email;
    
    /**
     * The date of the las password change
     */
    
    private Date lastPasswordChange;

    /**
     * The last time the user logged in
     */
   
    private Date lastLogin;

    /**
     * The privilege of the user. It can be USER or ADMIN
     */
   
    private UserPrivilege privilege;

    /**
     * The user status. It can be ENABLED or DISABLED
     */
    
    private UserStatus status;
    
    //
    /**
     * A set with userUtensil
     */
    
    private Set<UserUtensil> usersUt;
    /**
     * A set with userProduct 
     */
   
    private Set<UserProduct> usersPr;
    
    /**
     * A set with project
     */
   
    private Project project;
    
    //Constructor para generar un usuario con sus datos
    public User (Integer userId, String login,String Password, String fullName,String email, Date lastPasswordChange,Date lastLogin, UserPrivilege privilege, UserStatus status, Set<UserUtensil> usersUt,Set<UserProduct> usersPr ){
        this.userId=userId;
        this.login= login;
        this.password= Password;
        this.fullName=fullName;
        this.email=email;
        this.lastPasswordChange=lastPasswordChange;
        this.lastLogin=lastLogin;
        this.privilege= privilege;
        this.status=status;
        this.usersUt=usersUt;
        this.usersPr=usersPr;
        
    }
    //Constructor vacio
    public User(){
        
    }
    

        //Seters and Geters
    /**
     * @return the usersUt
     */
    @XmlTransient
    public Set<UserUtensil> getUsersUt() {
        return usersUt;
    }
    /**
     * @param usersUt to usersUt
     */
    public void setUsersUt(Set<UserUtensil> usersUt) {
        this.usersUt = usersUt;
    }

    /**
     * @return the usersPr
     */
    @XmlTransient
    public Set<UserProduct> getUsersPr() {
        return usersPr;
    }

    /**
     * @param usersPr the usersPr to set
     */
    public void setUsersPr(Set<UserProduct> usersPr) {
        this.usersPr = usersPr;
    }
    /**
     * @return the project
     */
    @XmlTransient
    public Project getProject() {
        return project;
    }
    /**
     * @param project the project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }
    
    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login=login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName=fullName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email=email;
    }

    /**
     * @return the lastPasswordChange
     */
    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    /**
     * @param lastPasswordChange the lastPasswordChange to set
     */
    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    /**
     * @return the lastLogin
     */
    public Date getLastLogin() {
        return lastLogin;
    }

    /**
     * @param lastLogin the lastLogin to set
     */
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * @return the privilege
     */
    public UserPrivilege getPrivilege() {
        return privilege;
    }

    /**
     * @param privilege the privilege to set
     */
    public void setPrivilege(UserPrivilege privilege) {
        this.privilege=privilege;
    }

    /**
     * @return the status
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(UserStatus status) {
        this.status=status;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.userId);
        return hash;
    }

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
        final User other = (User) obj;
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", login=" + login + ", password=" + password + ", fullName=" + fullName + ", email=" + email + ", lastPasswordChange=" + lastPasswordChange + ", lastLogin=" + lastLogin + ", privilege=" + privilege + ", status=" + status + ", users=" + usersUt + ", users=" + usersUt + ", project=" + project + ", users=" + usersUt + ", users=" + usersUt + '}';
    }

   
    
}

