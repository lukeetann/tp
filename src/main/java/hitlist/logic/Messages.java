package hitlist.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hitlist.logic.parser.Prefix;
import hitlist.model.company.Company;
import hitlist.model.company.role.Role;
import hitlist.model.group.Group;
import hitlist.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSON_NOT_FOUND = "The contact %1$s cannot be found";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail().map(e -> e.toString()).orElse(""))
                .append("; Address: ")
                .append(person.getAddress().map(e -> e.toString()).orElse(""))
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code group} for display to the user.
     */
    public static String formatGroup(Group group) {
        final StringBuilder builder = new StringBuilder();
        builder.append(group.getName());
        return builder.toString();
    }

    /**
     * Formats the {@code company} for display to the user.
     *
     * @param company The company to be formatted.
     * @return A string representation of the company, including its name and description.
     */
    public static String formatCompany(Company company) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Name: ")
                .append(company.getName())
                .append("; Description: ")
                .append(company.getDescription());
        return builder.toString();
    }

    /**
    * Formats the {@code role} for display to the user.
    *
    * @param role The role to be formatted.
    * @return A string representation of the role, including its name and description.
    */
    public static String formatCompanyRole(Role role) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Role: ")
                .append(role.getRoleName())
                .append("; Description: ")
                .append(role.getRoleDescription());
        return builder.toString();
    }
}
