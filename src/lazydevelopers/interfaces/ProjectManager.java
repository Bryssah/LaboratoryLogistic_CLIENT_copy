/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.interfaces;

import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import lazydevelopers.models.Project;

/**
 *  This is the interface with the Project logic.
 * @author Aingeru
 */
public interface ProjectManager {
    public <T> T findAllProject(GenericType<T> responseType) throws ClientErrorException;
    
    public <T> T findProjectByText(Class<T> responseType, String text) throws ClientErrorException;
    
    public List<Project> getProjects();
    
    public void edit( Integer id,Object requestEntity) throws ClientErrorException;
    
    public <T> T find(Class<T> responseType, String id) throws ClientErrorException;
    
    public void create(Object requestEntity) throws ClientErrorException;
    
    public void remove(String id) throws ClientErrorException;
}
