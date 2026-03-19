package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.person.Person;
import hitlist.model.person.PersonInGroupPredicate;

/**
 * Lists all contacts in a group.
 */
public class ListGroupCommand extends Command {

    public static final String COMMAND_WORD = "grplist";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all contacts in a contact group. "
            + "Parameters: "
            + PREFIX_GROUP + " GROUP\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_GROUP + " Students";

    public static final String MESSAGE_SUCCESS = "Listed all contacts in the group %1$s";
    public static final String MESSAGE_GROUP_NOT_FOUND = "No group %1$s was found";

    private final GroupName toList;

    /**
     * Creates a ListGroupCommand to list contacts in {@code GroupName}
     */
    public ListGroupCommand(GroupName toList) {
        requireNonNull(toList);
        this.toList = toList;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Group group = model.getGroup(toList)
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_GROUP_NOT_FOUND, toList)));

        Predicate<Person> predicate = new PersonInGroupPredicate(group);

        model.updateFilteredPersonList(predicate);

        String message = String.format(MESSAGE_SUCCESS, group.getName());
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListGroupCommand)) {
            return false;
        }

        ListGroupCommand otherListGroupCommand = (ListGroupCommand) other;
        return toList.equals(otherListGroupCommand.toList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toList", toList)
                .toString();
    }
}
