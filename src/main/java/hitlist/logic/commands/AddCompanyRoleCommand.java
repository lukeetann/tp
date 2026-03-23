package hitlist.logic.commands;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE_DESC;
import static java.util.Objects.requireNonNull;

import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.Role;

/**
 * Adds a company role to the company role list.
 */
public class AddCompanyRoleCommand extends Command {

    public static final String COMMAND_WORD = "roleadd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a company role to the company role list.\n"
            + "Parameters: \n"
            + PREFIX_ROLE + " ROLE_NAME "
            + PREFIX_ROLE_DESC + " ROLE_DESCRIPTION "
            + PREFIX_COMPANY + " COMPANY_NAME\n"
            + "Example: " + COMMAND_WORD + " /r Google Software Engineer /d Develops software for Google /c Google";

    public static final String MESSAGE_SUCCESS = "New company role added: %1$s";
    public static final String MESSAGE_DUPLICATE_COMPANY_ROLE = "The company role %1$s already exists";

    private final Role role;
    private final CompanyName companyName;

    /**
     * Creates an AddCompanyRoleCommand to add the specified {@code Role} to the company with the specified
     * {@code CompanyName}.
     *
     * @param role The role to be added to the company.
     * @param companyName The name of the company to which the role will be added.
     */
    public AddCompanyRoleCommand(Role role, CompanyName companyName) {
        requireAllNonNull(role, companyName);
        this.role = role;
        this.companyName = companyName;
        assert this.role != null : "Role should not be null after command initialization";
        assert this.companyName != null : "CompanyName should not be null after command initialization";
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasCompanyRole(companyName, role)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_COMPANY_ROLE, role.getRoleName()));
        }

        model.addCompanyRole(companyName, role);

        assert model.hasCompanyRole(companyName, role) : "Model should contain the newly added company role";

        return new CommandResult(String.format(MESSAGE_SUCCESS, role.getRoleName()),
                false, false, true);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddCompanyRoleCommand)) {
            return false;
        }
        AddCompanyRoleCommand otherCommand = (AddCompanyRoleCommand) other;
        return role.equals(otherCommand.role)
                && companyName.equals(otherCommand.companyName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("companyName", companyName)
                .add("role", role)
                .toString();
    }
}
