package hitlist.testutil;

import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;

import hitlist.logic.commands.AddGroupCommand;
import hitlist.model.group.Group;

/**
 * A utility class for Group.
 */
public class GroupUtil {

    /**
     * Returns an add group command string for adding the {@code group}.
     */
    public static String getAddGroupCommand(Group group) {
        return AddGroupCommand.COMMAND_WORD + " " + getGroupDetails(group);
    }

    /**
     * Returns the part of command string for the given {@code group}'s details.
     */
    public static String getGroupDetails(Group group) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_GROUP + group.getName().fullName + " ");
        return sb.toString();
    }
}
