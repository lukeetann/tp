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

            if (trimmedToken.isEmpty()) {
                continue;
            }

            // Validate search keyword
            if (!isValidSearchKeyword(trimmedToken)) {
                throw new ParseException(
                        String.format(FindCompanyCommand.MESSAGE_INVALID_KEYWORD,
                                trimmedToken,
                                FindCompanyCommand.SEARCH_KEYWORD_CONSTRAINTS));
            }
            companyNameKeywords.add(trimmedToken);
        }

        if (companyNameKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
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
    private boolean isValidSearchKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return false;
        }

        String trimmed = keyword.trim();

        if (trimmed.isEmpty()) {
            return false;
        }

        // Forbid forward slash (same as CompanyName)
        if (trimmed.contains("/")) {
            return false;
        }

        // Forbid control characters (same as CompanyName's \p{C})
        for (char c : trimmed.toCharArray()) {
            if (Character.isISOControl(c)) {
                return false;
            }
        }

        // Forbid vertical whitespace (same as CompanyName's \v)
        if (trimmed.matches(".*\\v.*")) {
            return false;
        }

        // SPECIAL EXCEPTION: Allow single character searches
        if (trimmed.length() == 1) {
            return true;
        }

        return true;
    }
}
