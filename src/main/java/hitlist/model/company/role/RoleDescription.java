package hitlist.model.company.role;

import static hitlist.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Role's description in the hit list.
 * Guarantees: immutable; is valid as declared in {@link #isValidRoleDescription(String)}
 */
public class RoleDescription {

    public static final String MESSAGE_CONSTRAINTS =
            "Role description must be between 2 and 1000 characters long, "
                    + "cannot start with a space, and cannot contain forward slashes (/) or line breaks.";

    /**
     * The first character of the role name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Forward slashes (/) are not allowed to prevent command parser conflicts.
     */
    public static final String VALIDATION_REGEX = "^[^\\s/][^/\\v]{1,999}$";

    private final String roleDescription;

    /**
     * Constructs a {@code Description}.
     *
     * @param roleDescription A valid role description.
     */
    public RoleDescription(String roleDescription) {
        requireNonNull(roleDescription);
        checkArgument(isValidRoleDescription(roleDescription), MESSAGE_CONSTRAINTS);
        this.roleDescription = roleDescription.trim();
    }

    /**
     * Returns true if a given string is a valid role description.
     *
     * @param test The string to test for validity as a role description.
     * @return True if the string is a valid role description, false otherwise.
     */
    public static boolean isValidRoleDescription(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return roleDescription;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RoleDescription)) {
            return false;
        }

        RoleDescription otherDescription = (RoleDescription) other;
        return roleDescription.equals(otherDescription.roleDescription);
    }

    @Override
    public int hashCode() {
        return roleDescription.hashCode();
    }
}
