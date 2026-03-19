package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static java.util.Objects.requireNonNull;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.group.Group;

/**
 * Adds a group to the HitList.
 */
public class AddGroupCommand extends Command {

    public static final String COMMAND_WORD = "grpadd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a group to the HitList. "
            + "Parameters: "
            + PREFIX_GROUP + " NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_GROUP + " Students";

    public static final String MESSAGE_SUCCESS = "Group %1$s has been created";
    public static final String MESSAGE_DUPLICATE_GROUP = "Duplicate Group: Group %1$s already exists";

    private final Group toAdd;

    /**
     * Creates an AddGroupCommand to add the specified {@code Group}
     */
    public AddGroupCommand(Group group) {
        requireNonNull(group);
        toAdd = group;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasGroup(toAdd)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_GROUP, toAdd.getName()));
        }

        model.addGroup(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddGroupCommand)) {
            return false;
        }

        AddGroupCommand otherAddGroupCommand = (AddGroupCommand) other;
        return toAdd.equals(otherAddGroupCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
