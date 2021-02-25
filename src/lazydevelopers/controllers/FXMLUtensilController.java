/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.controllers;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;
import lazydevelopers.interfaces.UtensilManager;
import lazydevelopers.interfaces.UtensilManagerFactory;
import lazydevelopers.models.Type;
import lazydevelopers.models.User;
import lazydevelopers.models.UserPrivilege;
import lazydevelopers.models.Utensil;

/**
 * Class to control the Utensils CRUD activities.
 *
 * @author Garikoitz
 */
public class FXMLUtensilController {

    private static final Logger LOGGER = Logger.getLogger("lazydevelopers.controllers.FXMLUtensilController");

    UtensilManager utensilManager;
    User user;

    // ATRIBUTOS
    private Stage stage;
    @FXML
    private Button utensilAddBtn;
    @FXML
    private Button utensilDeleteBtn;
    @FXML
    private TextField utensilNameTxt;
    @FXML
    private TableView<Utensil> utensilTableView;
    @FXML
    private TableColumn<Utensil, String> utensilNameColumn;
    @FXML
    private TableColumn<Utensil, Type> utensilTypeColumn;
    @FXML
    private TableColumn<Utensil, Integer> utensilAmountColumn;

    private ObservableList<Utensil> utensilsData;

    // MENÚ
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initialize the window with the parameters setted and the table charged.
     *
     * @param root the parent.
     */
    public void initStage(Parent root, User user) {
        try {
            LOGGER.info("Utensils management window opened.");
            Scene scene = new Scene(root);
            this.user = user;

            // Propiedades de Stage
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Utensils");
            stage.setResizable(false);

            // Botones
            utensilAddBtn.setDisable(false);
            utensilDeleteBtn.setDisable(true);

            utensilAddBtn.setOnAction(this::handleOnClickAdd);
            utensilDeleteBtn.setOnAction(this::handleOnClickDelete);

            // Confirmación para cerrar ventana.
            stage.onCloseRequestProperty().set(this::handleCloseRequest);

            // Campos
            utensilNameTxt.requestFocus();
            utensilNameTxt.textProperty().addListener(this::handleTextChange);

            // Cargo y establezco la tabla como editable.
            utensilTableView.getSelectionModel().selectedItemProperty().addListener(this::rowIsSelected);
            setUtensilTableView();
            setUtensilTableViewData();

            // Menú
            menuLoader();

            stage.show();

        } catch (Exception e) {
            LOGGER.severe("Something is wrong bro... " + e.getMessage());
        }
    }

    /**
     * Return an obsbservable list with the utensils types.
     *
     * @return an observable list with the utensils types.
     */
    private ObservableList<Type> getUtensilsType() {
        ObservableList<Type> types = FXCollections.observableArrayList(Type.values());
        return types;
    }

    /**
     * Sets the cell values ​​and makes the table editable.
     */
    private void setUtensilTableView() {
        // Pongo las factorias para las celdas en la tabla de utensilios.
        // Celda de name editable.
        utensilNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        utensilNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        utensilNameColumn.setOnEditCommit((TableColumn.CellEditEvent<Utensil, String> name) -> {
            if (!Pattern.matches("^[a-zA-Z0-9_ ]*$", name.getNewValue())) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("Sorry bro...");
                alert.setContentText("You only can write letters and numbers bro...");

                alert.showAndWait();
                utensilNameColumn.getOnEditCancel();
                utensilTableView.refresh();

            } else {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("UTENSIL NAME MODIFICATION");
                alert.setHeaderText("The utensil name will be modify bro...");
                alert.setContentText("Are you sure...?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Utensil utensil = (Utensil) name.getTableView().getItems()
                            .get(name.getTablePosition().getRow());

                    utensil.setName(name.getNewValue());

                    utensilManager.edit(utensil, utensil.getId());
                    LOGGER.info("Utensil name edited.");

                } else {
                    utensilNameColumn.getOnEditCancel();
                    utensilTableView.refresh();
                }

            }

        });

        // Celda de type editable.
        utensilTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        utensilTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(getUtensilsType()));
        utensilTypeColumn.setOnEditCommit(type -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("UTENSIL AMOUNT MODIFICATION");
            alert.setHeaderText("The utensil type will be modify bro...");
            alert.setContentText("Are sure?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Utensil utensil = (Utensil) type.getTableView().getItems()
                        .get(type.getTablePosition().getRow());

                utensil.setType(type.getNewValue());

                utensilManager.edit(utensil, utensil.getId());
                LOGGER.info("Utensil type edited.");

            } else {
                utensilTypeColumn.getOnEditCancel();
                utensilTableView.refresh();
            }

        });

        // Celda de amount editable.
        utensilAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        // Convierto el integer de amount en String.
        IntegerStringConverter converterInteger = new IntegerStringConverter();
        utensilAmountColumn.setCellFactory(TextFieldTableCell.<Utensil, Integer>forTableColumn(converterInteger));
        utensilAmountColumn.setOnEditCommit(amount -> {
            if (!Pattern.matches("^[0-9]+$", amount.getNewValue().toString())) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("Sorry bro...");
                alert.setContentText("You only can write numbers bro...");

                alert.showAndWait();
                utensilAmountColumn.getOnEditCancel();
                utensilTableView.refresh();

            } else {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("UTENSIL AMOUNT MODIFICATION");
                alert.setHeaderText("The utensil amount will be modify bro...");
                alert.setContentText("Are you sure?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Utensil utensil = (Utensil) amount.getTableView().getItems()
                            .get(amount.getTablePosition().getRow());

                    utensil.setAmount(amount.getNewValue());

                    utensilManager.edit(utensil, utensil.getId());
                    LOGGER.info("Utensil amount edited.");

                } else {
                    utensilAmountColumn.getOnEditCancel();
                    utensilTableView.refresh();
                }

            }

        });

    }

    /**
     * Charge the table view with utensils data.
     */
    private void setUtensilTableViewData() {
        try {
            utensilManager = UtensilManagerFactory.getUtensilImplementation();
            utensilsData = FXCollections.observableArrayList(utensilManager.getUtensils());
            LOGGER.info("Utensils table view charged.");

        } catch (Exception e) {
            LOGGER.severe("Something is wrong bro... " + e.getMessage());
            utensilTableView.setPlaceholder(new Label("Something is wrong in the server, sorry bro..."));
        }

        utensilTableView.setItems(utensilsData);

    }

    /**
     * This method add an utensil to DATABASE.
     *
     * @param event The window event.
     */
    private void handleOnClickAdd(ActionEvent event) {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("UTENSILS ADD");
            alert.setHeaderText("You will add an utensil bro");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Utensil utensil = new Utensil();
                utensil.setName("");
                utensil.setType(Type.PPE);
                utensil.setAmount(0);

                utensilManager.create(utensil);
                utensilTableView.getItems().add(utensil);

                int pos = utensilTableView.getItems().size() - 1;
                utensilTableView.getFocusModel().focus(pos);
                utensilTableView.requestFocus();

            } else {
                LOGGER.info("The utensil creation has been canceled.");

            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("SOMETHING IS WRONG");
            alert.setHeaderText("Sorry bro...");
            alert.setContentText("Something is wrong in the server, try later...");

            alert.showAndWait();
        }

    }

    /**
     * This method delete an utensil from DATABASE.
     *
     * @param event The window event.
     */
    private void handleOnClickDelete(ActionEvent event) {
        try {
            // Borrar selecionado
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("DELETE UTENSIL");
            alert.setHeaderText("You will delete an utensil bro...");
            alert.setContentText("Are you sure?");
            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get().equals(ButtonType.OK)) {
                utensilManager.remove(utensilTableView.getSelectionModel().getSelectedItem().getId().toString());
                LOGGER.info(utensilTableView.getSelectionModel().getSelectedItem().getName() + " is deleted.");

                // Refresco la tabla.
                setUtensilTableViewData();

            } else {
                event.consume();
                alert.close();
            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("SOMETHING IS WRONG");
            alert.setHeaderText("Sorry bro...");
            alert.setContentText("Something is wrong in the server, try later...");

            alert.showAndWait();
        }

    }

    /**
     * Method to request closing confirmation when the "X" of the window is pressed.
     *
     * @param event alert window.
     */
    private void handleCloseRequest(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CLOSE");
        alert.setHeaderText("Application will be closed bro...");
        alert.setContentText("Do you want to close the window...?");
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
     * Filtered the table with the text of the TextField only showing the matches.
     *
     * @param observable the type of the value.
     * @param oldValue old value of the event.
     * @param newValue new value of the event.
     */
    private void handleTextChange(ObservableValue observable, String oldValue, String newValue) {
        try {
            FilteredList<Utensil> filteredData = new FilteredList<>(utensilsData, u -> true);

            filteredData.setPredicate(utensil -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Si el texto del filtro está vacío, muestra todos los utensilios.
                }

                // Cojo el valor del campo de busqueda y lo pongo en minuscula.
                String lowerCaseFilter = newValue.toLowerCase();

                if (utensilNameTxt.getText().length() > 100) {
                    String maxLength = utensilNameTxt.getText().substring(0, 100);
                    utensilNameTxt.setText(maxLength);

                } else if (!Pattern.matches("^[a-zA-Z0-9_ ]*$", utensilNameTxt.getText())) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("INPUT ERROR");
                    alert.setHeaderText("Sorry bro...");
                    alert.setContentText("You only can write letters and numbers bro...");
                    
                    alert.showAndWait();
                    utensilNameTxt.clear();
                    setUtensilTableViewData();
                    
                } else {
                    if (utensil.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filtrado por nombre.

                    } else if (utensil.getAmount().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filtrado por amount.

                    } else if (utensil.getType().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filtrado por tipo.

                    }
                }

                return false; // No existen resultados.
            });
            // Envuelve la FilteredList en una SortedList. 
            SortedList<Utensil> sortedData = new SortedList<>(filteredData);

            // Vincula el comparador SortedList al comparador TableView.
            sortedData.comparatorProperty().bind(utensilTableView.comparatorProperty());

            // Agrega datos ordenados (y filtrados) a la tabla.
            utensilTableView.setItems(sortedData);

        } catch (Exception e) {
            LOGGER.warning("You cannot search because the server is down bro...");
        }

    }

    /**
     * This method ables and disables the delete button when a table row is selected or not.
     *
     * @param observable An entity that wraps a value and allows to observe the value for changes.
     * @param oldValue The old value that the method is going to observe.
     * @param newValue The new value that the method is going to observe.
     */
    private void rowIsSelected(ObservableValue observable, Object oldValue, Object newValue) {
        if (utensilTableView.getSelectionModel().getSelectedItem() != null) {
            utensilDeleteBtn.setDisable(false);

        } else {
            utensilDeleteBtn.setDisable(true);

        }
    }

    /**
     * This method set what have to do the menu.
     *
     * @param event The window event.
     */
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
