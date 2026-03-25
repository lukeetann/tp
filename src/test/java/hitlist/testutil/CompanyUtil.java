package hitlist.testutil;

import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY_DESC;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE_DESC;

import hitlist.logic.commands.AddCompanyCommand;
import hitlist.logic.commands.AddCompanyRoleCommand;
import hitlist.logic.commands.DeleteCompanyCommand;
import hitlist.model.company.Company;

/**
 * A utility class for Company.
 */
public class CompanyUtil {

    /**
     * Returns an add command string for adding the {@code company}.
     *
     * @param company The company for which the add command string is to be generated.
     * @return The add command string for adding the company.
     */
    public static String getAddCompanyCommand(Company company) {
        return AddCompanyCommand.COMMAND_WORD + " " + getCompanyDetails(company);
    }

    /**
     * Returns a delete command string for deleting the {@code company}.
     *
     * @param company The company for which the delete command string is to be generated.
     * @return The delete command string for adding the company.
     */
    public static String getDeleteCompanyCommand(Company company) {
        return DeleteCompanyCommand.COMMAND_WORD + " " + getCompanyNameDetails(company);
    }

    /**
     * Returns an add-company-role command string for the given {@code company}.
     *
     * @param company The company for which the add-company-role command string is to be generated.
     * @return The add-company-role command string.
     */
    public static String getAddCompanyRoleCommand(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(AddCompanyRoleCommand.COMMAND_WORD).append(" ")
                .append(PREFIX_ROLE).append("Software Engineer").append(" ")
                .append(PREFIX_ROLE_DESC).append("Develops software").append(" ")
                .append(PREFIX_COMPANY).append(company.getName());
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code company}'s details.
     *
     * @param company The company for which the details string is to be generated.
     * @return The part of command string for the company's details.
     */
    public static String getCompanyDetails(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_COMPANY)
                .append(company.getName())
                .append(" ")
                .append(PREFIX_COMPANY_DESC)
                .append(company.getDescription());
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code company}'s name details.
     *
     * @param company The company for which the name details string is to be generated.
     * @return The part of command string for the company's name details.
     */
    public static String getCompanyNameDetails(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_COMPANY)
                .append(company.getName());
        return sb.toString();
    }
}
