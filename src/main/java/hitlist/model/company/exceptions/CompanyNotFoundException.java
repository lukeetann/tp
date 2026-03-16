package hitlist.model.company.exceptions;

/**
 * Exception thrown when a company is not found in the system.
 */
public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(String message) {
        super(message);
    }
}
