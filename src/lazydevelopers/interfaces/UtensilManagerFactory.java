/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.interfaces;

import lazydevelopers.services.UtensilRESTClient;

/**
 * Return de UtensilRESTClient implementation.
 * @author Garikoitz
 */
public class UtensilManagerFactory {
    
    /**
     * Method to return the implementation of the interface.
     * @return the implementation of the interface.
     */
    public static UtensilManager getUtensilImplementation(){
        return new UtensilRESTClient();
    }
}
