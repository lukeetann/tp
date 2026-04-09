package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hitlist.commons.core.LogsCenter;
import hitlist.logic.commands.AddCommand;
import hitlist.logic.commands.AddCompanyCommand;
import hitlist.logic.commands.AddCompanyRoleCommand;
import hitlist.logic.commands.AddGroupCommand;
import hitlist.logic.commands.AssignGroupCommand;
import hitlist.logic.commands.ClearCommand;
import hitlist.logic.commands.Command;
import hitlist.logic.commands.DeleteCommand;
import hitlist.logic.commands.DeleteCompanyCommand;
import hitlist.logic.commands.DeleteCompanyRoleCommand;
import hitlist.logic.commands.DeleteGroupCommand;
import hitlist.logic.commands.EditCommand;
import hitlist.logic.commands.ExitCommand;
import hitlist.logic.commands.FindCommand;
import hitlist.logic.commands.FindCompanyCommand;
import hitlist.logic.commands.HelpCommand;
import hitlist.logic.commands.ListCommand;
import hitlist.logic.commands.ListCompanyCommand;
import hitlist.logic.commands.ListGroupCommand;
import hitlist.logic.commands.UnassignGroupCommand;
import hitlist.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class HitListParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(HitListParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD_ALIAS:
            // Fallthrough to support command alias "del"
        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ListGroupCommand.COMMAND_WORD:
            return new ListGroupCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case AddGroupCommand.COMMAND_WORD:
            return new AddGroupCommandParser().parse(arguments);

        case AssignGroupCommand.COMMAND_WORD:
            return new AssignGroupCommandParser().parse(arguments);

        case UnassignGroupCommand.COMMAND_WORD:
            return new UnassignGroupCommandParser().parse(arguments);

        case DeleteGroupCommand.COMMAND_WORD:
            return new DeleteGroupCommandParser().parse(arguments);

        case AddCompanyCommand.COMMAND_WORD:
            return new AddCompanyCommandParser().parse(arguments);

        case ListCompanyCommand.COMMAND_WORD:
            return new ListCompanyCommandParser().parse(arguments);

        case DeleteCompanyCommand.COMMAND_WORD:
            return new DeleteCompanyCommandParser().parse(arguments);

        case FindCompanyCommand.COMMAND_WORD:
            return new FindCompanyCommandParser().parse(arguments);

        case AddCompanyRoleCommand.COMMAND_WORD:
            return new AddCompanyRoleCommandParser().parse(arguments);

        case DeleteCompanyRoleCommand.COMMAND_WORD:
            return new DeleteCompanyRoleCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
