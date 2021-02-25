/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import lazydevelopers.models.Danger;
import lazydevelopers.models.Product;
import lazydevelopers.models.User;
import lazydevelopers.models.UserProduct;

/**
 *
 * @author Bryssa
 */
public class ProductManagerTestDataGenerator {
    private static final Logger LOGGER = Logger
        .getLogger("lazydevelopers.controllers.FXMLProductsController");
    
    private ArrayList testData;
    
    public ProductManagerTestDataGenerator(){
        testData = new ArrayList();
        Set<UserProduct> products;
        products = new HashSet();

        User dummy = new User();
        dummy.setUserId(999);

        Product prod = new Product();
        prod.setProductId(998);

        Date date = new Date();
        products.add(new UserProduct(dummy, prod, date));
        for(int i = 0;i<25;i++){
            if(i > 10)
                testData.add(new Product(i, "Nombre" + i, "Desc", i, Danger.HIGH, products));
            /*else if(i>10 && i<15){
                
            }*/
                
            else
                testData.add(new Product(i, "Nombre" + i, "Desc", i, Danger.MEDIUM, products));
        } 
    }
    
    public Collection getAllProducts (){
        LOGGER.info("Getting all users for UI");
        return testData;
    }
    
    public void isNameExisting(String name){
        LOGGER.info("Validatin product by name");
        /*if(testData.stream().filter()){
            
        }*/
    }
    
    
    
}
