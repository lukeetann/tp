package hitlist.model.company.role.exceptions;

/**
 * Exception thrown when attempting to create a role that already exists in the system.
 */
public class DuplicateRoleException extends RuntimeException {
    public DuplicateRoleException(String message) {
        super(message);
    }
}
