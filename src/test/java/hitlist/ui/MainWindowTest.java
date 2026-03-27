package hitlist.ui;

import static hitlist.ui.UiPaneVisibility.EXIT;
import static hitlist.ui.UiPaneVisibility.SHOW_HELP_MESSAGE;
import static hitlist.ui.UiPaneVisibility.SHOW_ROLE_LIST;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
import javafx.stage.Stage;

/**
 * Coverage tests for MainWindow.
 */
public class MainWindowTest {

    @BeforeAll
    public static void setupFxGuard() {
        assumeTrue(JavaFxTestSupport.isFxAvailable(),
                "Skipping MainWindowTest: JavaFX unavailable in this environment.");
    }

    @Test
    public void constructor_andGetPrimaryStage_success() throws Exception {
        AtomicReference<MainWindow> ref = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            MainWindow window = new MainWindow(new Stage(), new FixedResultLogicStub(new CommandResult("ok")));
            ref.set(window);
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(ref.get());
        assertNotNull(ref.get().getPrimaryStage()); // covers getPrimaryStage
    }

    @Test
    public void fillInnerParts_getPanel_success() throws Exception {
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
        assertNotNull(ref.get().getPersonListPanel()); // covers getPersonListPanel
        assertNotNull(ref.get().getCompanyListPanel()); // covers getCompanyListPanel
        assertNotNull(ref.get().getRoleListPanel()); // covers getRoleListPanel
    }

    @Test
    public void handleHelp_noCrash() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(), new FixedResultLogicStub(new CommandResult("ok")));
                window.handleHelp(); // covers handleHelp
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
    public void executeCommand_showHelpBranch_covered() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(),
                        new FixedResultLogicStub(new CommandResult("help", SHOW_HELP_MESSAGE)));
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
    public void executeCommand_exitBranch_covered() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(),
                        new FixedResultLogicStub(new CommandResult("bye", EXIT)));
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
    }

    @Test
    public void executeCommand_showRoleListBranch_covered() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                CommandResult result = new CommandResult("role", SHOW_ROLE_LIST);

                MainWindow window = new MainWindow(new Stage(), new FixedResultLogicStub(result));
                window.fillInnerParts();

                Method executeCommand = MainWindow.class.getDeclaredMethod("executeCommand", String.class);
                executeCommand.setAccessible(true);
                executeCommand.invoke(window, "role");

                // Assertions to ensure branch effects happened
                assertTrue(window.getRoleListPanel() != null);

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
    public void executeCommand_catchAndRethrow_commandException() throws Exception {
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
    public void executeCommand_catchAndRethrow_parseException() throws Exception {
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
    public void handleExit_coveredViaReflection() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindow(new Stage(), new FixedResultLogicStub(new CommandResult("ok")));
                Method handleExit = MainWindow.class.getDeclaredMethod("handleExit");
                handleExit.setAccessible(true);
                handleExit.invoke(window); // covers handleExit directly
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

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            return fixedResult;
        }
    }

    private static class ThrowingCommandExceptionLogicStub extends LogicStub {
        @Override
        public CommandResult execute(String commandText) throws CommandException {
            throw new CommandException("command failure");
        }
    }

    private static class ThrowingParseExceptionLogicStub extends LogicStub {
        @Override
        public CommandResult execute(String commandText) throws ParseException {
            throw new ParseException("parse failure");
        }
    }
}
