/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;

import java.util.Optional;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lazydevelopers.interfaces.UserManager;
import lazydevelopers.interfaces.UserManagerFactory;
import lazydevelopers.models.User;
import lazydevelopers.models.UserPrivilege;
import lazydevelopers.models.UserStatus;

/**
 *
 * @author 
 */
public class FXMLUserModifyController {

    private static final Logger LOGGER = Logger
            .getLogger("lazydevelopers.controllers.FXMLUsersController");
    
    @FXML
    private CheckBox checkBox_status;
    @FXML
    private ChoiceBox choiceBox_Privilege;
    @FXML
    private Label lbl_name;
    @FXML
    private Label lbl_User;
    @FXML
    private Label lbl_Email;
    @FXML
    private Button btn_Save;
    
    private User user;
    
    private User selectedUser;
    
    private Stage stage;
    
    UserManager userManager;
    
    
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    
    void initStage(Parent root,User user, User selectedUser) {
        LOGGER.info("User modify opening");
        this.user=user;
        
        Scene scene = new Scene(root);
        //Set windows properties
        stage.setScene(scene);
        stage.setTitle("Edit User");
        stage.setResizable(false);
        //Set window event handlers button
        btn_Save.setDisable(true);
        //btn_Save.setDefaultButton(true);
        btn_Save.setOnAction(this::handleButtonAction);
        //label recoge datos usuario
        lbl_name.setText(selectedUser.getFullName().toString());
        lbl_User.setText(selectedUser.getLogin().toString());
        lbl_Email.setText(selectedUser.getEmail().toString());
        
        choiceBox_Privilege.setDisable(false);
        choiceBox_Privilege.setItems(FXCollections.observableArrayList(
                "ANY", UserPrivilege.TECHNICAL.toString(),UserPrivilege.RESPONSIBLE,toString()));
        
        //edit

        //Some properties of the stage
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        stage.initModality(Modality.APPLICATION_MODAL);

        loadPrivilege();

        if(selectedUser!=null){//user selected
            this.selectedUser=selectedUser;
            stage.setTitle("Edit User");
            //Load data of the user
            choiceBox_Privilege.setValue(this.user.getPrivilege().toString());
            btn_Save.setDisable(false);
            btn_Save.setOnAction(this::handleButtonAction);
            if(this.selectedUser.getStatus().equals(UserStatus.ENABLED)){
              checkBox_status.setSelected(true);  
            }else if(this.selectedUser.getStatus().equals(UserStatus.DISABLED)){
                checkBox_status.setSelected(false);  
            }
            stage.show();
        }else{//No product selected
            popUp(Alert.AlertType.ERROR, "User not selected");
            stage.close();
        }
        
        //stage.show();
    }
    
     
     private void loadPrivilege(){
        LOGGER.info("Loading choice box privilege");
        //Load the levels of danger and an option to search for ANY level
         choiceBox_Privilege.setItems(FXCollections.observableArrayList(
            UserPrivilege.RESPONSIBLE.toString(),
                 UserPrivilege.TECHNICAL.toString()));
        
    }
        
    private void handleButtonAction(ActionEvent event){
        
        if(popUp(Alert.AlertType.CONFIRMATION, ("User " + lbl_name.getText() 
                + " will be saved"))){
            saveEditedUser();
            
        }
        saveEditedUser();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText("User Saved!");
        alert.showAndWait();
    }
    
    //falta buscar por id
    private void saveEditedUser(){
        try{
           /* if(this.selectedUser.equals(UserStatus.ENABLED)){
              checkBox_status.setIndeterminate(true);  
            }else if(this.selectedUser.equals(UserStatus.DISABLED)){
                checkBox_status.setIndeterminate(false);  
            }*/
           //Habilitar desactivar checkBox 
           if((checkBox_status.isSelected())){
               this.selectedUser.setStatus(UserStatus.ENABLED);
           }else{
               this.selectedUser.setStatus(UserStatus.DISABLED);
           }
            
            this.selectedUser.setPrivilege(UserPrivilege.valueOf(choiceBox_Privilege.getValue().toString()));
            
            userManager = UserManagerFactory.getUserImplementation();
            userManager.edit(this.selectedUser, this.selectedUser.getUserId().toString()); //selectedUser usuario seleccionado en la tabla. 
        }catch(Exception e){
            LOGGER.severe("EXCEPTION saving edited user: " + e.getMessage());
        }
    }
    
    //---------------------WINDOW-----------------------------
    
    
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
    
     private boolean popUp(Alert.AlertType type, String msg) {
        LOGGER.info("Creating PopUp");
        boolean res = false;
        Alert alert = null;
        alert = new Alert(type);

        try {
            if (type == Alert.AlertType.CONFIRMATION) { // Type of pop up is Confirmation
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                alert.setContentText("Are you sure?");
            } else //Type of pop up is info or error
            {
                alert.getButtonTypes().setAll(ButtonType.OK);
            }

            alert.setTitle("Important message!");
            alert.setHeaderText(msg);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {//Button YES
                LOGGER.info("Entra en YES");
                return res = true;
            } else //Button NO or OK
            {
                LOGGER.info("Entra en NO");
            }
            alert.close();
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
        return res;
    }
}
