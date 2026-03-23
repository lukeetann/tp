package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE_DESC;

import java.util.stream.Stream;

import hitlist.logic.commands.AddCompanyRoleCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.Role;
import hitlist.model.company.role.RoleDescription;
import hitlist.model.company.role.RoleName;

/**
 * Parses input arguments and creates a new AddCompanyRoleCommand object.
 */
public class AddCompanyRoleCommandParser implements Parser<AddCompanyRoleCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCompanyRoleCommand
     * and returns an AddCompanyRoleCommand object for execution.
     *
     * @param args The input arguments to parse
     * @return AddCompanyRoleCommand object based on the parsed input
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCompanyRoleCommand parse(String args) throws ParseException {
        assert args != null : "The arguments string to parse should not be null";

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ROLE, PREFIX_ROLE_DESC, PREFIX_COMPANY);

        if (!arePrefixesPresent(argMultimap, PREFIX_ROLE, PREFIX_ROLE_DESC, PREFIX_COMPANY)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyRoleCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ROLE, PREFIX_ROLE_DESC, PREFIX_COMPANY);

        String roleNameRaw = argMultimap.getValue(PREFIX_ROLE)
                .orElseThrow(() -> new ParseException(String.format(
                        MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyRoleCommand.MESSAGE_USAGE)));
        String roleDescRaw = argMultimap.getValue(PREFIX_ROLE_DESC)
                .orElseThrow(() -> new ParseException(String.format(
                        MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyRoleCommand.MESSAGE_USAGE)));
        String companyNameRaw = argMultimap.getValue(PREFIX_COMPANY)
                .orElseThrow(() -> new ParseException(String.format(
                        MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyRoleCommand.MESSAGE_USAGE)));

        RoleName roleName = ParserUtil.parseRoleName(roleNameRaw);
        RoleDescription roleDesc = ParserUtil.parseRoleDescription(roleDescRaw);
        CompanyName companyName = ParserUtil.parseCompanyName(companyNameRaw);

        Role role = new Role(roleName, roleDesc);
        return new AddCompanyRoleCommand(role, companyName);
    }

    /**
     * Returns true if all prefixes contain non-empty {@code Optional} values in
     * the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
