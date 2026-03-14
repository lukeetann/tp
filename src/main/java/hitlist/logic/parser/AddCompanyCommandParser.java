package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY_DESC;

import java.util.stream.Stream;

import hitlist.logic.commands.AddCompanyCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.company.Company;
import hitlist.model.company.CompanyDescription;
import hitlist.model.company.CompanyName;

/**
 * Parses input arguments and creates a new AddCompanyCommand object
 */
public class AddCompanyCommandParser implements Parser<AddCompanyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCompanyCommand
     * and returns an AddCompanyCommand object for execution.
     *
     * @param args The input arguments to parse
     * @return AddCompanyCommand object based on the parsed input
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCompanyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COMPANY, PREFIX_COMPANY_DESC);

        if (!(arePrefixesPresent(argMultimap, PREFIX_COMPANY, PREFIX_COMPANY_DESC))
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_COMPANY, PREFIX_COMPANY_DESC);
        CompanyName companyName = ParserUtil.parseCompanyName(argMultimap.getValue(PREFIX_COMPANY).get());
        CompanyDescription companyDesc = ParserUtil.parseCompanyDescription(argMultimap.getValue(PREFIX_COMPANY_DESC).get());

        Company company = new Company(companyName, companyDesc);

        return new AddCompanyCommand(company);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
