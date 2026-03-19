package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import hitlist.logic.commands.AssignGroupCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.group.GroupName;
import hitlist.model.person.Name;

/**
 * Parses input arguments and creates a new AssignGroupCommand object.
 */
public class AssignGroupCommandParser implements Parser<AssignGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AssignGroupCommand
     * and returns an AssignGroupCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public AssignGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GROUP);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_GROUP)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    AssignGroupCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_GROUP);

        Name personName = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        GroupName groupName = ParserUtil.parseGroupName(argMultimap.getValue(PREFIX_GROUP).get());

        return new AssignGroupCommand(personName, groupName);
    }

    /**
     * Returns true if all the prefixes contain non-empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
