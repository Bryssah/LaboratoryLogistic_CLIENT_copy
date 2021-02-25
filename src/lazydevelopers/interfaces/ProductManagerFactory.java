/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.interfaces;

import lazydevelopers.services.ProductRESTClient;

/**
 * Return de ProductRESTClient implementation.
 * @author Bryssa
 */
public class ProductManagerFactory {
    /**
     * Method to return the implementation of the interface.
     * @return the implementation of the inteface.
     */
    public static ProductManager getProductImplementation(){
        return new ProductRESTClient();
    }
}
