/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.interfaces;


import java.util.List;
import javax.ws.rs.ClientErrorException;
import lazydevelopers.models.User;

/**
 * The users logic.
 * @author Garikoitz
 */
public interface UserManager {
    public <T> T findUserByName(Class<T> responseType, String fullName) throws ClientErrorException;
    
    public void edit(Object requestEntity, String id) throws ClientErrorException;
    
    public <T> T findUserByPrivilege(Class<T> responseType, String privilege) throws ClientErrorException;
    
    public <T> T find(Class<T> responseType, String id) throws ClientErrorException;
    
    public <T> T findUserByStatus(Class<T> responseType, String status) throws ClientErrorException;
    
    public void create(Object requestEntity) throws ClientErrorException;
    
    public <T> T findAllUsers(Class<T> responseType) throws ClientErrorException;
    
    public void remove(String id) throws ClientErrorException;
    
    public <T> T userLogin(Class<T> responseType,Object requestEntity) throws ClientErrorException;
    
    public void resetPassword(Object requestEntity) throws ClientErrorException;
    
    public void changePassword(Object requestEntity) throws ClientErrorException;
    
    public List<User> getUsers();
}
