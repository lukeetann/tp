package hitlist.model.company.role.exceptions;

/**
 * Exception thrown when a role is not found in the system.
 */
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
