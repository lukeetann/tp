package hitlist.model.company.role;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a Role in the hitlist.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Role {

    private final RoleName roleName;
    private final RoleDescription roleDescription;

    /**
     * Constructs a {@code Role}.
     *
     * @param roleName A valid role name.
     * @param roleDescription A valid role description.
     */
    public Role(RoleName roleName, RoleDescription roleDescription) {
        requireAllNonNull(roleName, roleDescription);
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    /**
     * Gets the name of the role.
     *
     * @return The name of the role.
     */
    public RoleName getRoleName() {
        return roleName;
    }

    /**
     * Gets the description of the role.
     *
     * @return The description of the role.
     */
    public RoleDescription getRoleDescription() {
        return roleDescription;
    }

    /**
     * Returns true if both roles have the same name.
     * This defines a weaker notion of equality between two roles.
     *
     * @param otherRole The other role to compare with.
     * @return True if both roles have the same name, false otherwise.
     */
    public boolean isSameRole(Role otherRole) {
        if (otherRole == this) {
            return true;
        }

        return otherRole != null
                && roleName.equals(otherRole.roleName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Role)) {
            return false;
        }

        Role otherRole = (Role) other;
        return roleName.equals(otherRole.roleName)
                && roleDescription.equals(otherRole.roleDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName, roleDescription);
    }

    @Override
    public String toString() {
        return "role=" + roleName + ", description=" + roleDescription;
    }
}
