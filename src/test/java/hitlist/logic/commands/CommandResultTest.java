package hitlist.logic.commands;

import static hitlist.ui.UiPaneVisibility.EXIT;
import static hitlist.ui.UiPaneVisibility.SHOW_HELP_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CommandResultTest {
    @Test
    public void equals() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns true
        assertTrue(commandResult.equals(new CommandResult("feedback")));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(new CommandResult("different")));

        // different UiPaneVisibility value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", EXIT)));

        // different UiPaneVisibility value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", SHOW_HELP_MESSAGE)));
    }

    @Test
    public void hashcode() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(), new CommandResult("feedback").hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("different").hashCode());

        // different UiPaneVisibility value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", EXIT).hashCode());

        // different UiPaneVisibility value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", SHOW_HELP_MESSAGE).hashCode());
    }

    @Test
    public void toStringMethod() {
        CommandResult commandResult = new CommandResult("feedback");
        String expected = CommandResult.class.getCanonicalName()
            + "{feedbackToUser=" + commandResult.getFeedbackToUser()
            + ", showHelp=" + commandResult.isShowHelp()
            + ", exit=" + commandResult.isExit()
            + ", showCompanyList=" + commandResult.isShowCompanyList()
            + ", showGroupList=" + commandResult.isShowGroupList()
            + ", showRoleList=" + commandResult.isShowRoleList() + "}";
        assertEquals(expected, commandResult.toString());
    }
}
