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
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lazydevelopers.interfaces.ProjectManager;
import lazydevelopers.interfaces.ProjectManagerFactory;
import lazydevelopers.models.Project;
import lazydevelopers.models.User;
import lazydevelopers.models.UserPrivilege;

/**
 * This class contains all the methods that controls the Project window.
 *
 * @author Aingeru
 */
public class FXMLProjectController {

    private static final Logger LOGGER = Logger
            .getLogger("lazydevelopers.controller.FXMLProjectController");

    private Stage stage;

    public FXMLProjectController() {
    }

    //Menu and Privilege elements
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
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<?, ?> projectNameColumn;
    @FXML
    private TableColumn<?, ?> projectDescColumn;
    @FXML
    private TableColumn<?, ?> projectStartColumn;
    @FXML
    private TableColumn<?, ?> projectFinishColumn;
    @FXML
    private TextField txt_ProjectName;
    @FXML
    private Button btn_AddProject;
    @FXML
    private Button btn_DeleteProject;
    @FXML
    private Button btn_ModifyProject;

    private ObservableList<Project> projectData;

    ProjectManager projectManager;

    private User user;

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
            this.user = user;
            // Stage properties
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Projects");
            stage.setResizable(false);
            

            LOGGER.info("Project window opened.");
            //Disable the buttons.
            btn_DeleteProject.setDisable(true);
            btn_ModifyProject.setDisable(true);
            //Set the tableView and the data
            setProjectTableView();
            setProjectTableViewData();
            //Set the actions to the elementes (buttons, fields and table)
            btn_DeleteProject.setOnAction(this::handleOnClicDelete);
            btn_AddProject.setOnAction(this::handleAddProject);
            btn_ModifyProject.setOnAction(this::handleModifyProject);

            txt_ProjectName.requestFocus();
            txt_ProjectName.textProperty().addListener(this::handleTextChange);

            stage.onCloseRequestProperty().set(this::handleCloseRequest);
            projectTable.getSelectionModel().selectedItemProperty().addListener(this::rowIsSelected);
            menuLoader();
            
            stage.show();
            
        } catch (Exception e) {
            LOGGER.info("Something was wrong." + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Sorry, an error has ocurred");
            alert.showAndWait();
        }
    }

    /**
     * This method set the factories for the cells on the Project TableView.
     */
    private void setProjectTableView() {

        //Factories for the cells on Project table.
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        projectDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        projectStartColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        projectFinishColumn.setCellValueFactory(new PropertyValueFactory<>("finishDate"));

    }

    /**
     * This method load the project list on the Project table.
     */
    private void setProjectTableViewData() {

        try {
            //Load the Project list on the table.
            projectManager = ProjectManagerFactory.getProjectImplementation();
            projectData = FXCollections.observableArrayList(projectManager.getProjects());
            LOGGER.info("Projects table view charged.");
        } catch (Exception e) {
            LOGGER.severe("Something is wrong " + e.getMessage());
            projectTable.setPlaceholder(new Label("Something is wrong in the server"));
        }
        projectTable.setItems(projectData);
    }

    /**
     * This method allows the delete button to delete a selected project.
     *
     * @param event An event that indicates that a window its going to change its status.
     */
    private void handleOnClicDelete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Project");
        alert.setHeaderText("A project will be deleted.");
        alert.setContentText("Do you want to delete the selected Project?");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
            // delete the selected row
            projectManager.remove(projectTable.getSelectionModel().getSelectedItem().getId().toString());
            projectTable.getItems().remove(projectTable.getSelectionModel().getSelectedItem());
            // refresh the data in the tableView.
            setProjectTableViewData();
        } else {
            event.consume();
            alert.close();
        }

    }

    /**
     * This method allows to change to the EditProject window when the "Add" button is pressed
     *
     * @param event An event that indicates that a window its going to change its status.
     */
    private void handleAddProject(ActionEvent event) {
        //Create the loader for the signup xml
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/lazydevelopers/views/EditProject.fxml"));
        //Create the parent and load the tree
        Parent root = null;
        try {
            root = (Parent) loader.load();
            //Create the Stage
            Stage editProjectStage = new Stage();
            //Load de controller
            FXMLEditProjectController controller = loader.getController();
            //Set the stage
            controller.setStage(editProjectStage);
            //Pass the control to the controller
            controller.initStage(root, user);
            stage.close();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "An error in the Project loader", ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Sorry, an error has ocurred");
            alert.showAndWait();
        }
    }

    /**
     * This method allows to change to the EditProject window when the "Modifiy" button is pressed
     *
     * @param event An event that indicates that a window its going to change its status.
     */
    private void handleModifyProject(ActionEvent event) {

        Project project = projectTable.getSelectionModel().getSelectedItem();
        //Create the loader for the signup xml
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/lazydevelopers/views/EditProject.fxml"));
        //Create the parent and load the tree
        Parent root = null;
        try {
            root = (Parent) loader.load();
            //Create the Stage
            Stage editProjectStage = new Stage();
            //Load de controller
            FXMLEditProjectController controller = loader.getController();
            //Set the stage
            controller.setStage(editProjectStage);
            //Pass the control to the controller
            controller.initStageModify(root, project, user);
            stage.close();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "An error in the SignIn loader", ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Sorry, an error has ocurred");
            alert.showAndWait();
        }
    }

    /**
     * This method controlls the txt_Project (search) textField.
     *
     * @param observable an entity that wraps a value and allows to observe the value for changes
     * @param oldValue The old value that the method is going to observe
     * @param newValue The new value that the method is going to observe
     */
    private void handleTextChange(ObservableValue observable, String oldValue, String newValue) {
        FilteredList<Project> filteredData = new FilteredList<>(projectData, u -> true);

        filteredData.setPredicate(project -> {
            if (newValue == null || newValue.isEmpty()) {
                return true; // Si el texto del filtro está vacío, muestra todos los utensilios.
            }

            // Cojo el valor del campo de busqueda y lo pongo en minuscula.
            String lowerCaseFilter = newValue.toLowerCase();
            if (txt_ProjectName.getText().length() > 80) {
                String maxLength = txt_ProjectName.getText().substring(0, 80);
                txt_ProjectName.setText(maxLength);
            }
            if (project.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filtrado por nombre.

            } else if (project.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filtrado por descripcion.

            } else if (project.getStartDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filtrado por fecha de inicio.

            } else if (project.getFinishDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filtrado por fecha de fin.

            }

            return false; // No existen resultados.
        });
        // Envuelve la FilteredList en una SortedList. 
        SortedList<Project> sortedData = new SortedList<>(filteredData);

        // Vincula el comparador SortedList al comparador TableView.
        sortedData.comparatorProperty().bind(projectTable.comparatorProperty());

        // Agrega datos ordenados (y filtrados) a la tabla.
        projectTable.setItems(sortedData);
    }

    /**
     * This method controls the buttons of the menu and allows it to change to other windows.
     *
     * @param event An event that indicates that a window its going to change its status.
     */
    @FXML
    private void handleMenuActions(ActionEvent event) {
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
        if (event.getSource().equals(meb_logout)) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Login out");
            alert.setHeaderText("You will log out bro...");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
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
                } catch (Exception e) {
                    LOGGER.severe("Exception Login Out: " + e.getMessage());
                }

            }
        }

    }

    /**
     * This method ables and disables the modify and delete buttons when a table row is selected or not.
     *
     * @param observable an entity that wraps a value and allows to observe the value for changes
     * @param oldValue The old value that the method is going to observe
     * @param newValue The new value that the method is going to observe
     */
    private void rowIsSelected(ObservableValue observable, Object oldValue, Object newValue) {

        if (projectTable.getSelectionModel().getSelectedItem() != null) {
            btn_ModifyProject.setDisable(false);
            btn_DeleteProject.setDisable(false);
        } else {
            btn_ModifyProject.setDisable(true);
            btn_DeleteProject.setDisable(true);
        }
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
            Platform.exit();
        } else {
            event.consume();
            alert.close();
        }
    }
    
    /**
     * This method set the actions to menu.
     */
    private void menuLoader() {
        meb_main.setDisable(false);
        meb_main.setOnAction(this::handleMenuActions);

        meb_products.setDisable(false);
        meb_products.setOnAction(this::handleMenuActions);

        meb_utensils.setDisable(false);
        meb_utensils.setOnAction(this::handleMenuActions);

        meb_projects.setDisable(false);
        meb_projects.setOnAction(this::handleMenuActions);

        meb_userlist.setDisable(false);
        meb_userlist.setOnAction(this::handleMenuActions);

        meb_profile.setDisable(false);
        meb_profile.setOnAction(this::handleMenuActions);

        meb_logout.setDisable(false);
        meb_logout.setOnAction(this::handleMenuActions);

        // Carga el label de privilegio
        lbl_privilege.setText(this.user.getPrivilege().toString());

        // Comportamiento según privilegio
        meb_userlist.setVisible(true);
        meb_userlist.setDisable(true);
        if (user.getPrivilege().equals(UserPrivilege.RESPONSIBLE)) {
            meb_userlist.setDisable(false);
        }
    }

}
