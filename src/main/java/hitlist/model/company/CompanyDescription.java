package hitlist.model.company;

import static hitlist.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Company's description in the hit list.
 * Guarantees: immutable; is valid as declared in {@link #isValidCompanyDescription(String)}
 */
public class CompanyDescription {

    public static final String MESSAGE_CONSTRAINTS =
            "Company descriptions must be between 2 and 1000 characters long, "
                    + "cannot start with a space, and cannot contain forward slashes (/) or line breaks.";

    /**
     * The first character of the description must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Forward slashes (/) are banned to prevent command parser conflicts.
     */
    public static final String VALIDATION_REGEX = "^[^\\s/][^/\\v]{1,999}$";

    public final String companyDescription;

    /**
     * Constructs a {@code CompanyDescription}.
     *
     * @param companyDescription A valid company description.
     */
    public CompanyDescription(String companyDescription) {
        requireNonNull(companyDescription);
        checkArgument(isValidCompanyDescription(companyDescription), MESSAGE_CONSTRAINTS);
        this.companyDescription = companyDescription;
        assert this.companyDescription != null : "Company description should not be null after initialization";
        assert isValidCompanyDescription(this.companyDescription) : "Company description must match validation rules";
    }

    /**
     * Returns true if a given string is a valid company description.
     *
     * @param test The string to test for validity as a company description.
     * @return True if the string is a valid company description, false otherwise.
     */
    public static boolean isValidCompanyDescription(String test) {
        assert test != null : "String to test for description validity should not be null";
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return companyDescription;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CompanyDescription)) {
            return false;
        }

        CompanyDescription otherName = (CompanyDescription) other;
        return companyDescription.equals(otherName.companyDescription);
    }

    @Override
    public int hashCode() {
        return companyDescription.hashCode();
    }
}
