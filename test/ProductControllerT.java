/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.concurrent.TimeoutException;
import javafx.scene.input.KeyCode;
import laboratorylogistic_client.FXApplication;
import lazydevelopers.models.Danger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import org.testfx.matcher.control.TableViewMatchers;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 * Class with test methods
 * @author Bryssa
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductControllerT extends ApplicationTest{
    
    /**
     * Set up of the Start of application
     * @throws TimeoutException 
     */
    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(FXApplication.class);
   }

    /**
     * LogIn into the application
     */
    @Test
    public void test00_start() {
        clickOn("#txt_Login");
        write("bryssa");
        clickOn("#txt_Password");
        write("1234*abcd");
        clickOn("#btn_Login");
        clickOn("Menu");
        clickOn("#meb_products");
    }

    /**
     * Test if  the product window initialices correctly
     */
    @Test
    public void test01_window_initialization() {
        //txt_name
        verifyThat("#txt_name", isEnabled());
        verifyThat("#txt_name", isFocused());
        verifyThat("#txt_name", hasText(""));
        //spl_danger
        verifyThat("#spl_danger", isEnabled());
        //btn_search
        verifyThat("#btn_search", isEnabled());
        //btn_new
        verifyThat("#btn_new", isEnabled());
        //btn_edit
        verifyThat("#btn_edit", isDisabled());
        //btn_delete
        verifyThat("#btn_delete", isDisabled());
    }
    
    /**
     * Test if the edit/new window initialices correcty
     */
    @Test
    public void test02_new_edit_window() {
        clickOn("#btn_new");
        //txt_name
        verifyThat("#txt_edit_name", isEnabled());
        verifyThat("#txt_edit_name", isFocused());
        verifyThat("#btn_back", isEnabled());
        verifyThat("#btn_save", isDisabled());
        //Write 55 characters
        write("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        //50 Chars
        verifyThat("#txt_edit_name", hasText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaa"));
        
        clickOn("#txt_edit_amount");
        
        //Write a-1
        write("a-1");
        verifyThat("#txt_edit_amount", hasText("1"));
        //Write 999.999
        write("999999");
        //Limit of 100.000
        verifyThat("#txt_edit_amount", hasText("100000"));
        
        clickOn("#txt_edit_description");
        //Write 305 chars
        write("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaa");
        //Verify if it has 300 chars
        verifyThat("#txt_edit_description", hasText("aaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        
        //All fields filled, btn_save enabled
        verifyThat("#btn_save", isEnabled());
        
        //Go back
        clickOn("#btn_back");
        press​(javafx.scene.input.KeyCode.ENTER);
    }
    
    /**
     * Test of the creation of new product
     */
    @Test
    public void test03_new_product() {
        clickOn("#btn_new");
        write("Prueba");
        clickOn("#txt_edit_amount");
        write("10");
        clickOn("#txt_edit_description");
        write("Prueba descripcion");
        clickOn("#btn_save");
        clickOn("Sí");
        clickOn("Aceptar");

    }
    
    /**
     * Test of search a product
     */
    @Test
    public void test04_search_product() {
        clickOn("#txt_name");
        write("Prueba");
        clickOn("#btn_search");
        verifyThat("#tbv_table", TableViewMatchers.containsRow("Prueba", "Prueba descripcion", Danger.LOW, 10));
        clickOn("#txt_name");
        for(int i=0;i<6;i++){
            push(KeyCode.BACK_SPACE);
        }  
    }
    
    /**
     * Test of delete of a product
     */
    @Test
    public void test05_delete_product() {
        /*press​(javafx.scene.input.KeyCode.TAB);
        press​(javafx.scene.input.KeyCode.TAB);
        press​(javafx.scene.input.KeyCode.DOWN);*/
        clickOn("Prueba");
        clickOn("#btn_delete");
        clickOn("Sí");
        clickOn("Aceptar");
        clickOn("#txt_name");
        write("Prueba");
        clickOn("#btn_search");
        //PopUp of No result found
        press​(javafx.scene.input.KeyCode.ENTER);
        verifyThat("#tbv_table", hasNumRows(0));
    }
}
