/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lazydevelopers.interfaces.ProjectManager;
import lazydevelopers.interfaces.ProjectManagerFactory;
import lazydevelopers.models.Project;
import lazydevelopers.models.User;

/**
 * This class contains all the methods that controls the EditProject window.
 *
 * @author Aingeru
 */
public class FXMLEditProjectController {

    private static final Logger LOGGER = Logger
            .getLogger("lazydevelopers.controller.FXMLEditProjectController");

    private Stage stage;
    @FXML
    private AnchorPane projectAnchorPane;
    @FXML
    private TextField txt_EditProjectName;
    @FXML
    private TextArea txt_EditProjectDescription;
    @FXML
    private DatePicker date_EditProjectStartDate;
    @FXML
    private DatePicker date_EditProjectFinishDate;
    @FXML
    private Button btn_Save;
    @FXML
    private Button btn_Cancel;

    private Project project;
    
    private User user;

    ProjectManager projectManager;

    public FXMLEditProjectController() {
    }

    /**
     * Set the stage from the main.
     *
     * @param stage of the window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Set and initialize the stage and its properties.
     *
     * @param root Receives a parent stage that is the base class for all nodes that have children in the scene graph.
     */
    public void initStage(Parent root, User user) {
        try {

            //Create a scene associated to the parent root
            Scene scene = new Scene(root);
            // Stage properties
            stage = new Stage();
            this.user=user;
            stage.setScene(scene);
            stage.setTitle("Create Projects");
            stage.setResizable(false);
            stage.show();
            LOGGER.info("Add Project window opened.");
            btn_Save.setDisable(true);
            //set the handlers for the buttons and textfields
            stage.onCloseRequestProperty().set(this::handleCloseRequest);
            btn_Cancel.setOnAction(this::handleBackRequest);
            btn_Save.setOnAction(this::handleAddButtonAction);
            txt_EditProjectName.textProperty().addListener(this::handleTextChange);
            txt_EditProjectDescription.textProperty().addListener(this::handleTextChange);
            date_EditProjectStartDate.getEditor().textProperty().addListener(this::handleTextChange);
            date_EditProjectFinishDate.getEditor().textProperty().addListener(this::handleTextChange);
        } catch (Exception e) {
            LOGGER.info("Something was wrong." + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Sorry, an error has ocurred");
            alert.showAndWait();
        }
    }

    /**
     * This initStage is called when the "Modify" button is pressed in Project window
     *
     * @param root Receives a parent stage that is the base class for all nodes that have children in the scene graph.
     * @param project The project that receives from the Project window.
     */
    public void initStageModify(Parent root, Project project, User user) {
        try {

            //Create a scene associated to the parent root
            Scene scene = new Scene(root);
            this.project = project;
            // Stage properties
            stage = new Stage();
            this.user=user;
            stage.setScene(scene);
            stage.setTitle("Edit Projects");
            stage.setResizable(false);
            stage.show();
            LOGGER.info("Edit Project window opened.");
            btn_Save.setDisable(true);
            
            stage.onCloseRequestProperty().set(this::handleCloseRequest);
            btn_Cancel.setOnAction(this::handleBackRequest);
            
            btn_Save.setOnAction(this::handleModifyButtonAction);
            txt_EditProjectName.textProperty().addListener(this::handleTextChange);
            txt_EditProjectDescription.textProperty().addListener(this::handleTextChange);
            date_EditProjectStartDate.getEditor().textProperty().addListener(this::handleTextChange);
            date_EditProjectFinishDate.getEditor().textProperty().addListener(this::handleTextChange);
            //Fill the controls with the received project data
            txt_EditProjectName.setText(project.getName());
            txt_EditProjectDescription.setText(project.getDescription());
            date_EditProjectStartDate.setValue(project.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            date_EditProjectFinishDate.setValue(project.getFinishDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } catch (Exception e) {
            LOGGER.info("Something was wrong." + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Sorry, an error has ocurred");
            alert.showAndWait();
        }
    }

    /**
     * This method allows to create a Project in the database when the Save button is pressed.
     *
     * @param event An event that indicates that a window its going to change its status.
     */
    private void handleAddButtonAction(ActionEvent event) {

        try{    
        //create an empty project
        Project project = new Project();

        Date startDate = Date.from(date_EditProjectStartDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date finishDate = Date.from(date_EditProjectFinishDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        //set the data in the empty project from the window controls
        project.setName(txt_EditProjectName.getText());
        project.setDescription(txt_EditProjectDescription.getText());
        project.setStartDate(startDate);
        project.setFinishDate(finishDate);

        LOGGER.info("Project generated...Sending to server...");

        ProjectManagerFactory.getProjectImplementation().create(project);

        LOGGER.info("Project Created");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Project Created");
        alert.setContentText("Project Created succesfully.");
        alert.showAndWait();
        txt_EditProjectName.clear();
        txt_EditProjectDescription.clear();
        date_EditProjectStartDate.getEditor().clear();
        date_EditProjectFinishDate.getEditor().clear();
        }catch(Exception e){
            LOGGER.info("Something was wrong." + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server is down");
            alert.setContentText("Sorry, server is down");
            alert.showAndWait();
        }
    }

    /**
     * This method allows to modify a Project in the database when the Save button is pressed.
     *
     * @param event An event that indicates that a window its going to change its status.
     */
    private void handleModifyButtonAction(ActionEvent event) {

        try {
            Date startDate = Date.from(date_EditProjectStartDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date finishDate = Date.from(date_EditProjectFinishDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            //set the data in the empty project from the window controls
            project.setName(txt_EditProjectName.getText());
            project.setDescription(txt_EditProjectDescription.getText());
            project.setStartDate(startDate);
            project.setFinishDate(finishDate);

            LOGGER.info("Project generated...Sending to server...");
            projectManager = ProjectManagerFactory.getProjectImplementation();
            projectManager.edit(project.getId(), project);

            LOGGER.info("Project Modified");
            if (project != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Project modified");
                alert.setContentText("Project modified succesfully.");
                alert.showAndWait();
                changeStageToProject();
            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText("Failed to connect to server");
            alert.setContentText("Something is wrong in the server");

            alert.showAndWait();
        }

    }

    /**
     * This method enables or disables the Save button when all the textfields and data pickers  values are valid.
     *
     * @param observable an entity that wraps a value and allows to observe the value for changes
     * @param oldValue The old value that the method is going to observe
     * @param newValue The new value that the method is going to observe
     */
    private void handleTextChange(ObservableValue observable, String oldValue, String newValue) {
        if (txt_EditProjectName.getText().trim().isEmpty() || txt_EditProjectDescription.getText().trim().isEmpty() || date_EditProjectFinishDate.getEditor().getText().trim().isEmpty() || date_EditProjectStartDate.getEditor().getText().trim().isEmpty()) {
            btn_Save.setDisable(true);
        } else {
            btn_Save.setDisable(false);
             if(date_EditProjectStartDate.getValue().isAfter(date_EditProjectFinishDate.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Sorry, The Finish date cant be later than Start Date.");
            alert.showAndWait();
            date_EditProjectFinishDate.getEditor().clear();
        }
        }
        if (txt_EditProjectName.getText().length() > 30 ) {
                    String maxLength = txt_EditProjectName.getText().substring(0, 30);
                    txt_EditProjectName.setText(maxLength);
                }
        if (txt_EditProjectDescription.getText().length() > 80 ) {
                    String maxLength = txt_EditProjectDescription.getText().substring(0, 80);
                    txt_EditProjectDescription.setText(maxLength);
                }
        

       
    }

    /**
     * This method allows to close the actual window when the Back button is clicked.
     *
     * @param event An event that indicates that a window its going to change its status.
     */
    private void handleBackRequest(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // Make new confirmation alert.
        alert.setTitle("Go back"); // Title of the alert.
        String alertMsg = "Are you sure yo want to go back?"; // Text of the alert.
        alert.setContentText(alertMsg);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) { // If we click on OK we will run the method for change the stage.
            stage.close(); // Close the actual stage.
            changeStageToProject();
        }

    }

    /**
     * Method to return to the Project window and close where we are.
     *
     * @author Aingeru
     */
    private void changeStageToProject() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lazydevelopers/views/Project.fxml")); // Charge the Project scene.
        Parent root = null;
        try {
            root = (Parent) loader.load();
            stage.close(); // Close the actual stage.
        } catch (IOException ex) {
            LOGGER.info("I/O error.");
        }
        FXMLProjectController controller = loader.getController(); // Load the controller.
        controller.setStage(new Stage());
        controller.initStage(root, user); // Init the new Project stage.
    }

    /**
     * This method displays an alert to close the current window, which asks for confirmation
     *
     * @param event An event that indicates that a window its going to change its status.
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
            changeStageToProject();

        } else {
            event.consume();
            alert.close();
        }
    }

}