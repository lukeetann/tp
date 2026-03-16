package hitlist.model.company.role;

import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        // null name
        assertThrows(NullPointerException.class, () -> new Role(null, null));
    }

    @Test
    public void constructor_invalidRole_throwsIllegalArgumentException() {
        // invalid name and description
        String invalidName = "";
        String invalidDescription = "";
        assertThrows(IllegalArgumentException.class, ()
                -> new Role(new RoleName(invalidName), new RoleDescription(invalidDescription)));
    }

    @Test
    public void constructor_invalidRoleName_throwsIllegalArgumentException() {
        // invalid name
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, ()
                -> new Role(new RoleName(invalidName), new RoleDescription("Valid Role Description")));
    }

    @Test
    public void constructor_invalidRoleDescription_throwsIllegalArgumentException() {
        // invalid description
        String invalidDescription = "";
        assertThrows(IllegalArgumentException.class, ()
                -> new Role(new RoleName("Valid Role Name"), new RoleDescription(invalidDescription)));
    }

    @Test
    public void getRoleName() {
        RoleName roleName = new RoleName("Valid Role Name");
        Role role = new Role(roleName, new RoleDescription("Valid Role Description"));
        assertEquals(role.getRoleName(), roleName);
    }

    @Test
    public void getRoleDescription() {
        RoleDescription roleDescription = new RoleDescription("Valid Role Description");
        Role role = new Role(new RoleName("Valid Role Name"), roleDescription);
        assertEquals(role.getRoleDescription(), roleDescription);
    }

    @Test
    public void isSameRole() {
        Role role = new Role(new RoleName("Valid Role Name"), new RoleDescription("Valid Role Description"));

        // same object -> returns true
        assertTrue(role.isSameRole(role));

        // null -> returns false
        assertFalse(role.isSameRole(null));

        // different name -> returns false
        assertFalse(role.isSameRole(new Role(
                new RoleName("Other Valid Role Name"),
                new RoleDescription("Valid Role Description"))));

        // different description -> returns true
        assertTrue(role.isSameRole(new Role(
                new RoleName("Valid Role Name"),
                new RoleDescription("Other Valid Role Description"))));
    }

    @Test
    public void equals() {
        Role role = new Role(new RoleName("Valid Role Name"), new RoleDescription("Valid Role Description"));

        // same values -> returns true
        assertTrue(role.equals(
                new Role(
                        new RoleName("Valid Role Name"),
                        new RoleDescription("Valid Role Description"))));

        // same object -> returns true
        assertTrue(role.equals(role));

        // null -> returns false
        assertFalse(role.equals(null));

        // different types -> returns false
        assertFalse(role.equals(5.0f));

        // different values -> returns false
        assertFalse(role.equals(new Role(
                new RoleName("Other Valid Role Name"),
                new RoleDescription("Other Valid Role Description"))));
    }

    @Test
    public void hashCodeTest() {
        Role role1 = new Role(
                new RoleName("Valid Role Name"),
                new RoleDescription("Valid Role Description"));
        Role role2 = new Role(
                new RoleName("Valid Role Name"),
                new RoleDescription("Valid Role Description"));
        Role role3 = new Role(
                new RoleName("Other Valid Role Name"),
                new RoleDescription("Other Valid Role Description"));

        // same values -> same hash code
        assertEquals(role1.hashCode(), role2.hashCode());

        // different values -> different hash code
        assertNotEquals(role1.hashCode(), role3.hashCode());
    }

    @Test
    public void toString_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Role(null, null).toString());
    }

    @Test
    public void toString_validRole_returnsStringRepresentation() {
        Role role = new Role(new RoleName("Valid Role Name"), new RoleDescription("Valid Role Description"));
        String expectedString = "role=Valid Role Name, description=Valid Role Description";
        assertEquals(expectedString, role.toString());
    }
}
