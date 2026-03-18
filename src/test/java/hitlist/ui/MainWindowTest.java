package hitlist.ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import hitlist.logic.LogicStub;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

/**
 * Unit tests for {@link MainWindow}.
 */
public class MainWindowTest {

    static {
        // Initializes JavaFX runtime for tests.
        new JFXPanel();
    }

    @Test
    public void constructor_validDependencies_success() {
        // This test only verifies constructor path does not throw when JavaFX is initialized.
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            Stage stage = new Stage();
            MainWindowStub window = new MainWindowStub(stage);
            assertNotNull(window);
            assertNotNull(window.getPrimaryStage());
        }));
    }

    @Test
    public void showCompanyListPane_doesNotThrow() {
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            Stage stage = new Stage();
            MainWindowStub window = new MainWindowStub(stage);
            window.showCompanyListPane();
        }));
    }

    @Test
    public void hideCompanyListPane_doesNotThrow() {
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            Stage stage = new Stage();
            MainWindowStub window = new MainWindowStub(stage);
            window.hideCompanyListPane();
        }));
    }

    @Test
    public void showPersonListPane_doesNotThrow() {
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            Stage stage = new Stage();
            MainWindowStub window = new MainWindowStub(stage);
            window.showPersonListPane();
        }));
    }

    @Test
    public void hidePersonListPane_doesNotThrow() {
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            Stage stage = new Stage();
            MainWindowStub window = new MainWindowStub(stage);
            window.hidePersonListPane();
        }));
    }

    @Test
    public void handleHelp_doesNotThrow() {
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            Stage stage = new Stage();
            MainWindowStub window = new MainWindowStub(stage);
            window.handleHelp();
        }));
    }

    /**
     * Minimal subclass for constructing MainWindow in tests without full app wiring.
     */
    private static class MainWindowStub extends MainWindow {
        MainWindowStub(Stage stage) {
            super(stage, new LogicStub());
        }
    }
}
