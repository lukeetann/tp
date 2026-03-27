package hitlist.logic.commands;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;
import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static hitlist.ui.UiPaneVisibility.SHOW_GROUP_LIST;
import static java.util.Objects.requireNonNull;

import java.util.Set;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.Messages;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.group.Group;
import hitlist.model.person.Name;
import hitlist.model.person.Person;

/**
 * Adds a group to the HitList.
 */
public class AddGroupCommand extends Command {

    public static final String COMMAND_WORD = "grpadd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a group to the HitList.\n"
            + "Parameters: "
            + PREFIX_GROUP + " GROUP_NAME "
            + "[" + PREFIX_NAME + " MEMBER_NAME]...\n"
            + "Examples:\n"
            + COMMAND_WORD + " " + PREFIX_GROUP + " Students\n"
            + COMMAND_WORD + " " + PREFIX_GROUP + " Students "
            + PREFIX_NAME + " Alex Yeoh "
            + PREFIX_NAME + " Bernice Yu\n";

    public static final String MESSAGE_SUCCESS = "Group %1$s has been created";
    public static final String MESSAGE_DUPLICATE_GROUP = "Duplicate Group: Group %1$s already exists";

    private final Group toAdd;
    private final Set<Name> memberNames;

    /**
     * Creates an AddGroupCommand to add the specified {@code Group} with the specified members.
     */
    public AddGroupCommand(Group group, Set<Name> memberNames) {
        requireAllNonNull(group, memberNames);
        toAdd = group;
        this.memberNames = memberNames;
    }

    /**
     * Creates an AddGroupCommand to add the specified {@code Group} with no members.
     */
    public AddGroupCommand(Group group) {
        requireNonNull(group);
        toAdd = group;
        this.memberNames = Set.of();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasGroup(toAdd)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_GROUP, toAdd.getName()));
        }

        model.addGroup(toAdd);

        for (Name memberName : memberNames) {
            Person member = model.getHitList().getPersonList().stream()
                .filter(person -> person.getName().equals(memberName))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(Messages.MESSAGE_PERSON_NOT_FOUND, memberName)));

            toAdd.addMember(member);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getName()), SHOW_GROUP_LIST);
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
