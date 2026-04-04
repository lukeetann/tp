package hitlist.logic.commands;

import static hitlist.ui.UiPaneVisibility.SHOW_COMPANY_LIST;
import static java.util.Objects.requireNonNull;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.Messages;
import hitlist.model.Model;
import hitlist.model.company.CompanyMatchesFindPredicate;

/**
 * Finds and lists all companies in hitlist whose company name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindCompanyCommand extends Command {

    public static final String COMMAND_WORD = "cmpfind";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all companies whose names contains "
        + "the provided substring (case-insensitive).\n"
        + "Parameters: [KEYWORD]...\n"
        + "Example: " + COMMAND_WORD + " Google";

    private final CompanyMatchesFindPredicate predicate;

    public FindCompanyCommand(CompanyMatchesFindPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredCompanyList(predicate);
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
