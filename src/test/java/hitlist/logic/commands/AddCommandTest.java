package hitlist.logic.commands;

import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalPersons.ALICE;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.HitList;
import hitlist.model.ModelStub;
import hitlist.model.ReadOnlyHitList;
import hitlist.model.person.Name;
import hitlist.model.person.Person;
import hitlist.model.person.Phone;
import hitlist.testutil.PersonBuilder;
import javafx.collections.FXCollections;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(getExpectedSuccessMessage(validPerson), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_minimalPersonAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person minimalPerson = new Person(
                new Name("John Doe"),
                new Phone("98765432"),
                Optional.empty(),
                Optional.empty());

        CommandResult commandResult = new AddCommand(minimalPerson).execute(modelStub);

        assertEquals(getExpectedSuccessMessage(minimalPerson), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(minimalPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePersonSameNameAndPhone_throwsCommandException() {
        Person duplicatePerson = new PersonBuilder(ALICE).build();
        AddCommand addCommand = new AddCommand(duplicatePerson);
        ModelStub modelStub = new ModelStubWithPerson(duplicatePerson);

        String expectedMessage = String.format(
                AddCommand.MESSAGE_DUPLICATE_NAME,
                duplicatePerson.getName());

        assertThrows(CommandException.class, expectedMessage, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonSameNameOnly_throwsCommandException() {
        Person existingPerson = new PersonBuilder(ALICE).withPhone("12345678").build();
        Person newPersonWithSameName = new PersonBuilder(ALICE).withPhone("87654321").build();

        AddCommand addCommand = new AddCommand(newPersonWithSameName);
        ModelStub modelStub = new ModelStubWithPerson(existingPerson);

        String expectedMessage = String.format(
                AddCommand.MESSAGE_DUPLICATE_NAME,
                newPersonWithSameName.getName());

        assertThrows(CommandException.class, expectedMessage, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonSamePhoneOnly_success() throws Exception {
        Person existingPerson = new PersonBuilder(ALICE).withName("Alice Wong").build();
        Person validPerson = new PersonBuilder()
                .withName("Bob")
                .withPhone(existingPerson.getPhone().value)
                .build();

        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        modelStub.addPerson(existingPerson);

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(getExpectedSuccessMessage(validPerson), commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person duplicatePerson = new PersonBuilder(ALICE).build();
        AddCommand addCommand = new AddCommand(duplicatePerson);
        ModelStub modelStub = new ModelStubWithPerson(duplicatePerson);

        String expectedMessage = String.format(
                AddCommand.MESSAGE_DUPLICATE_NAME, duplicatePerson.getName());

        assertThrows(CommandException.class, expectedMessage, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();

        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        assertTrue(addAliceCommand.equals(addAliceCommand));
        assertTrue(addAliceCommand.equals(new AddCommand(alice)));
        assertFalse(addAliceCommand.equals(1));
        assertFalse(addAliceCommand.equals(null));
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    private static String getExpectedSuccessMessage(Person person) {
        StringBuilder expectedMessage = new StringBuilder(AddCommand.MESSAGE_SUCCESS)
                .append(person.getName())
                .append(" | Phone: ")
                .append(person.getPhone());

        person.getEmail().ifPresent(email -> expectedMessage.append(" | Email: ").append(email));
        person.getAddress().ifPresent(address -> expectedMessage.append(" | Address: ").append(address));

        return expectedMessage.toString();
    }

    /**
     * A Model stub that contains a person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }

        @Override
        public javafx.collections.ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyHitList getHitList() {
            return new HitList();
        }

        @Override
        public javafx.collections.ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(personsAdded);
        }
    }
}
