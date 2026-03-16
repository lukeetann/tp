package hitlist.testutil;

import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY_DESC;

import hitlist.logic.commands.AddCompanyCommand;
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
}
