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
        assertThrows(IllegalArgumentException.class, () -> new RoleName(""));
        assertThrows(IllegalArgumentException.class, () -> new RoleName(" "));
        assertThrows(IllegalArgumentException.class, () -> new RoleName("/"));
    }

    @Test
    public void isValidRoleName() {
        assertThrows(AssertionError.class, () -> RoleName.isValidRoleName(null));

        assertFalse(RoleName.isValidRoleName(""));
        assertFalse(RoleName.isValidRoleName(" "));
        assertFalse(RoleName.isValidRoleName("/"));
        assertFalse(RoleName.isValidRoleName("Role/Name"));
        assertFalse(RoleName.isValidRoleName("Role\nName"));

        assertTrue(RoleName.isValidRoleName("Software Engineer"));
        assertTrue(RoleName.isValidRoleName("12345"));
        assertTrue(RoleName.isValidRoleName("Software Engineer I"));
        assertTrue(RoleName.isValidRoleName("Software Engineer II"));
        assertTrue(RoleName.isValidRoleName("SE I"));
        assertTrue(RoleName.isValidRoleName("A".repeat(50)));
    }

    @Test
    public void toString_validName_returnsName() {
        RoleName name = new RoleName("Valid Role Name");
        assertEquals("Valid Role Name", name.toString());
    }

    @Test
    public void equals() {
        RoleName name = new RoleName("Valid Role Name");

        assertTrue(name.equals(new RoleName("Valid Role Name")));
        assertTrue(name.equals(name));
        assertFalse(name.equals(null));
        assertFalse(name.equals(5.0f));
        assertFalse(name.equals(new RoleName("Other Valid Role Name")));
    }

    @Test
    public void hashCodeTest() {
        RoleName name = new RoleName("Valid Role Name");
        RoleName sameName = new RoleName("Valid Role Name");
        RoleName differentName = new RoleName("Other Valid Role Name");

        assertEquals(name.hashCode(), sameName.hashCode());
        assertNotEquals(name.hashCode(), differentName.hashCode());
    }
}
