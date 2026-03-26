package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.model.Model.PREDICATE_SHOW_ALL_COMPANIES;
import static java.util.Objects.requireNonNull;

import hitlist.model.Model;
import hitlist.model.company.CompanyName;

/**
 * Lists all companies in the HitList to the user.
 */
public class ListCompanyCommand extends Command {

    public static final String COMMAND_WORD = "cmplist";

    public static final String MESSAGE_SUCCESS = "Listed company: %1$s";

    public static final String DEFAULT_MESSAGE_SUCCESS = "Listed all companies";

    public static final String MESSAGE_NO_COMPANY_FOUND = "No company found with the name: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists the company details of the company that matches"
            + " the searched company. If no company is searched, lists all companies\n"
            + "Parameters: " + "[" + PREFIX_COMPANY + " COMPANY_NAME]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_COMPANY + " Google\n"
            + "Example: " + COMMAND_WORD;

    private final CompanyName name;

    /**
     * Creates a ListCompanyCommand to list all companies.
     */
    public ListCompanyCommand() {
        // No-argument constructor for listing all companies
        this.name = null;
    }

    /**
     * Creates a ListCompanyCommand to list the specified company.
     * @param name
     */
    public ListCompanyCommand(CompanyName name) {
        requireNonNull(name);
        this.name = name;
    }

    private boolean isListAllCompanies() {
        return name == null;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (isListAllCompanies()) {
            model.updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);
            return new CommandResult(DEFAULT_MESSAGE_SUCCESS, false, false, true);
        } else if (model.hasCompanyByName(name)) {
            model.updateRoleList(name);
            return new CommandResult(String.format(MESSAGE_SUCCESS, name), false, false, false, false, true);
        } else {
            return new CommandResult(String.format(MESSAGE_NO_COMPANY_FOUND, name), false, false, true);
        }
    }
}
