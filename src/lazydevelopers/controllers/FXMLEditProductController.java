/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;


import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lazydevelopers.models.Product;
import lazydevelopers.models.User;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lazydevelopers.interfaces.ProductManager;
import lazydevelopers.interfaces.ProductManagerFactory;
import lazydevelopers.models.Danger;

/**
 * Window controller for the new/edit product window
 * @author Bryssa
 */
public class FXMLEditProductController {
        private static final Logger LOGGER = Logger
            .getLogger("lazydevelopers.controllers.FXMLProductsController");
    
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_back;
    @FXML
    private TextField txt_edit_name;
    @FXML
    private TextField txt_edit_amount;
    @FXML
    private TextArea txt_edit_description;
    @FXML
    private ChoiceBox spl_edit_danger;
    
    private Stage stage;
    private User user;
    private Product product;
    ProductManager productManager;
    
    /**
     * Set the stage from the window.
     *
     * @param stage of the window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Init stage for new product
     * @param root the parent root
     * @param user that is connected
     */
    public void initStage(Parent root, User user){//For new product
        LOGGER.info("Loading Init for New product Window");
        sharedInit(root);
        
        stage.setTitle("New Product");
        btn_save.setDisable(true);
        btn_save.setOnAction(this::newHandleButtonAction);
        spl_edit_danger.setValue(Danger.LOW.toString());
        
        stage.showAndWait();
    }
    
    /**
     * Init stage for edit product
     * @param root the parent root
     * @param user that is connected
     * @param product product to edit
     */
    public void initStage(Parent root, User user, Product product){//For edit product
        LOGGER.info("Loading Init for Edit product Window");
        sharedInit(root);

        if(product!=null){//Product selected
            this.product = product;
            stage.setTitle("Edit Product");
            //Load data of the product
            txt_edit_name.setText(this.product.getName());
            txt_edit_description.setText(this.product.getDescription());
            txt_edit_amount.setText(this.product.getAmount().toString());
            spl_edit_danger.setValue(this.product.getDanger().toString());
            btn_save.setDisable(false);
            btn_save.setOnAction(this::editHandleButtonAction);
            
            stage.showAndWait();
        }else{//No product selected
            popUp(Alert.AlertType.ERROR, "Product not selected");
            stage.close();
        }
    }
    
    /**
     * Init stage generic with shared properties
     * @param root the parent root
     */
    public void sharedInit(Parent root){
        LOGGER.info("Products window opening.");
        this.user = user;
        Scene scene = new Scene(root);
        stage.setScene(scene);

        //Some properties of the stage
        stage.setResizable(false);
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        stage.initModality(Modality.APPLICATION_MODAL);

        btn_back.setDisable(false);
        btn_back.setOnAction(this::handleButtonAction);
        txt_edit_name.setDisable(false);
        txt_edit_name.textProperty().addListener(this::handleTextChange);
        txt_edit_description.setDisable(false);
        txt_edit_description.textProperty().addListener(this::handleTextChange);
        txt_edit_amount.setDisable(false);
        txt_edit_amount.textProperty().addListener(this::handleTextChange);
        spl_edit_danger.setDisable(false);
        loadDanger();

    }
    
    /**
     * Method that handles the action of the button BACK.
     *
     * @param event of a button actioned.
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Window will be closed.");
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
    
    /**
     * Method that handles the action of the button Save for edit product.
     *
     * @param event of a button actioned.
     */
    @FXML
    private void editHandleButtonAction(ActionEvent event) {
        if(popUp(Alert.AlertType.CONFIRMATION, "Product " 
                    + this.product.getName() + " will be Saved.")){
                saveEditedProduct();
                popUp(Alert.AlertType.INFORMATION, "Product " 
                        + this.product.getName() + " saved");
                stage.close();
        }
    }
    
    /**
     * Method to save the changes of the product edited
     */
    private void saveEditedProduct(){
        try{
            this.product.setName(txt_edit_name.getText());
            this.product.setDescription(txt_edit_description.getText());
            this.product.setAmount(Integer.parseInt(txt_edit_amount.getText()));
            this.product.setDanger(dangerLevel(spl_edit_danger.getValue().toString()));
            productManager = ProductManagerFactory.getProductImplementation();
            productManager.edit(this.product, this.product.getProductId().toString());
        }catch(Exception e){
            LOGGER.severe("EXCEPTION saving edited product: " + e.getMessage());
        }
    }
    
    /**
     * Method that handles the action of the button Save for new product.
     *
     * @param event of a button actioned.
     */
    @FXML
    private void newHandleButtonAction(ActionEvent event) {
        if(popUp(Alert.AlertType.CONFIRMATION, "Product " 
                    + txt_edit_name.getText() + " will be Created.")){
                saveNewProduct();
                popUp(Alert.AlertType.INFORMATION, "Product " 
                        + txt_edit_name.getText() + " Created");
                stage.close();
        }
    }
    
    /**
     * Method to save the changes of the product created
     */
    private void saveNewProduct(){
        try{
            Product newProduct = new Product();
            newProduct.setName(txt_edit_name.getText());
            newProduct.setDescription(txt_edit_description.getText());
            newProduct.setAmount(Integer.parseInt(txt_edit_amount.getText()));
            newProduct.setDanger(dangerLevel(spl_edit_danger.getValue().toString()));
            
            productManager = ProductManagerFactory.getProductImplementation();
            productManager.create(newProduct);
        }catch(Exception e){
            LOGGER.severe("EXCEPTION saving new product: " + e.getMessage());
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
            if(txt_edit_name.isFocused()){//Name field
                if(txt_edit_name.getText().toString().length()>50){//Max length 50
                    txt_edit_name.setText(txt_edit_name.getText()
                            .toString().substring(0,50));
                }
            }
            if(txt_edit_description.isFocused()){//Description field
                if(txt_edit_description.getText().toString().length()>300){//Max length 300
                    txt_edit_description.setText(txt_edit_description
                            .getText().toString().substring(0,300));
                }
            }
            if(txt_edit_amount.isFocused()){//Amount field
                if(txt_edit_amount.getText().toString().length()>6){//Max length 6
                    txt_edit_amount.setText(txt_edit_amount.getText()
                            .toString().substring(0,6));
                }
                if(!Pattern.matches("[0-9]", txt_edit_amount.getText())){//Pattern 0-9
                    int cont = 0;
                    for(char current : txt_edit_amount.getText().toCharArray()){//Get te position o the non integer
                        if(!Pattern.matches("[0-9]", String.valueOf(current))){
                            break;
                        }else{
                            cont++;
                        }
                    }
                    txt_edit_amount.setText(txt_edit_amount.getText().toString().substring(0,cont));
                }
                if((Integer.parseInt(txt_edit_amount.getText())>100000)){//Max value to 100.000
                    txt_edit_amount.setText("100000");
                }
            }
            validateData();
            }catch(Exception e){
                LOGGER.severe("EXCEPTION: " + e.getMessage());
            }
    }
    
    private Danger dangerLevel(String danger){
        if(danger.equals(Danger.MEDIUM.toString()))
            return Danger.MEDIUM;
        if(danger.equals(Danger.HIGH.toString()))
            return Danger.HIGH;
        if(danger.equals(Danger.VERY_HIGH.toString()))
            return Danger.VERY_HIGH;
        else 
            return Danger.LOW;
    }
    
    /**
     * Validate if the fields are not empty
     */
    private void validateData(){
        if(!txt_edit_name.getText().isEmpty() 
                    && !txt_edit_description.getText().isEmpty()
                    && !txt_edit_amount.getText().isEmpty()
                    && spl_edit_danger.getValue() != null){
                btn_save.setDisable(false);
            }else{
                btn_save.setDisable(true);
            }
    }
    
    /**
     * Load the Danger level values
     */
    private void loadDanger(){
        LOGGER.info("Loading ChaiceBox of danger level");
        //Load the levels of danger and an option to search for ANY level
        spl_edit_danger.setItems(FXCollections.observableArrayList(
            Danger.LOW.toString(), Danger.MEDIUM.toString(), 
            Danger.HIGH.toString(), Danger.VERY_HIGH.toString()));
        /*if(product != null){
            spl_edit_danger.setValue(product.getDanger());
        }*/
        
    }
    
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
    
    /**
     * Method to request closing confirmation when the "X" of the window is pressed.
     * @param event pop-up alert window.
     */
    private void handleCloseRequest(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Window will be closed.");
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


