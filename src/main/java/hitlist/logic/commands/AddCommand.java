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

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the hitlist. "
            + "Parameters: "
            + PREFIX_NAME + " NAME "
            + PREFIX_PHONE + " PHONE "
            + "[" + PREFIX_EMAIL + " EMAIL] "
            + "[" + PREFIX_ADDRESS + " ADDRESS] "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + " John Doe "
            + PREFIX_PHONE + " 98765432 "
            + PREFIX_EMAIL + " johnd@example.com "
            + PREFIX_ADDRESS + " 311, Clementi Ave 2, #02-25 ";

    public static final String MESSAGE_SUCCESS = "Added %1$s with contact number %2$s";
    public static final String MESSAGE_DUPLICATE_NAME = "Duplicate Contact: "
            + "A contact with the name '%s' already exists";
    public static final String MESSAGE_DUPLICATE_PHONE = "Duplicate Phone number: "
            + "A contact with the number '%s' already exists";

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

        // Find existing person with same name
        Optional<Person> existingWithSameName = model.getFilteredPersonList().stream()
                .filter(p -> p.getName().equals(toAdd.getName()))
                .findFirst();

        if (existingWithSameName.isPresent()) {
            throw new CommandException(String.format(
                    MESSAGE_DUPLICATE_NAME,
                    toAdd.getName()));
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
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getName(), toAdd.getPhone()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
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
