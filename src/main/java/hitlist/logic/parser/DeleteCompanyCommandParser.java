package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static java.util.Objects.requireNonNull;

import java.util.stream.Stream;

import hitlist.logic.commands.DeleteCompanyCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.company.CompanyName;

/**
 * Parses input arguments and creates a new DeleteCompanyCommand object
 */
public class DeleteCompanyCommandParser implements Parser<DeleteCompanyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCompanyCommand
     * and returns a DeleteCompanyCommand object for execution.
     *
     * @param args The input arguments to parse
     * @return DeleteCompanyCommand object based on the parsed input
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCompanyCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_COMPANY);

        if (!arePrefixesPresent(argMultimap, PREFIX_COMPANY) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCompanyCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_COMPANY);

        assert argMultimap.getValue(PREFIX_COMPANY).isPresent() : "Prefix company value should be present";

        CompanyName companyName = ParserUtil.parseCompanyName(argMultimap.getValue(PREFIX_COMPANY).get());

        return new DeleteCompanyCommand(companyName);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
