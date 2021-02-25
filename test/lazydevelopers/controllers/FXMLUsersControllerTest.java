/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;

import java.util.concurrent.TimeoutException;
import laboratorylogistic_client.FXApplication;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author Paola
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FXMLUsersControllerTest extends ApplicationTest{
    
    public FXMLUsersControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws TimeoutException{
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(FXApplication.class);
    }

    @Test
    public void test0_InitialStage() {
        verifyThat("#btn_Login", isDisabled());
        verifyThat("#txt_Login", isEnabled());
        verifyThat("#txt_Password", isEnabled());
        verifyThat("#link_SignUp", isEnabled());
    }
    
    @Test
    public void test1_LoginAndPasswordAreFilled(){
        verifyThat("#txt_Login", hasText(""));
        verifyThat("#txt_Password", hasText(""));
        verifyThat("#btn_Login", isDisabled());
    }
    
    @Test
    public void test2_UserResponsible(){
        
        //El usuario para acceder a la "User list" debe ser "Responsible"
        clickOn("#txt_Login");
        write("prueba");
        clickOn("#txt_Password");
        write("abcd*1234");
        clickOn("#btn_Login");
        
    }
    
    @Test 
    public void test3_MenuUserList(){
        clickOn("Users");
        clickOn("#meb_userlist");
    }
    
    @Test
    public void test4_UserNotExists(){
        write("Bryssa");
        clickOn("#btn_Search");
        clickOn("Aceptar");
        clickOn("#txt_Search");
        eraseText(6);
        clickOn("#btn_Search");
    }
    
    @Test
    public void test5_UserExist(){
        clickOn("#txt_Search");
        write("prueba");
        clickOn("#btn_Search");
        clickOn("#txt_Search");
        eraseText(6);
        clickOn("#btn_Search");
    }
    
    @Test
    public void test6_CaracterLimit(){
        clickOn("#txt_Search");
        write("fffffffffffffffffffffffffffffffffffffffffffffffffff"); //52 Caracteres
        eraseText(52);
    }
    
    @Test
    public void test7_TechnicalFilter(){
        clickOn("#split_Privilege");
        clickOn("TECHNICAL");
        clickOn("#btn_Search");
        clickOn("#split_Privilege");
        clickOn("ANY");
        clickOn("#btn_Search");
        clickOn("#txt_Search");
    }
    
   /* @Test
    public void test8_DisabledFilter(){
        clickOn("#split_Status");
        clickOn("DISABLED");
        clickOn("#btn_Search");
        clickOn("#split_Status");
        clickOn("ANY");
        clickOn("#btn_Search");
    }
    
    @Test
    public void test9_EnabledFilter(){
        clickOn("#split_Status");
        clickOn("ENABLED");
        clickOn("#btn_Search");
        clickOn("#split_Status");
        clickOn("ANY");
        clickOn("#btn_Search");
    }
   
    @Test
    public void test10_StatusPrivilegeFilter(){
        clickOn("#split_Status");
        clickOn("ENABLED");
        clickOn("#split_Privilege");
        clickOn("RESPONSIBLE");
        clickOn("#btn_Search");
        clickOn("#split_Status");
        clickOn("ANY");
        clickOn("#split_Privilege");
        clickOn("ANY");
        clickOn("#btn_Search");
    }*/
    
    @Test
    public void test8_FilterData(){
        clickOn("#txt_Search");
        write("Pr");
        clickOn("#split_Privilege");
        clickOn("TECHNICAL");
        clickOn("#split_Status");
        clickOn("DISABLED");
        clickOn("#btn_Search");
        clickOn("#split_Status");
        clickOn("ENABLED");
        clickOn("#btn_Search");
        clickOn("Aceptar");
        clickOn("#txt_Search");
        eraseText(2);
        clickOn("#split_Status");
        clickOn("ANY");
        clickOn("#split_Privilege");
        clickOn("ANY");
        clickOn("#btn_Search");
    }
    
    @Test
    public void test9_Edit(){
        
        clickOn("Pahola");
        clickOn("#btn_Edit");
        clickOn("#checkBox_status");
        clickOn("#split_Privilege");
        clickOn("RESPONSIBLE");
        clickOn("#btn_Save");
        clickOn("Si");
        clickOn("Aceptar");
    }
    
    @Test
    public void test10_Delete(){
        
        clickOn("Pahola");
        clickOn("#btn_Delete");
        clickOn("Aceptar");
    }
    
}
