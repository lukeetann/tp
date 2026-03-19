package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static java.util.Objects.requireNonNull;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.Messages;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.group.Group;

public class DeleteGroupCommand extends Command {

    public static final String COMMAND_WORD = "grpdel";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes a contact group from HitList.\n"
            + "Parameters: " + PREFIX_GROUP + " GROUP_NAME\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_GROUP + " Students";

    public static final String MESSAGE_SUCCESS = "Deleted group: %1$s";
    public static final String MESSAGE_GROUP_NOT_FOUND = "Group %1$s does not exist";

    private final Group toDelete;

    public DeleteGroupCommand(Group toDelete) {
        requireNonNull(toDelete);
        this.toDelete = toDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasGroup(toDelete)) {
            throw new CommandException(String.format(MESSAGE_GROUP_NOT_FOUND, toDelete.getName()));
        }

        model.deleteGroup(toDelete);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.formatGroup(toDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteGroupCommand)) {
            return false;
        }

        DeleteGroupCommand otherDeleteGroupCommand = (DeleteGroupCommand) other;
        return toDelete.equals(otherDeleteGroupCommand.toDelete);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toDelete", toDelete)
                .toString();
    }
}
