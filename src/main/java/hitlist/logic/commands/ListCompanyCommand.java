package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static java.util.Objects.requireNonNull;

import hitlist.model.Model;
import hitlist.model.company.Company;
import hitlist.model.company.CompanyName;

/**
 * Lists all companies in the hit list to the user.
 */
public class ListCompanyCommand extends Command {

    private final CompanyName name;

    public static final String COMMAND_WORD = "cmplist";

    public static final String MESSAGE_SUCCESS = "Listed company(s): %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists the company details of the company that matches"
            + " the searched company. If no company is searched, lists all companies\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_COMPANY + " Google\n"
            + "Example: " + COMMAND_WORD;

    public ListCompanyCommand() {
        // No-argument constructor for listing all companies
        this.name = null;
    }

    public ListCompanyCommand(CompanyName name) {
        requireNonNull(name);
        this.name = name;
    }

    private boolean isMatchingCompany(Company otherCompany) {
        requireNonNull(otherCompany);
        if (name == null) {;
            return true; // No specific company to match, show all companies
        }
        return otherCompany.getName().equals(name);
    }

    private String companiesListed() {
        if (name == null) {
            return "All Companies";
        }
        return name.toString();
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredCompanyList(this::isMatchingCompany);
        return new CommandResult(String.format(MESSAGE_SUCCESS, companiesListed()), false, false, true);
    }
}
