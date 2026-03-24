package hitlist.logic.commands;

import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalGroups.STUDENTS;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import hitlist.model.ModelStub;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.person.Person;


public class ListGroupCommandTest {

    @Test
    public void constructor_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ListGroupCommand(null));
    }

    @Test
    public void constructor_noArguments_success() {
        new ListGroupCommand();
    }

    @Test
    public void execute_validGroupName_successful() throws Exception {
        Group validGroup = new Group(new GroupName("My group"));

        ModelStub modelStub = new ModelStubWithGroup(validGroup);
        CommandResult commandResult = new ListGroupCommand(validGroup.getName()).execute(modelStub);

        String expectedMessage =
            String.format(ListGroupCommand.MESSAGE_SUCCESS_1, validGroup.getName());

        assertEquals(expectedMessage,
                     commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_listAll_successful() throws Exception {
        Group validGroup = new Group(new GroupName("My group"));

        ModelStub modelStub = new ModelStubWithGroup(validGroup);
        CommandResult commandResult = new ListGroupCommand().execute(modelStub);

        String expectedMessage = ListGroupCommand.MESSAGE_SUCCESS_2;

        assertEquals(expectedMessage,
                     commandResult.getFeedbackToUser());
    }

    @Test
    public void equals() {
        GroupName groupNameA = new GroupName("Group A");
        GroupName groupNameB = new GroupName("Group B");
        ListGroupCommand listGroupACommand = new ListGroupCommand(groupNameA);
        ListGroupCommand listGroupBCommand = new ListGroupCommand(groupNameB);

        // same object -> returns true
        assertTrue(listGroupACommand.equals(listGroupACommand));

        // same values -> returns true
        ListGroupCommand listGroupACommandCopy = new ListGroupCommand(groupNameA);
        assertTrue(listGroupACommand.equals(listGroupACommandCopy));

        // different types -> returns false
        assertFalse(listGroupACommand.equals(1));

        // null -> returns false
        assertFalse(listGroupACommand.equals(null));

        // different content -> returns false
        assertFalse(listGroupACommand.equals(listGroupBCommand));

        ListGroupCommand listAllCommandA = new ListGroupCommand();
        ListGroupCommand listAllCommandB = new ListGroupCommand();

        // same object -> returns true
        assertTrue(listAllCommandA.equals(listAllCommandA));

        // same values (zero-argument case) -> returns true
        assertTrue(listAllCommandA.equals(listAllCommandB));
    }

    @Test
    public void toStringMethod() {
        ListGroupCommand listGroupCommand = new ListGroupCommand(STUDENTS.getName());
        String expected = ListGroupCommand.class.getCanonicalName() + "{toList=" + STUDENTS.getName() + "}";
        assertEquals(expected, listGroupCommand.toString());
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

        @Override
        public Optional<Group> getGroup(GroupName groupName) {
            requireNonNull(groupName);

            return group.getName().equals(groupName)
                ? Optional.of(group)
                : Optional.empty();
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            return;
        }
    }
}
