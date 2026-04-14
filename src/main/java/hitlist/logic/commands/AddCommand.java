package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static hitlist.logic.parser.CliSyntax.PREFIX_EMAIL;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static hitlist.logic.parser.CliSyntax.PREFIX_PHONE;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.person.Person;

/**
 * Adds a person to the HitList.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the hitlist.\n"
            + "Parameters: " + PREFIX_NAME + " NAME "
            + PREFIX_PHONE + " PHONE "
            + "[" + PREFIX_EMAIL + " EMAIL] "
            + "[" + PREFIX_ADDRESS + " ADDRESS] "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + " John Doe "
            + PREFIX_PHONE + " 98765432 "
            + PREFIX_EMAIL + " johnd@example.com "
            + PREFIX_ADDRESS + " 311, Clementi Ave 2, #02-25 ";

    public static final String MESSAGE_SUCCESS = "Added contact: ";
    public static final String MESSAGE_DUPLICATE_NAME = "Duplicate Contact: "
            + "A contact with the name '%s' already exists";
    public static final String MESSAGE_DUPLICATE_PHONE = "Duplicate Phone number: "
            + "A contact with the number '%s' already exists";

    private static final String PHONE_LABEL = " | Phone: ";
    private static final String EMAIL_LABEL = " | Email: ";
    private static final String ADDRESS_LABEL = " | Address: ";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        boolean hasSameName = model.getFilteredPersonList().stream()
                .anyMatch(person -> person.getName().equals(toAdd.getName()));

        if (hasSameName) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_NAME, toAdd.getName()));
        }

        // Find existing person with same number
        Optional<Person> existingWithNumber = model.getFilteredPersonList().stream()
                .filter(p -> p.getPhone().equals(toAdd.getPhone()))
                .findFirst();

        if (existingWithNumber.isPresent()) {
            throw new CommandException(String.format(
                    MESSAGE_DUPLICATE_PHONE,
                    toAdd.getPhone()));
        }

        model.addPerson(toAdd);
        return new CommandResult(buildSuccessMessage(toAdd));
    }

    /**
     * Builds a success message that reflects the fields actually provided by the user.
     */
    private static String buildSuccessMessage(Person person) {
        StringBuilder message = new StringBuilder(MESSAGE_SUCCESS)
                .append(person.getName())
                .append(PHONE_LABEL)
                .append(person.getPhone());

        person.getEmail().ifPresent(email -> message.append(EMAIL_LABEL).append(email));
        person.getAddress().ifPresent(address -> message.append(ADDRESS_LABEL).append(address));

        return message.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
