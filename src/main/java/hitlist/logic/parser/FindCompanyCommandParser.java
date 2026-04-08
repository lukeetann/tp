package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import hitlist.logic.commands.FindCompanyCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.company.CompanyMatchesFindPredicate;
import hitlist.model.company.CompanyName;

/**
 * Parses input arguments and creates a new FindCompanyCommand object
 */
public class FindCompanyCommandParser implements Parser<FindCompanyCommand> {

    @Override
    public FindCompanyCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmedArgs.split("\\s+");
        List<String> companyNameKeywords = new ArrayList<>();

        for (String token : tokens) {
            String trimmedToken = token.trim();

            // Validate each keyword follows the same rules as a CompanyName
            if (!CompanyName.isValidCompanyName(trimmedToken)) {
                throw new ParseException(
                        String.format(FindCompanyCommand.MESSAGE_INVALID_KEYWORD, trimmedToken,
                                CompanyName.MESSAGE_CONSTRAINTS));
            }
            companyNameKeywords.add(trimmedToken);
        }

        return new FindCompanyCommand(new CompanyMatchesFindPredicate(companyNameKeywords));
    }
}
