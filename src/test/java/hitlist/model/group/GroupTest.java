package hitlist.model.group;

import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalGroups.STUDENTS;
import static hitlist.testutil.TypicalGroups.UNEMPLOYED;
import static hitlist.testutil.TypicalPersons.ALICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import hitlist.model.person.exceptions.DuplicatePersonException;

public class GroupTest {
    @Test
    public void hasMember_emptyGroup_returnsFalse() {
        Group group = new Group(new GroupName("MyGroup"));

        assertFalse(group.hasMember(ALICE));
    }

    @Test
    public void hasMember_memberInGroup_returnsFalse() {
        Group group = new Group(new GroupName("MyGroup"));
        group.addMember(ALICE);

        assertTrue(group.hasMember(ALICE));
    }

    @Test
    public void addMember_emptyGroup_success() {
        Group group = new Group(new GroupName("MyGroup"));
        group.addMember(ALICE);
    }

    @Test
    public void addMember_memberAlreadyInGroup_throwsDuplicatePersonException() {
        Group group = new Group(new GroupName("MyGroup"));
        group.addMember(ALICE);

        assertThrows(DuplicatePersonException.class, () -> group.addMember(ALICE));
    }

    @Test
    public void isSameGroup() {
        // same object -> returns true
        assertTrue(STUDENTS.isSameGroup(STUDENTS));

        // null -> returns false
        assertFalse(STUDENTS.isSameGroup(null));

        // name differs in case -> returns false
        Group groupA = new Group(new GroupName("test"));
        Group groupB = new Group(new GroupName("Test"));
        assertFalse(groupA.isSameGroup(groupB));
    }

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(STUDENTS.equals(STUDENTS));

        // null -> returns false
        assertFalse(STUDENTS.equals(null));

        // different type -> returns false
        assertFalse(STUDENTS.equals(5));

        // different person -> returns false
        assertFalse(STUDENTS.equals(UNEMPLOYED));
    }

    @Test
    public void toStringMethod() {
        String expected = Group.class.getCanonicalName() + "{name=" + STUDENTS.getName() + "}";
        assertEquals(expected, STUDENTS.toString());
    }
}
