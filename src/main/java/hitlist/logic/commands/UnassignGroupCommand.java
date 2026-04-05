package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.person.Name;
import hitlist.model.person.Person;
import hitlist.model.person.PersonInGroupPredicate;

/**
 * Removes an existing contact from an existing group.
 */
public class UnassignGroupCommand extends Command {

    public static final String COMMAND_WORD = "grpunassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes a contact from an existing group.\n"
            + "Parameters: "
            + PREFIX_NAME + " NAME "
            + PREFIX_GROUP + " GROUP_NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + " Alex "
            + PREFIX_GROUP + " Students";

    public static final String MESSAGE_SUCCESS = "Removed %1$s from group %2$s";
    public static final String MESSAGE_GROUP_NOT_FOUND = "Group not found: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Contact not found: %1$s";
    public static final String MESSAGE_PERSON_NOT_UNIQUE = "Multiple contacts found with name: %1$s";
    public static final String MESSAGE_PERSON_NOT_IN_GROUP = "%1$s is not in group %2$s";

    private final Name personName;
    private final GroupName groupName;

    /**
     * Creates an UnassignGroupCommand to remove the specified person from the specified group.
     */
    public UnassignGroupCommand(Name personName, GroupName groupName) {
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

        Person personToUnassign = matchedPersons.get(0);

        if (!targetGroup.hasMember(personToUnassign)) {
            throw new CommandException(String.format(
                    MESSAGE_PERSON_NOT_IN_GROUP,
                    personToUnassign.getName(),
                    targetGroup.getName()));
        }

        targetGroup.removeMember(personToUnassign);

        Predicate<Person> predicate = new PersonInGroupPredicate(targetGroup);
        model.updateFilteredPersonList(predicate);

        return new CommandResult(String.format(
                MESSAGE_SUCCESS,
                personToUnassign.getName(),
                targetGroup.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnassignGroupCommand)) {
            return false;
        }

        UnassignGroupCommand otherUnassignGroupCommand = (UnassignGroupCommand) other;
        return personName.equals(otherUnassignGroupCommand.personName)
                && groupName.equals(otherUnassignGroupCommand.groupName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personName", personName)
                .add("groupName", groupName)
                .toString();
    }
}
