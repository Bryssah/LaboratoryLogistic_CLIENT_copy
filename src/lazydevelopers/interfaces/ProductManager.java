/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.interfaces;

import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import lazydevelopers.models.Product;

/**
 * ProductManager interface
 * @author Bryssa
 */
public interface ProductManager {
    public void edit(Object requestEntity, String id) throws ClientErrorException;
     
    public <T> T find(Class<T> responseType, String id) throws ClientErrorException;
     
    public <T> T findProductByName(Class<T> responseType, String name) throws ClientErrorException;
              
    public List<Product> getProducts();
    
    public void create(Object requestEntity) throws ClientErrorException;
    
    public <T> T findAllProducts(GenericType<T> responseType) throws ClientErrorException;
    
    public void remove(String id) throws ClientErrorException;
}
