/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import lazydevelopers.interfaces.UserManager;
import lazydevelopers.interfaces.UserManagerFactory;
import lazydevelopers.models.User;
import lazydevelopers.security.CryptographyClient;

/**
 * Manage the user login window.
 *
 * @author Gari
 */
public class FXMLSignInController {

    /**
     * This is the logger that it go to save information about the desktop application
     */
    private static final Logger LOGGER = Logger
            .getLogger("clientreto1.controller.FXMLSignInController");

    UserManager userManager;
    
    @FXML
    private Label err_Login;
    @FXML
    private Label err_Password;
    @FXML
    private TextField txt_Login;
    @FXML
    private PasswordField txt_Password;
    @FXML
    private Button btn_Login;
    @FXML
    private Hyperlink link_SignUp;
    @FXML
    private Hyperlink link_pass;

    private Stage stage;

    /**
     * Set stage for the login
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Set and initialize the stage and its properties.
     *
     * @param root
     */
    public void initStage(Parent root) {
        // Create a scene associated to to the parent root
        Scene scene = new Scene(root);

        // Associate the scene with the stage
        stage.setScene(scene);

        // Set window's properties
        stage.setTitle("Login");
        stage.setResizable(false);

        // Set window's event handlers button
        btn_Login.setDisable(true);
        btn_Login.setDefaultButton(true);
        btn_Login.setOnAction(this::handleButtonAction);

        // Set window's event handlers text
        txt_Login.requestFocus();
        txt_Login.textProperty().addListener(this::handleTextChanged);
        txt_Password.textProperty().addListener(this::handleTextChanged);

        // Set window's event handlers link
        link_SignUp.setOnAction(this::handleSignUp);
        link_pass.setOnAction(this::handleResetPassword);

        // Show the LogIn window
        stage.show();
    }

    /**
     * Set atributes to the controls that it need in the window showing event
     *
     * @param event
     */
    public void handleButtonAction(ActionEvent event) {

        LOGGER.info("Login button actioned!");

        User user = new User();
        userManager = UserManagerFactory.getUserImplementation();

        user.setLogin(txt_Login.getText());
        String password = txt_Password.getText();

        byte[] passwordByte = CryptographyClient.cifrarTexto(password);
        String encryptedPassword = CryptographyClient.toHexadecimal(passwordByte);

        // Pongo la password en el user que voy a enviar para el login.
        user.setPassword(encryptedPassword);

        userManager.userLogin(User.class, user);
        user = userManager.userLogin(User.class, user);

        if (user.getUserId() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lazydevelopers/views/MainMenuFXML.fxml"));
                Parent root = (Parent) loader.load();

                FXMLMainMenuController controller = (FXMLMainMenuController) loader.getController(); // Load the controller.
                Stage mainMenuStage = new Stage();

                controller.setStage(mainMenuStage);
                controller.initStage(root, user);
                stage.close();

            } catch (IOException e) {
                LOGGER.severe("Error loading MainMenu window.");
            }

        } else {
            LOGGER.severe("Password or user incorrect.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login error");
            alert.setContentText("Password or user incorrect.");
            alert.showAndWait();
            txt_Login.clear();
            txt_Password.clear();
        }

    }

    /**
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    public void handleTextChanged(ObservableValue observable, String oldValue, String newValue) {

        String errString = null;
        String errStringPass = null;

        if (txt_Login.getText().trim().isEmpty() || txt_Password.getText().trim().isEmpty()) {
            btn_Login.setDisable(true);

        } else {
            btn_Login.setDisable(false);

        }

        if (txt_Login.isFocused()) {
            if (txt_Login.getText().isEmpty()) {
                errString = "";

            } else if (!Pattern.matches("[a-zA-Z0-9]+", txt_Login.getText())) {
                errString = "Username not valid \n (Only A-Z and 0-9)";

            } else if (txt_Login.getText().length() < 6 || txt_Login.getText().length() > 20) {
                errString = "Username must to be between \n 6 – 20 characteres";

            }

            err_Login.setText(errString);
        }

        if (txt_Password.isFocused()) {

            if (txt_Password.getText().length() > 20) {
                errStringPass = "Use 20 characters maximum \nfor the password";

            }

            err_Password.setText(errStringPass);
        }
    }

    /**
     * Load the signUp xml and pass the control to it controller
     *
     * @param event
     */
    public void handleSignUp(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lazydevelopers/views/SignUp.fxml"));
        //Parent root = null;

        try {
            Parent root = (Parent) loader.load(); //aqui

            FXMLSignUpController controller = loader.getController();
            Stage signUp = new Stage();

            controller.setStage(signUp);
            controller.initStage(root);
            stage.close();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "An error in the SignUp loader", ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Sorry, an error has ocurred");
            alert.showAndWait();

        }

    }

    public void handleResetPassword(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reset password");
        dialog.setHeaderText("For refresh your password you should write your email.");
        dialog.setContentText("Please enter your email:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            User user = new User();
            userManager = UserManagerFactory.getUserImplementation();
            String email = result.get().toString();
            user.setEmail(email);
            userManager.resetPassword(user);
        }
    }

}
