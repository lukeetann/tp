package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import hitlist.logic.commands.FindCompanyCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.company.CompanyMatchesFindPredicate;

/**
 * Parses input arguments and creates a new FindCompanyCommand object
 */
public class FindCompanyCommandParser implements Parser<FindCompanyCommand> {

    /**
     * The search string should only consist of the characters defined below, and should not be blank.
     */
    public static final String VALIDATION_REGEX = "^[^/\\s\\p{C}][^/\\v\\p{C}]*$";

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

            // Validate search keyword
            if (!isValidSearchKeyword(trimmedToken)) {
                throw new ParseException(
                        String.format(FindCompanyCommand.MESSAGE_INVALID_KEYWORD,
                                trimmedToken,
                                FindCompanyCommand.SEARCH_KEYWORD_CONSTRAINTS));
            }
            companyNameKeywords.add(trimmedToken);
        }

        return new FindCompanyCommand(new CompanyMatchesFindPredicate(companyNameKeywords));
    }

    /**
     * Validates a search keyword.
     * More permissive than CompanyName validation:
     * - Single characters are allowed
     * - No maximum length limit (unlike CompanyName's 50-char limit)
     * - Forbids forward slash (/) - same as CompanyName
     * - Forbids control characters - same as CompanyName
     * - Forbids vertical whitespace - same as CompanyName
     * - Allows spaces and special characters
     */
    public static boolean isValidSearchKeyword(String test) {
        requireNonNull(test);
        return test.matches(VALIDATION_REGEX);
    }
}
