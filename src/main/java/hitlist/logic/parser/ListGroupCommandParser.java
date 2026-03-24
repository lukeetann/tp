package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;

import java.util.stream.Stream;

import hitlist.logic.commands.ListGroupCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.group.GroupName;

/**
 * Parses input arguments and creates a new ListGroupCommand object
 */
public class ListGroupCommandParser implements Parser<ListGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListGroupCommand
     * and returns an ListGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_GROUP);

        if (!arePrefixesPresent(argMultimap, PREFIX_GROUP)
                && !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListGroupCommand.MESSAGE_USAGE));
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_GROUP)) {
            return new ListGroupCommand();
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_GROUP);
        GroupName name = ParserUtil.parseGroupName(argMultimap.getValue(PREFIX_GROUP).get());;

        return new ListGroupCommand(name);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
