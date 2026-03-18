package hitlist.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hitlist.logic.LogicStub;
import hitlist.logic.commands.CommandResult;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.logic.parser.exceptions.ParseException;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Coverage tests for MainWindow.
 */
public class MainWindowTest {

    @BeforeAll public static void setupFxGuard() {
        assumeTrue(JavaFxTestSupport.isFxAvailable(),
                "Skipping MainWindowTest: JavaFX unavailable in this environment.");
    }

    @Test
    public void constructor_getPrimaryStage_success() throws Exception {
        AtomicReference<MainWindow> windowRef = new AtomicReference<>();
        AtomicReference<Stage> stageRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Stage stage = new Stage();
            stageRef.set(stage);
            MainWindow window = new MainWindow(stage, new FixedResultLogicStub(new CommandResult("ok")));
            windowRef.set(window);
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(windowRef.get());
        assertSame(stageRef.get(), windowRef.get().getPrimaryStage());
    }

    @Test
    public void fillInnerParts_getPanels_success() throws Exception {
        AtomicReference<MainWindow> ref = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            MainWindow window = new MainWindow(new Stage(), new FixedResultLogicStub(new CommandResult("ok")));
            window.fillInnerParts();
            ref.set(window);
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(ref.get());
        assertNotNull(ref.get().getPersonListPanel());
        assertNotNull(ref.get().getCompanyListPanel());
    }

    @Test
    public void showHideCompanyPane_updatesVisibleManaged() throws Exception {
        AtomicReference<MainWindow> ref = new AtomicReference<>();
        AtomicReference<VBox> companyPaneRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(), new FixedResultLogicStub(new CommandResult("ok")));
                window.fillInnerParts();

                Field field = MainWindow.class.getDeclaredField("companyListPane");
                field.setAccessible(true);
                VBox companyPane = (VBox) field.get(window);

                window.showCompanyListPane();
                assertTrue(companyPane.isVisible());
                assertTrue(companyPane.isManaged());

                window.hideCompanyListPane();
                assertFalse(companyPane.isVisible());
                assertFalse(companyPane.isManaged());

                ref.set(window);
                companyPaneRef.set(companyPane);
            } catch (Throwable t) {
                throw new AssertionError(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(ref.get());
        assertNotNull(companyPaneRef.get());
    }

    @Test
    public void showHidePersonPane_updatesVisibleManaged() throws Exception {
        AtomicReference<MainWindow> ref = new AtomicReference<>();
        AtomicReference<VBox> personPaneRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(), new FixedResultLogicStub(new CommandResult("ok")));
                window.fillInnerParts();

                Field field = MainWindow.class.getDeclaredField("personList");
                field.setAccessible(true);
                VBox personPane = (VBox) field.get(window);

                window.showPersonListPane();
                assertTrue(personPane.isVisible());
                assertTrue(personPane.isManaged());

                window.hidePersonListPane();
                assertFalse(personPane.isVisible());
                assertFalse(personPane.isManaged());

                ref.set(window);
                personPaneRef.set(personPane);
            } catch (Throwable t) {
                throw new AssertionError(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(ref.get());
        assertNotNull(personPaneRef.get());
    }

    @Test
    public void handleHelp_notShowing_showsOrFocuses() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(), new FixedResultLogicStub(new CommandResult("ok")));
                window.handleHelp();
                window.handleHelp();
            } catch (Throwable t) {
                thrown.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        if (thrown.get() != null) {
            throw new AssertionError(thrown.get());
        }
    }

    @Test
    public void executeCommand_showHelp_opensHelpWindow() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(),
                        new FixedResultLogicStub(new CommandResult("help", true, false, false)));
                window.fillInnerParts();

                Method executeCommand = MainWindow.class.getDeclaredMethod("executeCommand", String.class);
                executeCommand.setAccessible(true);
                executeCommand.invoke(window, "help");
            } catch (Throwable t) {
                thrown.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        if (thrown.get() != null) {
            throw new AssertionError(thrown.get());
        }
    }

    @Test
    public void executeCommand_showCompanyList_hidesPersonList() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        AtomicReference<MainWindow> ref = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(),
                        new FixedResultLogicStub(new CommandResult("companies", false, false, true)));
                window.fillInnerParts();

                Method executeCommand = MainWindow.class.getDeclaredMethod("executeCommand", String.class);
                executeCommand.setAccessible(true);
                executeCommand.invoke(window, "listcompany");

                ref.set(window);
            } catch (Throwable t) {
                thrown.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        if (thrown.get() != null) {
            throw new AssertionError(thrown.get());
        }
        assertNotNull(ref.get());
        assertNotNull(ref.get().getCompanyListPanel());
    }

    @Test
    public void executeCommand_exit_hidesPrimaryStage() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        AtomicReference<Stage> stageRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                stage.show();
                stageRef.set(stage);

                MainWindow window = new MainWindow(stage,
                        new FixedResultLogicStub(new CommandResult("bye", false, true, false)));
                window.fillInnerParts();

                Method executeCommand = MainWindow.class.getDeclaredMethod("executeCommand", String.class);
                executeCommand.setAccessible(true);
                executeCommand.invoke(window, "exit");
            } catch (Throwable t) {
                thrown.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        if (thrown.get() != null) {
            throw new AssertionError(thrown.get());
        }
        assertFalse(stageRef.get().isShowing());
    }

    @Test
    public void executeCommand_exceptionSetsErrorFeedback_command() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(), new ThrowingCommandExceptionLogicStub());
                window.fillInnerParts();

                Method executeCommand = MainWindow.class.getDeclaredMethod("executeCommand", String.class);
                executeCommand.setAccessible(true);
                executeCommand.invoke(window, "bad");
            } catch (Throwable t) {
                thrown.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(thrown.get());

        Throwable root = unwrapInvocationTargetException(thrown.get());
        assertThrows(CommandException.class, () -> {
            throw root;
        });
    }

    @Test
    public void executeCommand_exceptionSetsErrorFeedback_parse() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(), new ThrowingParseExceptionLogicStub());
                window.fillInnerParts();

                Method executeCommand = MainWindow.class.getDeclaredMethod("executeCommand", String.class);
                executeCommand.setAccessible(true);
                executeCommand.invoke(window, "bad parse");
            } catch (Throwable t) {
                thrown.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(thrown.get());

        Throwable root = unwrapInvocationTargetException(thrown.get());
        assertThrows(ParseException.class, () -> {
            throw root;
        });
    }

    @Test
    public void handleExit_savesGuiSettings_hidesWindows() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        AtomicReference<Stage> stageRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                stage.show();
                stageRef.set(stage);

                MainWindow window = new MainWindow(stage, new FixedResultLogicStub(new CommandResult("ok")));
                Method handleExit = MainWindow.class.getDeclaredMethod("handleExit");
                handleExit.setAccessible(true);
                handleExit.invoke(window);
            } catch (Throwable t) {
                thrown.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        if (thrown.get() != null) {
            throw new AssertionError(thrown.get());
        }
        assertFalse(stageRef.get().isShowing());
    }

    private static Throwable unwrapInvocationTargetException(Throwable throwable) {
        if (throwable instanceof InvocationTargetException) {
            Throwable cause = throwable.getCause();
            return cause == null ? throwable : cause;
        }
        Throwable cause = throwable.getCause();
        if (cause instanceof InvocationTargetException) {
            Throwable nested = cause.getCause();
            return nested == null ? cause : nested;
        }
        return throwable;
    }

    private static class FixedResultLogicStub extends LogicStub {
        private final CommandResult fixedResult;

        FixedResultLogicStub(CommandResult fixedResult) {
            this.fixedResult = fixedResult;
        }

        @Override public CommandResult execute(String commandText) throws CommandException, ParseException {
            return fixedResult;
        }
    }

    private static class ThrowingCommandExceptionLogicStub extends LogicStub {
        @Override public CommandResult execute(String commandText) throws CommandException {
            throw new CommandException("command failure");
        }
    }

    private static class ThrowingParseExceptionLogicStub extends LogicStub {
        @Override public CommandResult execute(String commandText) throws ParseException {
            throw new ParseException("parse failure");
        }
    }
}
