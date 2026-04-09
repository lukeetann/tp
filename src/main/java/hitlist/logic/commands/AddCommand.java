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
    public static final String MESSAGE_DUPLICATE_PHONE = "Duplicate Contact: "
            + "A contact with the phone number '%s' already exists";
    public static final String MESSAGE_DUPLICATE_BOTH = "Duplicate Contact: "
            + "A contact with the name '%s' already exists, "
            + "and a different contact with the phone number '%s' already exists";
    public static final String MESSAGE_DUPLICATE_SAME_PERSON = "Duplicate Contact: "
            + "A contact with the name '%s' and phone number '%s' already exists";

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

        // Find existing person with same phone
        Optional<Person> existingWithSamePhone = model.getFilteredPersonList().stream()
                .filter(p -> p.getPhone().equals(toAdd.getPhone()))
                .findFirst();

        if (existingWithSameName.isPresent() && existingWithSamePhone.isPresent()) {
            // Check if it's the same person or different ones
            Person nameMatch = existingWithSameName.get();
            Person phoneMatch = existingWithSamePhone.get();

            if (nameMatch.equals(phoneMatch)) {
                // Same person has both matching name and phone
                throw new CommandException(String.format(
                        MESSAGE_DUPLICATE_SAME_PERSON,
                        toAdd.getName(), toAdd.getPhone()));
            } else {
                // Different contacts - report both conflicts separately
                throw new CommandException(String.format(
                        MESSAGE_DUPLICATE_BOTH,
                        toAdd.getName(), toAdd.getPhone()));
            }
        } else if (existingWithSameName.isPresent()) {
            throw new CommandException(String.format(
                    MESSAGE_DUPLICATE_NAME,
                    toAdd.getName()));
        } else if (existingWithSamePhone.isPresent()) {
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
