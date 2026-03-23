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
        assertThrows(NullPointerException.class, () -> new Role(null, new RoleDescription("Valid Description")));
        assertThrows(NullPointerException.class, () -> new Role(new RoleName("Valid Name"), null));
    }

    @Test
    public void constructor_invalidRole_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, ()
                -> new Role(new RoleName(""), new RoleDescription("")));
    }

    @Test
    public void constructor_invalidRoleName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, ()
                -> new Role(new RoleName(""), new RoleDescription("Valid Role Description")));
    }

    @Test
    public void constructor_invalidRoleDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, ()
                -> new Role(new RoleName("Valid Role Name"), new RoleDescription("")));
    }

    @Test
    public void getRoleName() {
        RoleName roleName = new RoleName("Valid Role Name");
        Role role = new Role(roleName, new RoleDescription("Valid Role Description"));
        assertEquals(roleName, role.getRoleName());
    }

    @Test
    public void getRoleDescription() {
        RoleDescription roleDescription = new RoleDescription("Valid Role Description");
        Role role = new Role(new RoleName("Valid Role Name"), roleDescription);
        assertEquals(roleDescription, role.getRoleDescription());
    }

    @Test
    public void isSameRole() {
        Role role = new Role(new RoleName("Valid Role Name"), new RoleDescription("Valid Role Description"));

        assertTrue(role.isSameRole(role));

        assertFalse(role.isSameRole(null));

        assertFalse(role.isSameRole(new Role(
                new RoleName("Other Valid Role Name"),
                new RoleDescription("Valid Role Description"))));

        assertTrue(role.isSameRole(new Role(
                new RoleName("Valid Role Name"),
                new RoleDescription("Other Valid Role Description"))));
    }

    @Test
    public void equals() {
        Role role = new Role(new RoleName("Valid Role Name"), new RoleDescription("Valid Role Description"));

        assertTrue(role.equals(new Role(
                new RoleName("Valid Role Name"),
                new RoleDescription("Valid Role Description"))));

        assertTrue(role.equals(role));

        assertFalse(role.equals(null));

        assertFalse(role.equals(5.0f));

        assertFalse(role.equals(new Role(
                new RoleName("Other Valid Role Name"),
                new RoleDescription("Valid Role Description"))));

        assertFalse(role.equals(new Role(
                new RoleName("Valid Role Name"),
                new RoleDescription("Other Valid Role Description"))));
    }

    @Test
    public void hashCodeTest() {
        Role role1 = new Role(new RoleName("Valid Role Name"), new RoleDescription("Valid Role Description"));
        Role role2 = new Role(new RoleName("Valid Role Name"), new RoleDescription("Valid Role Description"));
        Role role3 = new Role(new RoleName("Other Valid Role Name"), new RoleDescription("Other Valid Role Description"));

        assertEquals(role1.hashCode(), role2.hashCode());

        assertNotEquals(role1.hashCode(), role3.hashCode());
    }

    @Test
    public void toString_validRole_returnsStringRepresentation() {
        Role role = new Role(new RoleName("Valid Role Name"), new RoleDescription("Valid Role Description"));
        String expectedString = "role=Valid Role Name, description=Valid Role Description";
        assertEquals(expectedString, role.toString());
    }
}
