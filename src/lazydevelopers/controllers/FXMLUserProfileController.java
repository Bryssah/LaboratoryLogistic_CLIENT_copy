/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;

import java.util.Optional;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lazydevelopers.interfaces.UserManager;
import lazydevelopers.interfaces.UserManagerFactory;
import lazydevelopers.models.User;

/**
 *
 * @author Paola
 */
public class FXMLUserProfileController {
    
    private static final Logger LOGGER = Logger
            .getLogger("lazydevelopers.controllers.FXMLUserProfileController");
    @FXML
    private PasswordField txt_Pass;
    @FXML
    private Button btn_Password;
    @FXML
    private PasswordField pass_current;
    @FXML
    private PasswordField pass_NewPass;
    @FXML
    private PasswordField pass_VerifyPass;
    @FXML
    private Button btn_Save;
    @FXML
    private Label txt_Current;
    @FXML
    private Label txt_NewPass;
    @FXML
    private Label txt_VerifyPass;
    
    private User user;
    
    private Stage stage;
    
    private UserManager userManager;
    
    public void setStage(Stage stage){
        this.stage = stage;
    }

    void initStage(Parent root, User user) {
        LOGGER.info("User profile opening");
        this.user=user;
        Scene scene = new Scene(root);
        //Set windows properties
        stage.setScene(scene);
        stage.setTitle("Profile");
        stage.setResizable(false);
        //Set window event handlers button
        btn_Password.setDisable(false);
        btn_Password.setOnAction(this::handleChangeAction);
        btn_Save.setDisable(false);
        btn_Save.setOnAction(this::handleSaveAction);
        //Set window event handlers fields
        pass_current.setVisible(false);
        pass_NewPass.setVisible(false);
        pass_VerifyPass.setVisible(false);
        //Set window event handlers Label
        txt_Current.setVisible(false);
        txt_NewPass.setVisible(false);
        txt_VerifyPass.setVisible(false);
        
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        stage.show();
    }
    
    private void handleChangeAction(ActionEvent event){
        
        if(event.getSource().equals(btn_Password)){
            try{
                pass_current.setVisible(true);
                txt_Current.setVisible(true);
                pass_NewPass.setVisible(true);
                pass_VerifyPass.setVisible(true);
                txt_NewPass.setVisible(true);
                txt_VerifyPass.setVisible(true);
                 
            }catch(Exception e){
                
            }
        }
        
    }
    
    /*private void handleTextChange(ObservableValue observable, String oldValue, String newValue){
        
        //Password verification local check
        if (txt_Pass.isFocused() || pass_current.isFocused()) {             //If is focused
            boolean verifyOK = false;
            if (txt_Pass.getText().isEmpty()) {
                //error
            } else if (!txt_Pass.getText().equals(pass_current.getText())) { //If password verification not matches the main password 
                //error
            } else { //If password verification is OK
                //error
                verifyOK = true;
            }
            
        }
        
    }*/
    
    private void handleSaveAction(ActionEvent event){
        
        if(event.getSource().equals(btn_Save)){
            try{
                
                User userAux = this.user;
                userAux.setPassword(pass_NewPass.getText());
                userManager = UserManagerFactory.getUserImplementation();
                userManager.changePassword(userAux);
                
                pass_current.setVisible(false);
                txt_Current.setVisible(false);
                pass_NewPass.setVisible(false);
                pass_VerifyPass.setVisible(false);
                txt_NewPass.setVisible(false);
                txt_VerifyPass.setVisible(false);
            }catch(Exception e){
                LOGGER.severe("EXCEPTION profile: " + e.getMessage());
            }
        }
        
    }
    
     private void handleCloseRequest(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Application will be closed.");
        alert.setContentText("Do you want to close the window?");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
            stage.close();
        } else {
            event.consume();
            alert.close();
        }
    }
    
}
