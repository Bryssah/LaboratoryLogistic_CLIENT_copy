
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import laboratorylogistic_client.FXApplication;
import lazydevelopers.models.Type;
import lazydevelopers.models.Utensil;
import static org.junit.Assert.assertNotEquals;
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

/**
 * This class will do the test for the Utensil controller.
 *
 * @author Garikoitz
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UtensilTest extends ApplicationTest {
    
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
    
    private final String MAX_CHARACTERES = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
            + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
    
    public UtensilTest(){
        
    }

    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(FXApplication.class);
    }

    @Override
    public void stop() {
    }
        
    @Test
    public void test001_login_and_go_UtensilWindow() {
        clickOn("#txt_Login");
        write("garikoitz");
        clickOn("#txt_Password");
        write("1234");
        clickOn("#btn_Login");
        clickOn("Menu");
        clickOn("#meb_utensils");
    }

    @Test
    public void test002_VerifyUtensilInitWindow() {
        verifyThat("#utensilDeleteBtn", isDisabled());
        verifyThat("#utensilAddBtn", isEnabled());
        verifyThat("#utensilNameTxt", isEnabled());
        verifyThat("#utensilNameTxt", isFocused());
        
    }

    @Test
    public void test003_VerifyTableHasData() {
        utensilTableView = lookup("#utensilTableView").queryTableView();
        int rowCount = utensilTableView.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
    }
    
    @Test
    public void test004_NameColumUpdate() {
        updateNameColumnTest("utensilNameColumn");
    }
    
    @Test
    public void test005_TypeColumUpdate() {
        updateTypeColumnTest("utensilTypeColumn");
    }
    
    @Test
    public void test006_AmountColumUpdate() {
        updateAmountColumnTest("utensilAmountColumn");
    }
        
    @Test
    public void test007_testDelete() {
        utensilTableView = lookup("#utensilTableView").queryTableView();
        int totalRows = utensilTableView.getItems().size();
        int rowCount = utensilTableView.getItems().size();
        Node node = lookup("#utensilNameColumn").nth(rowCount).query();
        clickOn(node);
        verifyThat("#utensilDeleteBtn", isEnabled());
        clickOn("#utensilDeleteBtn");
        clickOn("Aceptar");
        verifyThat(utensilTableView, TableViewMatchers.hasNumRows(totalRows-1));
    }

    @Test
    public void test008_testAdd() {
        utensilTableView = lookup("#utensilTableView").queryTableView();
        int totalRows = utensilTableView.getItems().size();
        clickOn("#utensilAddBtn");
        clickOn("Aceptar");
        utensilTableView = lookup("#projectTable").queryTableView();
        updateNameColumnTest("utensilNameColumn");
        verifyThat(utensilTableView, TableViewMatchers.hasNumRows(totalRows+1));
    }
    
    @Test
    public void test009_VerifySearch() {
        utensilTableView = lookup("#utensilTableView").queryTableView();
        int totalRows = utensilTableView.getItems().size();
        clickOn("#utensilNameTxt");
        write("Esto es una busqueda para el test");
        verifyThat(utensilTableView, TableViewMatchers.hasNumRows(totalRows=0));
        eraseText(33);
    }
    
    @Test
    public void test010_VerifySearchMaxCharacteres() {
        clickOn("#utensilNameTxt");
        write(MAX_CHARACTERES);
        utensilNameTxt = lookup("#utensilNameTxt").query(); 
        assertTrue(utensilNameTxt.getText().length() <= 100);
        eraseText(100);
    }
    
    @Test
    public void test011_VerifyOnlyLettersAlertInSearch() {
        clickOn("#utensilNameTxt");
        write("/");
        verifyThat("Aceptar", isVisible());
        clickOn("Aceptar");
        write("a");
        eraseText(1);
    }
    
    @Test
    public void test012_VerifyOnlyLettersAlertInCell() {
        utensilTableView = lookup("#utensilTableView").queryTableView();
        int rowCount = utensilTableView.getItems().size();
        Node node = lookup("#utensilNameColumn").nth(rowCount).query();
        clickOn(node);
        node = lookup("#utensilNameColumn").nth(rowCount).query();
        clickOn(node);
        write("/");
        type(KeyCode.ENTER);
        verifyThat("Aceptar", isVisible());
        clickOn("Aceptar");
    }
    
    public void updateNameColumnTest(String column) {
        utensilTableView = lookup("#utensilTableView").queryTableView();
        int rowCount = utensilTableView.getItems().size();
        Node node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        eraseText(25);
        write("Test value");
        type(KeyCode.ENTER);
        clickOn("Aceptar");
    }
    
    public void updateTypeColumnTest(String column) {
        utensilTableView = lookup("#utensilTableView").queryTableView();
        int rowCount = utensilTableView.getItems().size();
        Node node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        //clickOn(node);
        clickOn("CONTAINER");
        type(KeyCode.ENTER);
    }
    
    public void updateAmountColumnTest(String column) {
        utensilTableView = lookup("#utensilTableView").queryTableView();
        int rowCount = utensilTableView.getItems().size();
        Node node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        eraseText(10);
        write("666");
        type(KeyCode.ENTER);
        clickOn("Aceptar");
    }
}
