package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY_DESC;
import static java.util.Objects.requireNonNull;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.Messages;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.company.Company;

/**
 * Adds a company to the company list.
 */
public class AddCompanyCommand extends Command {

    public static final String COMMAND_WORD = "cmpadd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a company and company description to the company "
            + "list.\nParameters: \n"
            + PREFIX_COMPANY + " NAME \n"
            + PREFIX_COMPANY_DESC + " DESCRIPTION\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_COMPANY + " Google"
            + PREFIX_COMPANY_DESC + " A technology company";

    public static final String MESSAGE_SUCCESS = "New Company added: %1$s";
    public static final String MESSAGE_DUPLICATE_COMPANY = "The company %1$s already exists";

    private final Company company;

    /**
     * Creates an AddCompanyCommand to add the specified {@code Company}
     *
     * @param company The company to be added
     */
    public AddCompanyCommand(Company company) {
        requireNonNull(company);
        this.company = company;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasCompany(company)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_COMPANY, company.getName()));
        }

        model.addCompany(company);

        assert model.hasCompany(company) : "Model should contain the newly added company";

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.formatCompany(company)),
                false, false, true);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCompanyCommand)) {
            return false;
        }

        AddCompanyCommand otherAddCompanyCommand = (AddCompanyCommand) other;
        return company.equals(otherAddCompanyCommand.company);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("company", company)
                .toString();
    }
}
