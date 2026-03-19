package hitlist.logic.commands;

import static hitlist.testutil.Assert.assertThrows;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.ModelStub;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;

public class DeleteGroupCommandTest {

    @Test
    public void constructor_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeleteGroupCommand(null));
    }

    @Test
    public void execute_existingGroup_deleteSuccessful() throws Exception {
        Group validGroup = new Group(new GroupName("My group"));
        ModelStubWithGroup modelStub = new ModelStubWithGroup(validGroup);

        CommandResult commandResult = new DeleteGroupCommand(validGroup).execute(modelStub);

        assertEquals(String.format(DeleteGroupCommand.MESSAGE_SUCCESS, Messages.formatGroup(validGroup)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validGroup), modelStub.groupsDeleted);
    }

    @Test
    public void execute_groupNotFound_throwsCommandException() {
        Group validGroup = new Group(new GroupName("My group"));
        DeleteGroupCommand deleteGroupCommand = new DeleteGroupCommand(validGroup);
        ModelStub modelStub = new ModelStubWithoutGroup();

        String expectedMessage = String.format(DeleteGroupCommand.MESSAGE_GROUP_NOT_FOUND, validGroup.getName());

        assertThrows(CommandException.class, expectedMessage, () -> deleteGroupCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Group groupA = new Group(new GroupName("Group A"));
        Group groupB = new Group(new GroupName("Group B"));

        DeleteGroupCommand deleteGroupACommand = new DeleteGroupCommand(groupA);
        DeleteGroupCommand deleteGroupBCommand = new DeleteGroupCommand(groupB);

        assertTrue(deleteGroupACommand.equals(deleteGroupACommand));

        DeleteGroupCommand deleteGroupACommandCopy = new DeleteGroupCommand(groupA);
        assertTrue(deleteGroupACommand.equals(deleteGroupACommandCopy));

        assertFalse(deleteGroupACommand.equals(1));
        assertFalse(deleteGroupACommand.equals(null));
        assertFalse(deleteGroupACommand.equals(deleteGroupBCommand));
    }

    @Test
    public void toStringMethod() {
        Group group = new Group(new GroupName("Students"));
        DeleteGroupCommand deleteGroupCommand = new DeleteGroupCommand(group);

        String expected = DeleteGroupCommand.class.getCanonicalName() + "{toDelete=" + group + "}";
        assertEquals(expected, deleteGroupCommand.toString());
    }

    /**
     * A Model stub that contains a single group.
     */
    private class ModelStubWithGroup extends ModelStub {
        private final Group group;
        private final ArrayList<Group> groupsDeleted = new ArrayList<>();

        ModelStubWithGroup(Group group) {
            requireNonNull(group);
            this.group = group;
        }

        @Override
        public boolean hasGroup(Group group) {
            requireNonNull(group);
            return this.group.isSameGroup(group);
        }

        @Override
        public void deleteGroup(Group group) {
            requireNonNull(group);
            groupsDeleted.add(group);
        }
    }

    /**
     * A Model stub that does not contain any group.
     */
    private class ModelStubWithoutGroup extends ModelStub {
        @Override
        public boolean hasGroup(Group group) {
            requireNonNull(group);
            return false;
        }
    }
}
