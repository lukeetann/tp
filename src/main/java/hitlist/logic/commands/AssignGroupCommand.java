package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static java.util.Objects.requireNonNull;

import java.util.List;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.person.Name;
import hitlist.model.person.Person;

/**
 * Adds an existing contact to an existing group.
 */
public class AssignGroupCommand extends Command {

    public static final String COMMAND_WORD = "grpassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a contact to an existing group.\n"
            + "Parameters: "
            + PREFIX_NAME + " NAME "
            + PREFIX_GROUP + " GROUP_NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + " Alex "
            + PREFIX_GROUP + " Students";

    public static final String MESSAGE_SUCCESS = "Added %1$s to group %2$s";
    public static final String MESSAGE_GROUP_NOT_FOUND = "Group not found: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Contact not found: %1$s";
    public static final String MESSAGE_PERSON_NOT_UNIQUE = "Multiple contacts found with name: %1$s";
    public static final String MESSAGE_PERSON_ALREADY_IN_GROUP = "%1$s is already in group %2$s";

    private final Name personName;
    private final GroupName groupName;

    /**
     * Creates an AssignGroupCommand to add the specified person into the specified group.
     */
    public AssignGroupCommand(Name personName, GroupName groupName) {
        requireNonNull(personName);
        requireNonNull(groupName);
        this.personName = personName;
        this.groupName = groupName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> matchedPersons = model.getPersonsByName(personName);
        if (matchedPersons.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, personName));
        }

        if (matchedPersons.size() > 1) {
            throw new CommandException(String.format(MESSAGE_PERSON_NOT_UNIQUE, personName));
        }

        Group targetGroup = model.getGroup(groupName)
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_GROUP_NOT_FOUND, groupName)));

        Person personToAssign = matchedPersons.get(0);

        if (targetGroup.getMembers().contains(personToAssign)) {
            throw new CommandException(String.format(
                    MESSAGE_PERSON_ALREADY_IN_GROUP,
                    personToAssign.getName(),
                    targetGroup.getName()));
        }

        targetGroup.addMember(personToAssign);

        return new CommandResult(String.format(
                MESSAGE_SUCCESS,
                personToAssign.getName(),
                targetGroup.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AssignGroupCommand)) {
            return false;
        }

        AssignGroupCommand otherAssignGroupCommand = (AssignGroupCommand) other;
        return personName.equals(otherAssignGroupCommand.personName)
                && groupName.equals(otherAssignGroupCommand.groupName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personName", personName)
                .add("groupName", groupName)
                .toString();
    }
}
