/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;

import java.util.Optional;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lazydevelopers.interfaces.ProductManager;
import lazydevelopers.interfaces.ProductManagerFactory;
import lazydevelopers.models.Danger;
import lazydevelopers.models.Product;
import lazydevelopers.models.User;
import lazydevelopers.models.UserPrivilege;

/**
 * Window controller for the product window
 * @author Bryssa
 */
public class FXMLProductsController {
    private static final Logger LOGGER = Logger
            .getLogger("lazydevelopers.controllers.FXMLProductsController");
    
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
    private Button btn_search;
    @FXML
    private Button btn_new;
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_delete;
    @FXML
    private TextField txt_name;
    @FXML
    private ChoiceBox spl_danger;
    @FXML
    private TableView tbv_table;
    @FXML
    private TableColumn tbc_name;
    @FXML
    private TableColumn tbc_description;
    @FXML
    private TableColumn tbc_danger;
    @FXML
    private TableColumn tbc_amount;
    
    
    private Stage stage;
    private User user;
    private ObservableList<Product> pList;
    ProductManager productManager;
    private Product selectedProduct;
    
    /**
     * Set the stage from the window.
     *
     * @param stage of the window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Init stage for product window
     * @param root the parent root
     * @param user that is connected
     */
    public void initStage(Parent root, User user){
        LOGGER.info("Products window opening.");
        this.user = user;
        Scene scene = new Scene(root);
        stage.setScene(scene);

        //Some properties of the stage
        stage.setTitle("Products");
        stage.setResizable(false);
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        
        
        //El botón Edit (btn_edit) está deshabilitado.
        btn_edit.setDisable(true);
        btn_edit.setOnAction(this::handleButtonAction);
        
        //El botón Delete (btn_delete) está deshabilitado.
        btn_delete.setDisable(true);
        btn_delete.setOnAction(this::handleButtonAction);
        
        //El botón Search (btn_search) está habilitado.
        btn_search.setDisable(false);
        btn_search.setOnAction(this::handleButtonAction);

        //El botón New (btn_new) está habilitado.
        btn_new.setDisable(false);
        btn_new.setOnAction(this::handleButtonAction);

        /*Los campos Name (txt_name), Last date (dat_date) y opciones de
        menu Logo(meb_main), Products(meb_products), Utensils(meb_utensils), 
        Projects(meb_projects), Profile(meb_profile), LogOut(meb_logout).*/
        txt_name.setDisable(false);
        txt_name.textProperty().addListener(this::handleTextChange);
        //Set to able the DatePicker if there is time
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
        
        /*Campo Danger (spl_danger) está habilitado y cargado con los niveles 
        de peligro.*/
        loadDanger();
        
        /*Campo privilege(txt_privilege) está habilitado y cargado con el 
        privilegio del usuario conectado.*/
        lbl_privilege.setText(this.user.getPrivilege().toString());
        
        /*Opción de menú User List(meb_userlist) visible y habilitado si 
        los privilegios del usuario conectado son de “RESPONSIBLE”.*/
        meb_userlist.setVisible(true);
        meb_userlist.setDisable(true);
        if(user.getPrivilege().equals(UserPrivilege.RESPONSIBLE))
            meb_userlist.setDisable(false);
        
        /*Tabla productos(tbv_table) está habilitado y carga los datos 
        de todos los productos.*/
        //Method to load the table depending of the parameters
        tbv_table.setDisable(false);
        tbv_table.getSelectionModel().selectedItemProperty()
                .addListener(this::handleProductSelectedChanged);
        //Table factories
        tbc_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbc_description.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        tbc_danger.setCellValueFactory(new PropertyValueFactory<>("danger"));
        tbc_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //Load the table with data
        loadTable(null,"ANY");
        
        //Se enfoca el campo Name(txt_name).
        txt_name.requestFocus();

        //Show the stage
        stage.show();
    }
    
    /**
     * Method to save the selected item and control the buttons
     * @param observable observable value
     * @param oldValue the old value
     * @param newValue te new value
     */
    @FXML
    private void handleProductSelectedChanged
        (ObservableValue observable, Object oldValue, Object newValue){
        /*Si no hay seleccionado un campo, se deshabilitan los botones 
        Edit y Delete.*/
        if(newValue == null){
            btn_edit.setDisable(true);
            btn_delete.setDisable(true);
            selectedProduct = null;
        }
        //Si selecciona un campo se habilitan los botones Edit y Delete.
        else{
            btn_edit.setDisable(false);
            btn_delete.setDisable(false);
            selectedProduct = ((Product)tbv_table.getSelectionModel().getSelectedItem());
        }
    }
    
    /**
     * Method that handles the action of the buttons.
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
        if(event.getSource().equals(meb_logout)){//LogOut/SignOut menu option
           if(popUp(Alert.AlertType.CONFIRMATION, "You are about to Sign Out."))
               signOut();
        }
        try{
            if(event.getSource().equals(btn_search)){//Search button
                loadTable(txt_name.getText(), spl_danger.getValue().toString());
            }
            if(event.getSource().equals(btn_delete)){//Delete button
                if(popUp(Alert.AlertType.CONFIRMATION, "Product " 
                        + selectedProduct.getName() + " will be deleted.")){
                    //Call to the method to delete selected product.
                    deleteProduct();
                    popUp(Alert.AlertType.INFORMATION, "Product " 
                            + selectedProduct.getName() + " deleted");
                    loadTable(txt_name.getText(), spl_danger.getValue().toString());
                }
            }
            if(event.getSource().equals(btn_edit)){// Edit button
                LOGGER.info("Edit pushed");
                editProduct();
                LOGGER.info("Edit finished, refresing table.");
                loadTable(txt_name.getText(), spl_danger.getValue().toString());
            }
            if(event.getSource().equals(btn_new)){//New product button
                LOGGER.info("New pushed");
                newProduct();
                LOGGER.info("New product finished, refresing table.");
                loadTable(txt_name.getText(), spl_danger.getValue().toString());
            }
        }catch(Exception e){
            LOGGER.severe("EXCEPTION button handler: " + e.getMessage());
        }
        
    }
    
    /**
    * Method to validate that the LOCAL data is right.
    *
    * @param observable the type of the value.
    * @param oldValue old value of the event.
    * @param newValue new value of the event.
    * @author Bryssa
    */
    private void handleTextChange(ObservableValue observable, String oldValue, String newValue) {
        try{
            //Capar el tamaño del campo (txt_name) a 50 caracteres.
            if(txt_name.getText().toString().length()>50){
                txt_name.setText(txt_name.getText().toString().substring(0,50));
            }
        }catch(Exception e){
            LOGGER.severe("EXCEPTION: " + e.getMessage());
        }
    }
    
    /**
     * Method to delete a product
     */
    private void deleteProduct(){
        try{
            LOGGER.info("Deleting product");
            productManager = ProductManagerFactory.getProductImplementation();
            productManager.remove(selectedProduct.getProductId().toString());
        }catch(Exception e){
            LOGGER.severe("EXCEPTION on delete: " + e.getMessage());
        }
    }
    
    /**
     * Method to eopen the edit window
     */
    private void editProduct(){
        try{
            LOGGER.info("Loading Edit product window");
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/lazydevelopers/views/EditProduct.fxml"));
            Parent root = (Parent) loader.load();
            FXMLEditProductController controller
                    = (FXMLEditProductController) loader.getController();
            Stage editProductStage = new Stage();
            controller.setStage(editProductStage);
            controller.initStage(root, this.user, selectedProduct);
        }catch(Exception e){
            LOGGER.severe("EXCEPTION on edit: " + e.getMessage());
        }
    }
    
    /**
     * Method to open the new window
     */
    private void newProduct(){
        try{
            LOGGER.info("Loading New product window");
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/lazydevelopers/views/EditProduct.fxml"));
            Parent root = (Parent) loader.load();
            FXMLEditProductController controller
                    = (FXMLEditProductController) loader.getController();
            Stage newProductStage = new Stage();
            controller.setStage(newProductStage);
            controller.initStage(root, this.user);
        }catch(Exception e){
            LOGGER.severe("EXCEPTION on new: " + e.getMessage());
        }
    }
    
    /**
     * Method which is called to load the data within the filtering values
     * sets by the user.
     * @param name of the product
     * @param danger level of the product
     */
    private void loadTable(String name, String danger){
        try{
            //Clear the table
            tbv_table.getItems().clear();

            //Data fro the database
            productManager = ProductManagerFactory.getProductImplementation();
            pList = FXCollections.observableArrayList(productManager.getProducts());

            if(pList.isEmpty()){//No data to filter/load
                popUp(Alert.AlertType.ERROR, "Data Base is empty, no data to load");
            }else{//There is data to load/filter
                LOGGER.info("Filtering data");
                if(name!=null){//Filter of name 
                    for(int i=0;i<pList.size();i++){
                        if(!pList.get(i).getName().contains(name)){
                            pList.remove(i);
                            i--;
                        }
                    }
                }
                if(!danger.equals("ANY")){//Filter of danger level
                    for(int i=0;i<pList.size();i++){
                        if(!pList.get(i).getDanger().toString().equals(danger)){
                            pList.remove(i);
                            i--;
                        }
                    }
                }
                if(!pList.isEmpty()){//Table is not empty
                    LOGGER.info("Loading data");
                    tbv_table.setItems(pList);
                }
                else{//Table is empty
                    popUp(Alert.AlertType.INFORMATION, "No products found.");
                    LOGGER.info("No data to show");
                }  
            }   
        }catch(Exception e){
            LOGGER.severe("EXCEPTION Loading table: " + e.getMessage());
        }  
    }
    
    /**
     * Method to load the options of the ChoiceBox of Danger Level
     */
    private void loadDanger(){
        LOGGER.info("Loading ChaiceBox of danger level");
        //Load the levels of danger and an option to search for ANY level
        spl_danger.setItems(FXCollections.observableArrayList(
            "ANY", Danger.LOW.toString(), Danger.MEDIUM.toString(), 
            Danger.HIGH.toString(), Danger.VERY_HIGH.toString()));
        //Set the value ANY as default option
        spl_danger.setValue("ANY");
        
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
