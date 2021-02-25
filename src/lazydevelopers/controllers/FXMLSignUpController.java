/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ClientErrorException;
import lazydevelopers.interfaces.UserManager;
import lazydevelopers.interfaces.UserManagerFactory;
import lazydevelopers.models.User;
import lazydevelopers.models.UserPrivilege;
import lazydevelopers.models.UserStatus;
import lazydevelopers.security.CryptographyClient;

/**
 * Class to control the actions of the SignUp window.
 *
 * @author Bryssa, Gari
 */
public class FXMLSignUpController {

    private static final Logger LOGGER = Logger
            .getLogger("clientreto1.controller.FXMLSignUpController");

    UserManager userManager;

    @FXML
    private Button btn_SignUp;
    @FXML
    private Button btn_Back;
    @FXML
    private TextField txt_Username;
    @FXML
    private TextField txt_Email;
    @FXML
    private TextField txt_Fullname;
    @FXML
    private PasswordField txt_PasswordSU;
    @FXML
    private PasswordField txt_VerifyPass;
    @FXML
    private Label err_Username;
    @FXML
    private Label err_Email;
    @FXML
    private Label err_Fullname;
    @FXML
    private Label err_Password;
    @FXML
    private Label err_VerifyPass;
    @FXML
    private ProgressIndicator pri_progression;

    private Stage stage;

    //Local data check
    private boolean usernameOK = false;
    private boolean emailOK = false;
    private boolean nameOK = false;
    private boolean passOK = false;
    private boolean verifyOK = false;

    /**
     * Set the stage from the main.
     *
     * @param stage of the window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Init method to set the stage, the action handlers and some properties.
     *
     * @param root the parent we recived from the SignIn window.
     * @author Bryssa
     */
    public void initStage(Parent root) {
        LOGGER.info("SignUp window opened."); // Gari
        Scene scene = new Scene(root);
        stage.setScene(scene);

        //Some properties of the stage
        stage.setTitle("Sign Up window");
        stage.setResizable(false);
        stage.onCloseRequestProperty().set(this::handleCloseRequest); // Gari

        //Sign Up button
        btn_SignUp.setDisable(true);
        btn_SignUp.setDefaultButton(true);
        btn_SignUp.setOnAction(this::handleButtonAction);

        //Back button
        btn_Back.setOnAction(this::handleButtonAction);

        //Text Fields
        txt_Username.textProperty().addListener(this::handleTextChange);
        txt_Email.textProperty().addListener(this::handleTextChange);
        txt_Fullname.textProperty().addListener(this::handleTextChange);
        txt_PasswordSU.textProperty().addListener(this::handleTextChange);
        txt_VerifyPass.textProperty().addListener(this::handleTextChange);
        txt_Username.requestFocus();

        txt_Username.setText("");
        txt_Email.setText("");
        txt_Fullname.setText("");
        txt_PasswordSU.setText("");
        txt_VerifyPass.setText("");
        stage.show();
    }

    /**
     * Method that handles the action of the buttons.
     *
     * @param event of a button actioned.
     * @author Bryssa
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource().equals(btn_SignUp)) {

            userManager = UserManagerFactory.getUserImplementation();
            User user = new User();

            user.setLogin(txt_Username.getText());
            user.setEmail(txt_Email.getText());
            user.setFullName(txt_Fullname.getText());

            String password = txt_PasswordSU.getText();
            byte[] passwordByte = CryptographyClient.cifrarTexto(password);
            String encryptedPassword = CryptographyClient.toHexadecimal(passwordByte);

            user.setPassword(encryptedPassword);
            user.setPrivilege(UserPrivilege.USER);
            user.setStatus(UserStatus.ENABLED);
            try{
                LOGGER.info("SignUp button actioned!");
                userManager.create(user);
                LOGGER.info("User created.");
                
            } catch(ClientErrorException e){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Sign Up error");
                alert.setHeaderText("Error creating an user.");
                alert.setContentText("Something is wrong...");
            }
                
            popUp(AlertType.INFORMATION, "User created successfully");
            txt_Username.clear(); 
            txt_Email.clear(); 
            txt_Fullname.clear();
            txt_PasswordSU.clear(); 
            txt_VerifyPass.clear();

        }
        if (event.getSource().equals(btn_Back)) { 
            LOGGER.info("Back button actioned!");
            backButtonClickHandler(); 
        }
    }

    /**
     * Method to check that the LOCAL data is right.
     *
     * @param observable the type of the value.
     * @param oldValue old value of the event.
     * @param newValue new value of the event.
     * @author Bryssa
     */
    private void handleTextChange(ObservableValue observable, String oldValue, String newValue) {
        LOGGER.info("User is changing text");
        String errString = null;
        btn_SignUp.setDisable(true);

        //Username local check
        if (txt_Username.isFocused()) {         //If is focused
            usernameOK = false;
            if (txt_Username.getText().isEmpty()) {                 //Field is empty
                errString = "Username cannot be empty";
            } else if (!Pattern.matches("[a-zA-Z0-9]+", txt_Username.getText())) {      //Syntax w/o especial characters or blankspaces
                errString = "Username not valid \n (Only A-Z and 0-9)";
            } else if (txt_Username.getText().length() < 5
                    || txt_Username.getText().length() > 20) { //Username length is between 5 and 20
                errString = "Username must to be between \n 5 – 20 characters";
            } else {//If username is OK
                errString = "";
                usernameOK = true;
            }
            err_Username.setText(errString);
        }

        //Email local check
        if (txt_Email.isFocused()) {                //If is focused
            emailOK = false;
            if (txt_Email.getText().isEmpty()) {    //Field is empty
                errString = "Email cannot be empty";
            } else if (!checkEmail(txt_Email.getText())) {              //Syntax is correct callin  a method to verify
                errString = "E-mail syntax. not valid\n(e.g. example@email.com)";
            } else if (txt_Email.getText().length() < 6
                    || txt_Email.getText().length() > 30) {             //Email length is between 6 and 20
                errString = "E-mail must to be between \n 6-30 characters";
            } else { //If email is OK
                errString = "";
                emailOK = true;
            }
            err_Email.setText(errString);
        }

        //Fullname local check
        if (txt_Fullname.isFocused()) {                  //If is focused
            nameOK = false;
            if (txt_Fullname.getText().isEmpty()) {         //Field is empty
                errString = "Fullname cannot be empty";
            } else if (!Pattern.matches("[a-z_A-Z_ _ñÑáÁéÉíÍóÓúÚ]+", txt_Fullname.getText())) { // 
                //[a-z_A-Z_ ]
                errString = "Fullname not valid\n(Only A-Z and blankspaces)";
            } else if (txt_Fullname.getText().length() < 5
                    || txt_Fullname.getText().length() > 50) {           //Fullname length is between 6 and 20
                errString = "Fullname must to be between \n 5 – 50 characters";
            } else {        //If fullname is OK
                errString = "";
                nameOK = true;
            }
            err_Fullname.setText(errString);
        }

        //Password local check
        if (txt_PasswordSU.isFocused()) {       //If is focused
            passOK = false;
            if (txt_PasswordSU.getText().isEmpty()) {                //Field is empty
                errString = "Password cannot be empty";
            } else if (txt_PasswordSU.getText().length() < 4
                    || txt_PasswordSU.getText().length() > 20) {    //Password length is between 4 and 20
                errString = "Password must to be between \n 4 – 20 characters";
            } else {                    //If password is OK
                errString = "";
                passOK = true;
            }
            err_Password.setText(errString);
        }

        //Password verification local check
        if (txt_VerifyPass.isFocused() || txt_PasswordSU.isFocused()) {             //If is focused
            verifyOK = false;
            if (txt_VerifyPass.getText().isEmpty()) {
                errString = "Password verificstion cannot be empty";
            } else if (!txt_VerifyPass.getText().equals(txt_PasswordSU.getText())) { //If password verification not matches the main password 
                errString = "Passwords do not match,\n try again";
            } else { //If password verification is OK
                errString = "";
                verifyOK = true;
            }
            err_VerifyPass.setText(errString);
        }

        int level = okLevel();//Gets the level of verification

        //Sets the progress in the indicator according to the level of verification
        pri_progression.setProgress(0.2 * level);

        if (level == 5) {//Final check, if all OK, enable SignUp button
            btn_SignUp.setDisable(false);
        }
    }

    /**
     * Method that verifies that the email contains the requested rules. Ex: "example@mail.com".
     *
     * @param email to check.
     * @return the email verified.
     * @author Gari
     */
    private boolean checkEmail(String email) {
        LOGGER.info("Checking email...");
        boolean ok;

        // Pattern for validate the email.
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);

        if (mather.find() == true) {
            ok = true;
        } else {
            ok = false;
            LOGGER.info("Email incorrect");
        }
        return ok;
    }

    /**
     * Method to verify if all the fields are right.
     *
     * @return the amount of right fields, 0 - all wrong / 5 - all right.
     * @author Bryssa
     */
    private int okLevel() {
        Integer level = 0;
        if (usernameOK) {
            level++;
        }
        if (emailOK) {
            level++;
        }
        if (nameOK) {
            level++;
        }
        if (passOK) {
            level++;
        }
        if (verifyOK) {
            level++;
        }
        return level;
    }

    /**
     * Method to build and show a requiered PopUp with parameters.
     *
     * @param type of Pop Up wanted -CONFIRMATION, INFORMATION, ERROR-.
     * @param msg Message to show in the Pop Up.
     * @author Bryssa
     */
    private void popUp(AlertType type, String msg) {
        LOGGER.info("Creating PopUp");
        Alert alert = null;
        alert = new Alert(type);

        try {
            if (type == AlertType.CONFIRMATION) { // Type of pop up is Confirmation
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                alert.setContentText("Are you sure?");
            } else //Type of pop up is info or error
            {
                alert.getButtonTypes().setAll(ButtonType.OK);
            }

            alert.setTitle("PopUp!");
            alert.setHeaderText(msg);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {//Button YES
                LOGGER.info("Entra en YES");
                stage.hide();
            } else //Button NO or OK
            {
                LOGGER.info("Entra en NO");
            }
            alert.close();
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
    }

    /**
     * Method to request closing confirmation when the "X" of the window is pressed.
     *
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

    /**
     * Method to ask for confirmation and execute an action by pressing the back button.
     *
     * @author Gari
     */
    private void backButtonClickHandler() {

        Alert alert = new Alert(AlertType.CONFIRMATION); // Make new confirmation alert.
        alert.setTitle("Go back"); // Title of the alert.
        String alertMsg = "Are you sure yo want to go back?"; // Text of the alert.
        alert.setContentText(alertMsg);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) { // If we click on OK we will run the method for change the stage.
            this.changeStageToLogin();
        }

    }

    /**
     * Method to return to the SignIn window and close where we are.
     *
     * @author Gari
     */
    private void changeStageToLogin() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lazydevelopers/views/SignIn.fxml")); // Charge the SignIn scene.
        Parent root = null;
        try {
            root = (Parent) loader.load();
            stage.close(); // Close the actual stage.
        } catch (IOException ex) {
            LOGGER.info("I/O error.");
        }
        FXMLSignInController controller = loader.getController(); // Load the controller.
        controller.setStage(new Stage());
        controller.initStage(root); // Init the new SignI stage.
    }
}
