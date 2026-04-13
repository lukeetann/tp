package hitlist.logic.commands;

import static hitlist.ui.UiPaneVisibility.SHOW_COMPANY_LIST;
import static java.util.Objects.requireNonNull;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.Messages;
import hitlist.model.Model;
import hitlist.model.company.CompanyMatchesFindPredicate;

/**
 * Finds and lists all companies in HitList whose company name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindCompanyCommand extends Command {

    public static final String COMMAND_WORD = "cmpfind";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all companies whose names contain "
            + "the provided substring (case-insensitive).\n"
            + "Parameters: KEYWORD...\n"
            + "Note: Each keyword must be a valid company name format.\n"
            + "Example: " + COMMAND_WORD + " Google";

    public static final String MESSAGE_INVALID_KEYWORD = "Invalid search keyword: '%s'\n"
            + "Search keywords must be valid company names. %s\n"
            + "Example: " + COMMAND_WORD + " Google";

    public static final String SEARCH_KEYWORD_CONSTRAINTS =
            "Search keywords cannot contain forward slashes (/), control characters, or vertical whitespace.\n"
                    + "Single characters are allowed for search.\n"
                    + "Search keywords have no maximum length and can contain spaces.";

    private final CompanyMatchesFindPredicate predicate;

    public FindCompanyCommand(CompanyMatchesFindPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredCompanyList(predicate);

        if (model.getFilteredCompanyList().isEmpty()) {
            return new CommandResult(Messages.MESSAGE_NO_COMPANIES_FOUND, SHOW_COMPANY_LIST);
        }

        return new CommandResult(
                String.format(Messages.MESSAGE_COMPANY_LISTED_OVERVIEW,
                        model.getFilteredCompanyList().size()), SHOW_COMPANY_LIST);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FindCompanyCommand)) {
            return false;
        }
        FindCompanyCommand otherFindCompanyCommand = (FindCompanyCommand) other;
        return predicate.equals(otherFindCompanyCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
