package hitlist.logic.commands;

import static hitlist.logic.commands.CommandTestUtil.VALID_GROUP_NAME_STUDENTS;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalPersons.AMY;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.ModelStub;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.person.Name;
import hitlist.model.person.Person;
import hitlist.testutil.PersonBuilder;

public class UnassignGroupCommandTest {

    @Test
    public void constructor_nullPersonName_throwsNullPointerException() {
        GroupName validGroupName = new GroupName(VALID_GROUP_NAME_STUDENTS);
        assertThrows(NullPointerException.class, () -> new UnassignGroupCommand(null, validGroupName));
    }

    @Test
    public void constructor_nullGroupName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UnassignGroupCommand(AMY.getName(), null));
    }

    @Test
    public void execute_validPersonAndGroup_removeSuccessful() throws Exception {
        Group validGroup = new Group(new GroupName(VALID_GROUP_NAME_STUDENTS));
        validGroup.addMember(AMY);

        ModelStubWithPersonAndGroup modelStub = new ModelStubWithPersonAndGroup(AMY, validGroup);
        UnassignGroupCommand command = new UnassignGroupCommand(AMY.getName(), validGroup.getName());

        CommandResult commandResult = command.execute(modelStub);

        assertEquals(String.format(UnassignGroupCommand.MESSAGE_SUCCESS, AMY.getName(), validGroup.getName()),
                commandResult.getFeedbackToUser());
        assertFalse(validGroup.getMembers().contains(AMY));
        assertTrue(modelStub.isUpdateFilteredPersonListCalled());
    }

    @Test
    public void execute_personNotFound_throwsCommandException() {
        Group validGroup = new Group(new GroupName(VALID_GROUP_NAME_STUDENTS));
        ModelStub modelStub = new ModelStubWithoutPerson(validGroup);
        UnassignGroupCommand command = new UnassignGroupCommand(AMY.getName(), validGroup.getName());

        String expectedMessage = String.format(UnassignGroupCommand.MESSAGE_PERSON_NOT_FOUND, AMY.getName());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(modelStub));
    }

    @Test
    public void execute_groupNotFound_throwsCommandException() {
        ModelStub modelStub = new ModelStubWithoutGroup(AMY);
        GroupName missingGroupName = new GroupName(VALID_GROUP_NAME_STUDENTS);
        UnassignGroupCommand command = new UnassignGroupCommand(AMY.getName(), missingGroupName);

        String expectedMessage = String.format(UnassignGroupCommand.MESSAGE_GROUP_NOT_FOUND, missingGroupName);

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonsFound_throwsCommandException() {
        Group validGroup = new Group(new GroupName(VALID_GROUP_NAME_STUDENTS));
        Person duplicateAmy = new PersonBuilder(AMY).withPhone("99998888").build();
        ModelStub modelStub = new ModelStubWithDuplicatePersons(AMY, duplicateAmy, validGroup);
        UnassignGroupCommand command = new UnassignGroupCommand(AMY.getName(), validGroup.getName());

        String expectedMessage = String.format(UnassignGroupCommand.MESSAGE_PERSON_NOT_UNIQUE, AMY.getName());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(modelStub));
    }

    @Test
    public void execute_personNotInGroup_throwsCommandException() {
        Group validGroup = new Group(new GroupName(VALID_GROUP_NAME_STUDENTS));
        ModelStub modelStub = new ModelStubWithPersonAndGroup(AMY, validGroup);
        UnassignGroupCommand command = new UnassignGroupCommand(AMY.getName(), validGroup.getName());

        String expectedMessage = String.format(UnassignGroupCommand.MESSAGE_PERSON_NOT_IN_GROUP,
                AMY.getName(), validGroup.getName());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        Name firstPersonName = new Name("Amy Bee");
        Name secondPersonName = new Name("Bob Choo");
        GroupName firstGroupName = new GroupName("Students");
        GroupName secondGroupName = new GroupName("Unemployed");

        UnassignGroupCommand unassignFirstCommand = new UnassignGroupCommand(firstPersonName, firstGroupName);
        UnassignGroupCommand unassignFirstCommandCopy = new UnassignGroupCommand(firstPersonName, firstGroupName);
        UnassignGroupCommand unassignSecondCommand = new UnassignGroupCommand(secondPersonName, secondGroupName);

        assertTrue(unassignFirstCommand.equals(unassignFirstCommand));
        assertTrue(unassignFirstCommand.equals(unassignFirstCommandCopy));
        assertFalse(unassignFirstCommand.equals(1));
        assertFalse(unassignFirstCommand.equals(null));
        assertFalse(unassignFirstCommand.equals(unassignSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Name personName = new Name("Amy Bee");
        GroupName groupName = new GroupName("Students");
        UnassignGroupCommand unassignGroupCommand = new UnassignGroupCommand(personName, groupName);

        String expected = UnassignGroupCommand.class.getCanonicalName()
                + "{personName=" + personName + ", groupName=" + groupName + "}";

        assertEquals(expected, unassignGroupCommand.toString());
    }

    /**
     * A Model stub that contains one matching person and one existing group.
     */
    private class ModelStubWithPersonAndGroup extends ModelStub {
        private final Person person;
        private final Group group;
        private Predicate<Person> lastPredicate;

        ModelStubWithPersonAndGroup(Person person, Group group) {
            requireNonNull(person);
            requireNonNull(group);
            this.person = person;
            this.group = group;
        }

        @Override
        public List<Person> getPersonsByName(Name name) {
            requireNonNull(name);
            return person.getName().equals(name) ? List.of(person) : List.of();
        }

        @Override
        public Optional<Group> getGroup(GroupName groupName) {
            requireNonNull(groupName);
            return group.getName().equals(groupName) ? Optional.of(group) : Optional.empty();
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            requireNonNull(predicate);
            lastPredicate = predicate;
        }

        boolean isUpdateFilteredPersonListCalled() {
            return lastPredicate != null;
        }
    }

    /**
     * A Model stub with an existing group but no matching person.
     */
    private class ModelStubWithoutPerson extends ModelStub {
        private final Group group;

        ModelStubWithoutPerson(Group group) {
            requireNonNull(group);
            this.group = group;
        }

        @Override
        public List<Person> getPersonsByName(Name name) {
            requireNonNull(name);
            return List.of();
        }

        @Override
        public Optional<Group> getGroup(GroupName groupName) {
            requireNonNull(groupName);
            return group.getName().equals(groupName) ? Optional.of(group) : Optional.empty();
        }
    }

    /**
     * A Model stub with a matching person but no existing group.
     */
    private class ModelStubWithoutGroup extends ModelStub {
        private final Person person;

        ModelStubWithoutGroup(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public List<Person> getPersonsByName(Name name) {
            requireNonNull(name);
            return person.getName().equals(name) ? List.of(person) : List.of();
        }

        @Override
        public Optional<Group> getGroup(GroupName groupName) {
            requireNonNull(groupName);
            return Optional.empty();
        }
    }

    /**
     * A Model stub with multiple persons sharing the same name and one existing group.
     */
    private class ModelStubWithDuplicatePersons extends ModelStub {
        private final Person firstPerson;
        private final Person secondPerson;
        private final Group group;

        ModelStubWithDuplicatePersons(Person firstPerson, Person secondPerson, Group group) {
            requireNonNull(firstPerson);
            requireNonNull(secondPerson);
            requireNonNull(group);
            this.firstPerson = firstPerson;
            this.secondPerson = secondPerson;
            this.group = group;
        }

        @Override
        public List<Person> getPersonsByName(Name name) {
            requireNonNull(name);
            if (firstPerson.getName().equals(name) && secondPerson.getName().equals(name)) {
                return List.of(firstPerson, secondPerson);
            }
            return List.of();
        }

        @Override
        public Optional<Group> getGroup(GroupName groupName) {
            requireNonNull(groupName);
            return group.getName().equals(groupName) ? Optional.of(group) : Optional.empty();
        }
    }
}
