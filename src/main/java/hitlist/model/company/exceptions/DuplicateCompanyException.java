package hitlist.model.company.exceptions;

/**
 * Exception thrown when attempting to create a company that already exists in the system.
 */
public class DuplicateCompanyException extends RuntimeException {
    public DuplicateCompanyException(String message) {
        super(message);
    }
}
