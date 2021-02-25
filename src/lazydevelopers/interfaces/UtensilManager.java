/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.interfaces;

import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import lazydevelopers.models.Utensil;

/**
 * The utensils logic.
 * @author Garikoitz
 */
public interface UtensilManager {
    public void edit(Object requestEntity, Integer id) throws ClientErrorException;

    public <T> T find(Class<T> responseType, String id) throws ClientErrorException;

    public <T> T findAllUtensils(GenericType<T> responseType) throws ClientErrorException;
    
    public List <Utensil> getUtensils();

    public void create(Object requestEntity) throws ClientErrorException;

    public <T> T findUtensilByName(Class<T> responseType, String name) throws ClientErrorException;

    public void remove(String id) throws ClientErrorException;

    public <T> T findUtensilByAmount(Class<T> responseType, String amount) throws ClientErrorException;

    public <T> T findByUtensilType(Class<T> responseType, String type) throws ClientErrorException;
}
