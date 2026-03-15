package hitlist.model.person;

import static hitlist.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalPersons.ALICE;
import static hitlist.testutil.TypicalPersons.BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import hitlist.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns true
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns true
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns true
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // same person but with empty email -> returns false
        editedAlice = new Person(ALICE.getName(), ALICE.getPhone(), Optional.empty(),
                ALICE.getAddress(), ALICE.getTags());
        assertFalse(ALICE.equals(editedAlice));

        // same person but with empty address -> returns false
        editedAlice = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                Optional.empty(), ALICE.getTags());
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void constructor_withEmptyOptionals() {
        // Test that Person can be created with empty email and address
        Person person = new Person(ALICE.getName(), ALICE.getPhone(), Optional.empty(),
                Optional.empty(), ALICE.getTags());
        assertTrue(person.getEmail().isEmpty());
        assertTrue(person.getAddress().isEmpty());
        assertEquals(ALICE.getName(), person.getName());
        assertEquals(ALICE.getPhone(), person.getPhone());
        assertEquals(ALICE.getTags(), person.getTags());
    }

    @Test
    public void equals_withEmptyOptionals() {
        // Two persons with same fields but both have empty email and address -> returns true
        Person person1 = new Person(ALICE.getName(), ALICE.getPhone(), Optional.empty(),
                Optional.empty(), ALICE.getTags());
        Person person2 = new Person(ALICE.getName(), ALICE.getPhone(), Optional.empty(),
                Optional.empty(), ALICE.getTags());
        assertTrue(person1.equals(person2));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress() + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
