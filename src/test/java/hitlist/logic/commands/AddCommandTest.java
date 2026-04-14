package hitlist.logic.commands;

import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalPersons.ALICE;
import static hitlist.testutil.TypicalPersons.BOB;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.HitList;
import hitlist.model.ModelStub;
import hitlist.model.ReadOnlyHitList;
import hitlist.model.person.Person;
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

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPerson.getName(), validPerson.getPhone()),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePersonSameNameAndPhone_throwsCommandException() {
        Person duplicatePerson = new PersonBuilder(ALICE).build();
        AddCommand addCommand = new AddCommand(duplicatePerson);
        ModelStub modelStub = new ModelStubWithPerson(duplicatePerson);

        String expectedMessage = String.format(
                AddCommand.MESSAGE_DUPLICATE_NAME,
                duplicatePerson.getName(),
                duplicatePerson.getPhone());

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
    public void execute_duplicatePersonSamePhoneOnly_throwsCommandException() {
        Person existingPerson = new PersonBuilder(ALICE).withName("Alice Wong").build();
        Person duplicatePhonePerson = new PersonBuilder()
                .withName("Bob").withPhone(existingPerson.getPhone().value).build();

        ModelStub modelStub = new ModelStubAcceptingPersonAdded();
        modelStub.addPerson(existingPerson);

        AddCommand addCommand = new AddCommand(duplicatePhonePerson);

        assertThrows(CommandException.class,
                String.format(AddCommand.MESSAGE_DUPLICATE_PHONE, duplicatePhonePerson.getPhone()), ()
                -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentContactsNameAndPhone_throwsCommandException() {
        // Create two different existing persons
        Person existingPerson1 = new PersonBuilder(ALICE).withPhone("11111111").build(); // Same name as new person
        Person existingPerson2 = new PersonBuilder(BOB).withName("Charlie").withPhone("22222222").build();

        // New person has name matching existingPerson1 and phone matching existingPerson2
        Person newPerson = new PersonBuilder(ALICE).withPhone("22222222").build();

        AddCommand addCommand = new AddCommand(newPerson);
        ModelStub modelStub = new ModelStubWithMultiplePersons(existingPerson1, existingPerson2);

        String expectedMessage = String.format(
                AddCommand.MESSAGE_DUPLICATE_NAME,
                newPerson.getName(),
                newPerson.getPhone());

        assertThrows(CommandException.class, expectedMessage, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A Model stub that contains a single person.
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
     * A Model stub that contains multiple persons.
     */
    private class ModelStubWithMultiplePersons extends ModelStub {
        private final ArrayList<Person> persons = new ArrayList<>();

        ModelStubWithMultiplePersons(Person... persons) {
            requireNonNull(persons);
            this.persons.addAll(Arrays.asList(persons));
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return persons.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public javafx.collections.ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(persons);
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
