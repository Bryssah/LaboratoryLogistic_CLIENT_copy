/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;

import java.util.Optional;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lazydevelopers.interfaces.UserManager;
import lazydevelopers.interfaces.UserManagerFactory;
import lazydevelopers.models.User;
import lazydevelopers.models.UserPrivilege;
import lazydevelopers.models.UserStatus;

/**
 *
 * @author Paola
 */
public class FXMLUsersController {
    
    private static final Logger LOGGER = Logger
            .getLogger("lazydevelopers.controllers.FXMLUsersController");
    
    private Stage stage;
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
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
    private Label lbl_Privilege;
    
    //Local elements
    @FXML
    private TextField txt_Search;
    @FXML
    private Button btn_Search;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox split_Privilege;
    @FXML
    private ChoiceBox split_Status;
    @FXML
    private Button btn_Edit;
    @FXML
    private Button btn_Delete;
    
    
    //Table
    @FXML
    private TableView table_User;
    @FXML
    private TableColumn column_Name;
    @FXML
    private TableColumn column_User;
    @FXML
    private TableColumn column_Email;
    @FXML
    private TableColumn column_LasConnection;
    @FXML
    private TableColumn column_Privilege;
    @FXML
    private TableColumn column_Status;
    
    private UserManager userManager; 
    
    private ObservableList<User> pList;
    
    private User user;
    
    private User selectedUser;
    
    
    
    
    public void initStage(Parent root,User user){
        LOGGER.info("User window opening.");
        this.user = user;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //Set window's properties
        stage.setTitle("Users");
        stage.setResizable(false);
        //El botón de Search (btn_Search) habilitado.
        //Set window's event handlers button
        btn_Search.setDisable(false);
        btn_Search.setDefaultButton(true);
        btn_Search.setOnAction(this::handleButtonAction);
        //SplitMenuButton privilege(Split_Privilege) y SplitMenuButton Status(Split_Status) habilitados. 
        split_Privilege.setDisable(false);
        split_Privilege.setItems(FXCollections.observableArrayList(
        "ANY",UserPrivilege.TECHNICAL.toString(),UserPrivilege.RESPONSIBLE.toString()));
        split_Privilege.setValue("ANY");
        
        split_Status.setDisable(false);
        split_Status.setItems(FXCollections.observableArrayList(
        "ANY",UserStatus.DISABLED.toString(),UserStatus.ENABLED.toString()));
        split_Status.setValue("ANY");
        //Los botones de Editar (btn_Edit) y  borrar (btn_Delete) están  deshabilitados.
        btn_Edit.setDisable(false);
        btn_Edit.setOnAction(this::handleEditAction);
        
        btn_Delete.setDisable(false);
        btn_Delete.setOnAction(this::handleDeleteAction);
        
        //Set window's event handlers text
        //Campo search (txt_Search)  habilitado. 
        txt_Search.requestFocus();
        txt_Search.textProperty().addListener(this::handleTextChange);
        
        
        //Menu
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
        lbl_Privilege.setText(this.user.getPrivilege().toString());
        
        /*Opción de menú User List(meb_userlist) visible y habilitado si 
        los privilegios del usuario conectado son de “RESPONSIBLE”.*/
        meb_userlist.setVisible(true);
        meb_userlist.setDisable(true);
        if(user.getPrivilege().equals(UserPrivilege.RESPONSIBLE))
            meb_userlist.setDisable(false);
        
        /*Tabla usuarios está habilitado y carga los datos 
        de todos los productos.*/
        //Method to load the table depending of the parameters
        table_User.setDisable(false);
        table_User.getSelectionModel().selectedIndexProperty()
                .addListener(this::handleUserSelectedChanged);
        //Table Factories
        column_Name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        column_User.setCellValueFactory(new PropertyValueFactory<>("login"));
        column_Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        column_LasConnection.setCellValueFactory(new PropertyValueFactory<>(""));
        column_Privilege.setCellValueFactory(new PropertyValueFactory<>("privilege"));
        column_Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        //Close
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        //Show the stage
        stage.show();
        loadTable(null,"ANY","ANY");
    }
    
    //---------------------------------------MENU----------------------------------------------
    /**
     * MENU The action of the button
     * @param event 
     */
    @FXML
    private void handleButtonAction(ActionEvent event){
       //Volver al main
        if(event.getSource().equals(meb_main)){
            try{
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
            }catch(Exception e){
                LOGGER.severe("Error: " + e.getMessage());
            }
        }
        //Ir a ventana productos
        if(event.getSource().equals(meb_products)){
            try{
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
            }catch(Exception e){
                LOGGER.severe("Error: " + e.getMessage());
            }
        }
        //Ir a ventana utensil
        if(event.getSource().equals(meb_utensils)){
            try{
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
            }catch(Exception e){
                LOGGER.severe("Error: " + e.getMessage());
            }
        }
       //Ir a ventana Projects
       if(event.getSource().equals(meb_projects)){
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
       //Ir a ventana user
        if(event.getSource().equals(meb_userlist)){
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
        //Ir a ventana Perfil
        if(event.getSource().equals(meb_profile)){
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
                //this.stage.close();
            }catch(Exception e){
                LOGGER.severe("Error: " + e.getMessage());
            }
        }
       
       if(event.getSource().equals(meb_logout)){
           if(popUp(Alert.AlertType.CONFIRMATION, "You are about to Sign Out."))
               signOut();
        }
       
        if (event.getSource().equals(btn_Search)) {
            LOGGER.info("Search button actioned!");
            try{
            if(event.getSource().equals(btn_Search)){//Search button
                loadTable(txt_Search.getText(), split_Privilege.getValue().toString(),split_Status.getValue().toString());
            }
            }catch(Exception e){
                 LOGGER.severe("EXCEPTION button handler: " + e.getMessage());   
            }
                    
                    
            //TODO
        }
    }
    
    //-----------------------------------------TABLE-------------------------------------------------
    
    /**
     * User selected.
     * 
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    @FXML
    private void handleUserSelectedChanged
        (ObservableValue observable, Object oldValue, Object newValue){
        /*Si no hay seleccionado un campo, se deshabilitan los botones 
        Edit y Delete.*/
        if(newValue == null){
            btn_Edit.setDisable(true);
            btn_Delete.setDisable(true);
            selectedUser= null;
        }
        //Si selecciona un campo se habilitan los botones Edit y Delete.
        else{
            btn_Edit.setDisable(false);
            btn_Delete.setDisable(false);
            selectedUser = ((User)table_User.getSelectionModel().getSelectedItem());
        }
    }
        
        /**
     * Delete User
     */
     private void deleteUser(){
        try{
            LOGGER.info("Deleting product");
            userManager = UserManagerFactory.getUserImplementation();
            userManager.remove(selectedUser.getUserId().toString());
        }catch(Exception e){
            LOGGER.severe("EXCEPTION on delete: " + e.getMessage());
        }
    }   
     
    
     
             
     /**
     * Load Table
     * @param fullName
     * @param Privilege
     * @param Status 
     */
    private void loadTable(String fullName, String Privilege, String Status){
       
            try{
                table_User.getItems().clear();
                //DataBase
                userManager = UserManagerFactory.getUserImplementation();
                pList = FXCollections.observableArrayList(userManager.getUsers());
                //Filter Name
                if(pList.isEmpty()){
                   popUp(Alert.AlertType.ERROR, "Data Base is empty, no data to load");  
                }else{
                    LOGGER.info("Filtering Data");
                    if(fullName!=null){
                        for(int i=0;i<pList.size();i++){
                            if(pList.get(i).getFullName().contains(fullName)){
                            } else {
                                pList.remove(i);
                                i--;
                            }
                        }
                    }
                }
                //Filter Status
                if(!Status.equals("ANY")){
                    for(int i=0;i<pList.size();i++){
                        if(!pList.get(i).getStatus().toString().equals(Status)){
                            pList.remove(i);
                            i--;
                        }
                    }
                }
                //Filter Privilege
                if(!Privilege.equals("ANY")){
                    for(int i=0;i<pList.size();i++){
                        if(!pList.get(i).getPrivilege().toString().equals(Privilege)){
                            pList.remove(i);
                            i--;
                        }
                    }
                }
                //Loading Data
                if(!pList.isEmpty()){
                    LOGGER.info("Loading Data");
                    table_User.setItems(pList); 
                }else{
                    popUp(Alert.AlertType.INFORMATION, "No Users Found");
                    LOGGER.info("No data to show");
                }
                
                
            }catch(Exception e){
                LOGGER.severe("EXCEPTION Loading table: " + e.getMessage());
            }
        
    }
    
    //----------------------------------RESTRICTIONS-------------------------------------
    /**
     * 
     * 
     * @param observable
     * @param oldValue
     * @param newVAlue 
     */
   
    private void handleTextChange(ObservableValue observable, String oldValue, String newVAlue){
         try{
            //Capar el tamaño del campo (txt_name) a 50 caracteres.
            if(txt_Search.getText().toString().length()>50){
                txt_Search.setText(txt_Search.getText().toString().substring(0,50));
            }
        }catch(Exception e){
            LOGGER.severe("EXCEPTION: " + e.getMessage());
        }     
    }
    
    //------------------------------POPUP---------------------------------
    
    /**
     * Method to build and show a requiered PopUp with parameters.
     *
     * @param type of Pop Up wanted -CONFIRMATION, INFORMATION, ERROR-.
     * @param msg Message to show in the Pop Up.
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
    
    //--------------------------------------BUTTONS---------------------------------------
    
    /**
     * Edit button
     * @param event 
     */
    public void handleEditAction(ActionEvent event){
       
        if(event.getSource().equals(btn_Edit)){
                LOGGER.info("Edit pushed");
                editUser();
                LOGGER.info("Edit finished, refresing table.");
                loadTable(null,"ANY","ANY");
            }
    }
    
    private void editUser(){
        try{
            LOGGER.info("Loading Edit user window");    
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/lazydevelopers/views/UserModifyMenuFXML.fxml"));
            Parent root = (Parent) loader.load();
            FXMLUserModifyController controller
                    = (FXMLUserModifyController) loader.getController();
            Stage editUserStage = new Stage();
            controller.setStage(editUserStage);
            controller.initStage(root, this.user, selectedUser);
        }catch(Exception e){
            LOGGER.severe("EXCEPTION on edit: " + e.getMessage());
        }
    }
    
   
    /**
     * Delete Button 
     * @param event 
     */
    public void handleDeleteAction(ActionEvent event){
        //try/catch
        LOGGER.info("Delete Button Actioned!");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("User will be Deleted");
        alert.setContentText("Do you want to delete de user?");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
           //TODO 
           deleteUser();
           //cerrar alerta
           alert.close();
           //Alerta de usuario borrado
           Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
           alert2.setTitle("Delete");
           alert2.setHeaderText("User Deleted");
           alert2.showAndWait();
           loadTable(null,"ANY","ANY");
           txt_Search.setText("");
           split_Status.setValue("ANY");
           split_Privilege.setValue("ANY");
           
        } else {
            event.consume();
            alert.close();
        }
    }
    
    //-----------------------------------CLOSED-----------------------------------------
    
    /**
     * 
     * @param event 
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
        } else {
            event.consume();
            alert.close();
        }
    }

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
}
