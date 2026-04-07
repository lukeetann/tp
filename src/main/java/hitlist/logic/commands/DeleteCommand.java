package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static java.util.Objects.requireNonNull;

import java.util.List;

import hitlist.commons.core.index.Index;
import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.Messages;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.person.Name;
import hitlist.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the HitList.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String COMMAND_WORD_ALIAS = "del";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) or NAME\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "Example: " + COMMAND_WORD_ALIAS + " " + PREFIX_NAME + " John Doe";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted %1$s";

    private final Index targetIndex;
    private final Name targetName;

    /**
     * Creates a DeleteCommand to delete the specified {@code Person}
     * @param targetIndex
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.targetName = null;
    }

    /**
     * Creates a DeleteCommand to delete the specified {@code Person}
     * @param targetName
     */
    public DeleteCommand(Name targetName) {
        this.targetIndex = null;
        this.targetName = targetName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (isDeleteByIndex()) {
            return executeByIndex(model);
        } else {
            return executeByName(model);
        }
    }

    private CommandResult executeByIndex(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    private CommandResult executeByName(Model model) throws CommandException {
        List<Person> nameMatches = model.getPersonsByName(targetName);
        Person personToDelete = nameMatches.stream()
                .filter(person -> person.getName().equals(targetName))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(Messages.MESSAGE_PERSON_NOT_FOUND, targetName)));

        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    private boolean isDeleteByIndex() {
        return targetIndex != null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        if (isDeleteByIndex() && otherDeleteCommand.isDeleteByIndex()) {
            return targetIndex.equals(otherDeleteCommand.targetIndex);
        } else if (!isDeleteByIndex() && !otherDeleteCommand.isDeleteByIndex()) {
            return targetName.equals(otherDeleteCommand.targetName);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .toString();
    }
}
