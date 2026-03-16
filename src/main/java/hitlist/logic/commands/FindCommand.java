package hitlist.logic.commands;

import static java.util.Objects.requireNonNull;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.Messages;
import hitlist.model.Model;
import hitlist.model.person.PersonMatchesFindPredicate;

/**
 * Finds and lists all persons whose names match any of the given prefixes
 * and/or whose tags match any of the given tags.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names match "
            + "any given prefix and/or whose tags match the specified tags (case-insensitive).\n"
            + "Parameters: [KEYWORD]... [t/TAG]...\n"
            + "Example: " + COMMAND_WORD + " Han t/school";

    private final PersonMatchesFindPredicate predicate;

    public FindCommand(PersonMatchesFindPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FindCommand)) {
            return false;
        }
        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
