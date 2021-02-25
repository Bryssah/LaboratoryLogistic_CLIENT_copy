/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;

import java.util.Optional;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lazydevelopers.models.User;
import lazydevelopers.models.UserPrivilege;

/**
 *
 * @author 2dam
 */
public class FXMLMainMenuController {
     private static final Logger LOGGER = Logger
            .getLogger("lazydevelopers.controllers.FXMLMainMenuController");
    
    //Shared elements
    @FXML
    private MenuItem meb_main;
    @FXML
    private MenuItem meb_products;
    @FXML
    private MenuItem meb_utensils;
    @FXML
    private MenuItem meb_projects;
    @FXML
    private MenuItem meb_userlist;
    @FXML
    private MenuItem meb_profile;
    @FXML
    private MenuItem meb_logout;
    @FXML
    private Label lbl_privilege;
    
    //Local elements
    @FXML
    private Label lbl_welcome;
    
    
    private Stage stage;
    private User user;
    
    /**
     * Set the stage from the window.
     *
     * @param stage of the window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Init stage for main menu
     * @param root the parent root
     * @param user that is connected
     */
    public void initStage(Parent root,User user){
        LOGGER.info("Main Menu window opening.");
        this.user = user;
        Scene scene = new Scene(root);
        stage.setScene(scene);

        //Some properties of the stage
        stage.setTitle("Main Menu");
        stage.setResizable(false);
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        
        /*Las opciones de menu Logo(meb_main), Products(meb_products), 
        Utensils(meb_utensils), Projects(meb_projects), Profile(meb_profile), 
        LogOut(meb_logout) están habilitados.*/
        meb_main.setDisable(false);
        meb_main.setOnAction(this::handleButtonAction);
        meb_products.setDisable(false);
        meb_products.setOnAction(this::handleButtonAction);
        meb_utensils.setDisable(false);
        meb_utensils.setOnAction(this::handleButtonAction);
        meb_projects.setDisable(false);
        meb_projects.setOnAction(this::handleButtonAction);
        meb_userlist.setDisable(false);
        meb_userlist.setOnAction(this::handleButtonAction);
        meb_profile.setDisable(false);
        meb_profile.setOnAction(this::handleButtonAction);
        meb_logout.setDisable(false);
        meb_logout.setOnAction(this::handleButtonAction);
        
        /*Campo privilege(txt_privilege) está habilitado y cargado con el
        privilegio del usuario conectado.*/
        lbl_privilege.setDisable(false);
        lbl_privilege.setText(this.user.getPrivilege().toString());
        
        /*Campo Welcome <User>(lbl_welcome) está habilitado y cargado con 
        el nombre de usuario conectado.*/
        lbl_welcome.setDisable(false);
        lbl_welcome.setText(this.user.getLogin());
        
        /*Opción de menú User List(meb_userlist) visible y habilitado si 
        los privilegios del usuario conectado son de “RESPONSIBLE”.*/
        meb_userlist.setVisible(true);
        meb_userlist.setDisable(true);
        if(user.getPrivilege().equals(UserPrivilege.RESPONSIBLE))
            meb_userlist.setDisable(false);
        
        stage.show();
    }
    
    /**
     * Method that handles the action of the menus
     *
     * @param event of a button actioned.
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource().equals(meb_main)) {
            try {
                LOGGER.info("Loading Main Menu Window");
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/lazydevelopers/views/MainMenuFXML.fxml"));
                Parent root = (Parent) loader.load();
                FXMLMainMenuController controller
                        = (FXMLMainMenuController) loader.getController();
                Stage mainMenuStage = new Stage();
                controller.setStage(mainMenuStage);
                controller.initStage(root, this.user);
                this.stage.close();
            } catch (Exception e) {
                LOGGER.severe("Error: " + e.getMessage());
            }
        }
        if (event.getSource().equals(meb_products)) {
            try {
                LOGGER.info("Loading Products Window");
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/lazydevelopers/views/Product.fxml"));
                Parent root = (Parent) loader.load();
                FXMLProductsController controller
                        = (FXMLProductsController) loader.getController();
                Stage productsStage = new Stage();
                controller.setStage(productsStage);
                controller.initStage(root, this.user);
                this.stage.close();
            } catch (Exception e) {
                LOGGER.severe("Error: " + e.getMessage());
            }
        }
        if (event.getSource().equals(meb_utensils)) {
            try {
                LOGGER.info("Loading Utensils Window");
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/lazydevelopers/views/Utensil.fxml"));
                Parent root = (Parent) loader.load();
                FXMLUtensilController controller
                        = (FXMLUtensilController) loader.getController();
                Stage utensilStage = new Stage();
                controller.setStage(utensilStage);
                controller.initStage(root, this.user);
                this.stage.close();
            } catch (Exception e) {
                LOGGER.severe("Error: " + e.getMessage());
            }
        }
        if (event.getSource().equals(meb_projects)) {
            try{
                LOGGER.info("Loading Projects Window");
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/lazydevelopers/views/Project.fxml"));
                Parent root = (Parent) loader.load();
                FXMLProjectController controller
                        = (FXMLProjectController) loader.getController();
                Stage projectStage = new Stage();
                controller.setStage(projectStage);
                controller.initStage(root, this.user);
                this.stage.close();
            }catch(Exception e){
                LOGGER.severe("Error: " + e.getMessage());
            }
        }
        if (event.getSource().equals(meb_userlist)) {
            try{
                LOGGER.info("Loading User List Window");
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/lazydevelopers/views/UserMenuFXML.fxml"));
                Parent root = (Parent) loader.load();
                FXMLUsersController controller
                        = (FXMLUsersController) loader.getController();
                Stage usersStage = new Stage();
                controller.setStage(usersStage);
                controller.initStage(root, this.user);
                this.stage.close();
            }catch(Exception e){
                LOGGER.severe("Error: " + e.getMessage());
            }
        }
        if (event.getSource().equals(meb_profile)) {
            try{
                LOGGER.info("Loading Profile Window");
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/lazydevelopers/views/UserProfileMenuFXML.fxml"));
                Parent root = (Parent) loader.load();
                FXMLUserProfileController controller
                        = (FXMLUserProfileController) loader.getController();
                Stage profileStage = new Stage();
                controller.setStage(profileStage);
                controller.initStage(root, this.user);
                this.stage.close();
            }catch(Exception e){
                LOGGER.severe("Error: " + e.getMessage());
            }
        }
        if(event.getSource().equals(meb_logout)){
           if(popUp(Alert.AlertType.CONFIRMATION, "You are about to Sign Out."))
               signOut();
        }
    }
    
    /**
     * Method to SignOut in the application.
     * Returns the user to SignIn window.
     */
    private void signOut(){
        try{
            LOGGER.info("Signin Out...");
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/lazydevelopers/views/SignIn.fxml"));
            Parent root = (Parent) loader.load();
            FXMLSignInController controller
                    = (FXMLSignInController) loader.getController();
            Stage signInStage = new Stage();
            controller.setStage(signInStage);
            controller.initStage(root);
            this.stage.close();
        }catch(Exception e){
            LOGGER.severe("Exception Login Out: " + e.getMessage());
        }
        
    }
    
    /**
     * Method to build and show a requiered PopUp with parameters.
     *
     * @param type of Pop Up wanted -CONFIRMATION, INFORMATION, ERROR-.
     * @param msg Message to show in the Pop Up.
     * @author Bryssa
     */
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
    
    /**
     * Method to request closing confirmation when the "X" of the window is pressed.
     * @param event pop-up alert window.
     * @author Gari
     */
    private void handleCloseRequest(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Application will be closed.");
        alert.setContentText("Do you want to close the window?");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
            stage.close();
            Platform.exit();
        } else {
            event.consume();
            alert.close();
        }
    }
}
