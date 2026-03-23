package hitlist.model.company.role;

import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RoleNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RoleName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new RoleName(invalidName));
    }

    @Test
    public void isValidRoleName() {
        // invalid name
        assertFalse(RoleName.isValidRoleName("")); // empty string
        assertFalse(RoleName.isValidRoleName(" ")); // spaces only
        assertFalse(RoleName.isValidRoleName("/")); // forward slash only
        assertFalse(RoleName.isValidRoleName("Role/Name")); // contains forward slash
        assertFalse(RoleName.isValidRoleName("Role\nName")); // contains line break

        // valid name
        assertTrue(RoleName.isValidRoleName("Software Engineer")); // alphabets only
        assertTrue(RoleName.isValidRoleName("12345")); // numbers only
        assertTrue(RoleName.isValidRoleName("Software Engineer I")); // alphanumeric characters and punctuation
        assertTrue(RoleName.isValidRoleName("Software Engineer II")); // with capital letters
        assertTrue(RoleName.isValidRoleName("SE I")); // short name
        assertTrue(RoleName.isValidRoleName("Software Engineer I Software E")); // long name (30 characters)
    }

    @Test
    public void toString_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RoleName(null).toString());
    }

    @Test
    public void toString_validName_returnsName() {
        RoleName name = new RoleName("Valid Role Name");
        assertEquals("Valid Role Name", name.toString());
    }

    @Test
    public void equals() {
        RoleName name = new RoleName("Valid Role Name");

        // same values -> returns true
        assertTrue(name.equals(new RoleName("Valid Role Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new RoleName("Other Valid Role Name")));
    }

    @Test
    public void hashCodeTest() {
        RoleName name = new RoleName("Valid Role Name");
        RoleName sameName = new RoleName("Valid Role Name");
        RoleName differentName = new RoleName("Other Valid Role Name");

        // same values -> same hash code
        assertEquals(name.hashCode(), sameName.hashCode());

        // different values -> different hash code
        assertNotEquals(name.hashCode(), differentName.hashCode());
    }
}
