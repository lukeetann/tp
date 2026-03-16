package hitlist.logic.commands;

import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalGroups.STUDENTS;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.HitList;
import hitlist.model.ModelStub;
import hitlist.model.ReadOnlyHitList;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;

public class AddGroupCommandTest {

    @Test
    public void constructor_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddGroupCommand(null));
    }

    @Test
    public void execute_groupAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingGroupAdded modelStub = new ModelStubAcceptingGroupAdded();
        Group validGroup = new Group(new GroupName("My group"));

        CommandResult commandResult = new AddGroupCommand(validGroup).execute(modelStub);

        assertEquals(String.format(AddGroupCommand.MESSAGE_SUCCESS, Messages.formatGroup(validGroup)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validGroup), modelStub.groupsAdded);
    }

    @Test
    public void execute_duplicateGroup_throwsCommandException() {
        Group validGroup = new Group(new GroupName("My group"));
        AddGroupCommand addGroupCommand = new AddGroupCommand(validGroup);
        ModelStub modelStub = new ModelStubWithGroup(validGroup);

        assertThrows(CommandException.class,
                     AddGroupCommand.MESSAGE_DUPLICATE_GROUP, () -> addGroupCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Group groupA = new Group(new GroupName("Group A"));
        Group groupB = new Group(new GroupName("Group B"));
        AddGroupCommand addGroupACommand = new AddGroupCommand(groupA);
        AddGroupCommand addGroupBCommand = new AddGroupCommand(groupB);

        // same object -> returns true
        assertTrue(addGroupACommand.equals(addGroupACommand));

        // same values -> returns true
        AddGroupCommand addGroupACommandCopy = new AddGroupCommand(groupA);
        assertTrue(addGroupACommand.equals(addGroupACommandCopy));

        // different types -> returns false
        assertFalse(addGroupACommand.equals(1));

        // null -> returns false
        assertFalse(addGroupACommand.equals(null));

        // different content -> returns false
        assertFalse(addGroupACommand.equals(addGroupBCommand));
    }

    @Test
    public void toStringMethod() {
        AddGroupCommand addGroupCommand = new AddGroupCommand(STUDENTS);
        String expected = AddGroupCommand.class.getCanonicalName() + "{toAdd=" + STUDENTS + "}";
        assertEquals(expected, addGroupCommand.toString());
    }

    /**
     * A Model stub that contains a single group.
     */
    private class ModelStubWithGroup extends ModelStub {
        private final Group group;

        ModelStubWithGroup(Group group) {
            requireNonNull(group);
            this.group = group;
        }

        @Override
        public boolean hasGroup(Group group) {
            requireNonNull(group);
            return this.group.isSameGroup(group);
        }
    }

    /**
     * A Model stub that always accept the group being added.
     */
    private class ModelStubAcceptingGroupAdded extends ModelStub {
        final ArrayList<Group> groupsAdded = new ArrayList<>();

        @Override
        public boolean hasGroup(Group group) {
            requireNonNull(group);
            return groupsAdded.stream().anyMatch(group::isSameGroup);
        }

        @Override
        public void addGroup(Group group) {
            requireNonNull(group);
            groupsAdded.add(group);
        }

        @Override
        public ReadOnlyHitList getHitList() {
            return new HitList();
        }
    }
}
