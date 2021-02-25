
import java.util.concurrent.TimeoutException;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import laboratorylogistic_client.FXApplication;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.matcher.control.TableViewMatchers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * This class contains the tests for the FXMLProjectController.
 *
 * @author Aingeru
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FXMLProjectControllerTest extends ApplicationTest {

    TableView table;
    TextField txt_EditProjectName;
    TextArea txt_EditProjectDescription;

    /* @Override
    public void start(Stage stage) throws Exception {

        new FXApplication().start(stage);
    }*/
    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(FXApplication.class);
    }

    @Test
    public void test01_Initialization() {
        clickOn("#txt_Login");
        write("aingeru");
        clickOn("#txt_Password");
        write("1234");
        clickOn("#btn_Login");
        clickOn("Menu");
        clickOn("#meb_projects");

    }

    @Test
    public void test02_ProjectWindowInitialization() {

        verifyThat("#txt_ProjectName", isEnabled());
        verifyThat("#txt_ProjectName", isFocused());
        verifyThat("#btn_AddProject", isEnabled());
        verifyThat("#btn_ModifyProject", isDisabled());
        verifyThat("#btn_DeleteProject", isDisabled());

    }

    @Test
    public void test03_EditProjectWindowInitialization() {

        clickOn("#btn_AddProject");
        verifyThat("#txt_EditProjectName", isEnabled());
        verifyThat("#txt_EditProjectName", isFocused());
        verifyThat("#txt_EditProjectDescription", isEnabled());
        verifyThat("#date_EditProjectStartDate", isEnabled());
        verifyThat("#date_EditProjectFinishDate", isEnabled());
        verifyThat("#btn_Save", isDisabled());
        verifyThat("#btn_Cancel", isEnabled());

        clickOn("#txt_EditProjectName");
        write("hola");
        clickOn("#txt_EditProjectDescription");
        write("esta es una descripcion");
        //clickOn(940,450); Mi portatil de Clase
        clickOn(655, 360);
        clickOn("20");
        //clickOn(940,500); Mi portatil de Clase
        clickOn(655, 410); //Mi ordenador de casa
        clickOn("22");
        clickOn("#btn_Save");
        verifyThat("Aceptar", isVisible());
        clickOn("Aceptar");
        clickOn("#btn_Cancel");
        verifyThat("Aceptar", isVisible());
        clickOn("Cancelar");
    }

    @Test
    public void test04_CheckNameAndDescriptionFieldsLength() {
        clickOn("#txt_EditProjectName");
        write("holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        clickOn("#txt_EditProjectDescription");
        write("esta es una descripcionnnnnnnnnnnnnnnn"
                + "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
                + "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
        txt_EditProjectName = lookup("#txt_EditProjectName").query();
        txt_EditProjectDescription = lookup("#txt_EditProjectDescription").query();
        assertTrue(txt_EditProjectName.getText().length() <= 30);
        assertTrue(txt_EditProjectDescription.getText().length() <= 80);
    }

    @Test
    public void test05_CheckStartDateCannotBeAfterFinishDate() {
        //clickOn(940,450); Mi portatil de Clase
        clickOn(655, 360);
        clickOn("21");
        //clickOn(940,500); Mi portatil de Clase
        clickOn(655, 410); //Mi ordenador de casa
        clickOn("19");
        clickOn("Aceptar");
        clickOn("#btn_Cancel");
        clickOn("Aceptar");
    }

    @Test
    public void test06_CheckSearchProject() {

        table = lookup("#projectTable").queryTableView();
        int totalRows = table.getItems().size();
        clickOn("#txt_ProjectName");
        write("hola");
        table = lookup("#projectTable").queryTableView();
        assertThat(table, is(not(TableViewMatchers.hasNumRows(totalRows))));
        clickOn("#txt_ProjectName");
        eraseText(4);
    }

    @Test
    public void test07_CheckCreateProject() {

        table = lookup("#projectTable").queryTableView();
        int totalRows = table.getItems().size();
        clickOn("#btn_AddProject");
        clickOn("#txt_EditProjectName");
        write("hola");
        clickOn("#txt_EditProjectDescription");
        write("esta es una descripcion");
        //clickOn(940,450); Mi portatil de Clase
        clickOn(655, 360);
        clickOn("20");
        //clickOn(940,500); Mi portatil de Clase
        clickOn(655, 410); //Mi ordenador de casa
        clickOn("22");
        clickOn("#btn_Save");
        clickOn("Aceptar");
        clickOn("#btn_Cancel");
        clickOn("Aceptar");
        table = lookup("#projectTable").queryTableView();
        verifyThat(table, TableViewMatchers.hasNumRows(totalRows + 1));
    }

    @Test
    public void test08_CheckDeleteProject() {
        table = lookup("#projectTable").queryTableView();
        int totalRows = table.getItems().size();
        totalRows = table.getItems().size();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn("#btn_DeleteProject");
        clickOn("Aceptar");
        verifyThat(table, TableViewMatchers.hasNumRows(totalRows - 1));
    }

    @Test
    public void test09_CheckModifyProject() {
        table = lookup("#projectTable").queryTableView();
        String name = table.getColumns().get(0).toString();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn("#btn_ModifyProject");
        clickOn("#txt_EditProjectName");
        eraseText(4);
        write("hola que tal");
        clickOn("#btn_Save");
        clickOn("Aceptar");
        table = lookup("#projectTable").queryTableView();
        assertThat(table, is(not(TableViewMatchers.hasTableCell(name))));
    }

}
