package hitlist.model.company.role;

import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RoleDescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RoleDescription(null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new RoleDescription(""));
        assertThrows(IllegalArgumentException.class, () -> new RoleDescription(" "));
        assertThrows(IllegalArgumentException.class, () -> new RoleDescription("/"));
    }

    @Test
    public void isValidRoleDescription() {
        assertThrows(NullPointerException.class, () -> RoleDescription.isValidRoleDescription(null));

        assertFalse(RoleDescription.isValidRoleDescription(""));
        assertFalse(RoleDescription.isValidRoleDescription(" "));
        assertFalse(RoleDescription.isValidRoleDescription("/"));
        assertFalse(RoleDescription.isValidRoleDescription("Role/Description"));
        assertFalse(RoleDescription.isValidRoleDescription("Role\nDescription"));

        assertTrue(RoleDescription.isValidRoleDescription("Software Engineer"));
        assertTrue(RoleDescription.isValidRoleDescription("12345"));
        assertTrue(RoleDescription.isValidRoleDescription("A".repeat(999)));
    }

    @Test
    public void constructor_trimsWhitespace_success() {
        RoleDescription description = new RoleDescription("  Valid Role Description  ");
        assertEquals("Valid Role Description", description.toString());
    }

    @Test
    public void toString_validDescription_returnsDescription() {
        RoleDescription description = new RoleDescription("Valid Role Description");
        assertEquals("Valid Role Description", description.toString());
    }

    @Test
    public void equals() {
        RoleDescription description = new RoleDescription("Valid Role Description");

        assertTrue(description.equals(new RoleDescription("Valid Role Description")));
        assertTrue(description.equals(description));
        assertFalse(description.equals(null));
        assertFalse(description.equals(5.0f));
        assertFalse(description.equals(new RoleDescription("Other Valid Role Description")));
    }

    @Test
    public void hashCodeTest() {
        RoleDescription description1 = new RoleDescription("Valid Role Description");
        RoleDescription description2 = new RoleDescription("Valid Role Description");
        RoleDescription description3 = new RoleDescription("Other Valid Role Description");

        assertEquals(description1.hashCode(), description2.hashCode());
        assertNotEquals(description1.hashCode(), description3.hashCode());
    }
}
