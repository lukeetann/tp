package hitlist.model.group.exceptions;

/**
 * Signals that the operation will result in duplicate Contact Groups.
 */
public class DuplicateGroupException extends RuntimeException {
    public DuplicateGroupException() {
        super("Operation would result in duplicate groups");
    }
}
