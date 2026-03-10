package hitlist.model.group;

import static hitlist.testutil.TypicalGroups.STUDENTS;
import static hitlist.testutil.TypicalGroups.UNEMPLOYED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GroupTest {

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
