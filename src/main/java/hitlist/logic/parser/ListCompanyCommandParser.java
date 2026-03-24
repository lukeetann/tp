package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;

import java.util.stream.Stream;

import hitlist.logic.commands.ListCompanyCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.company.CompanyName;

/**
 * Parses input arguments and creates a new ListGroupCommand object
 */
public class ListCompanyCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the ListCompanyCommand
     * and returns an ListCompanyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCompanyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COMPANY);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCompanyCommand.MESSAGE_USAGE));
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_COMPANY)) {
            return new ListCompanyCommand();
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_COMPANY);
        CompanyName name = ParserUtil.parseCompanyName(argMultimap.getValue(PREFIX_COMPANY).get());

        return new ListCompanyCommand(name);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
