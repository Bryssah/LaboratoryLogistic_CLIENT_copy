/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.interfaces;

import lazydevelopers.services.ProjectRESTClient;

/**
 * Return the  ProjectRESTClient implementation.
 * @author Aingeru
 */
public class ProjectManagerFactory {
    
    public static ProjectManager getProjectImplementation(){
        return new ProjectRESTClient();
    }
}
