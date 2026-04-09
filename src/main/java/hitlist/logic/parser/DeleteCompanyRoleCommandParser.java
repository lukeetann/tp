package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE;

import java.util.Optional;

import hitlist.commons.core.index.Index;
import hitlist.logic.commands.DeleteCompanyRoleCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.RoleName;

/**
 * Parses input arguments and creates a new DeleteCompanyRoleCommand object.
 */
public class DeleteCompanyRoleCommandParser implements Parser<DeleteCompanyRoleCommand> {

    @Override
    public DeleteCompanyRoleCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ROLE, PREFIX_COMPANY);
        String preamble = argMultimap.getPreamble().trim();

        Optional<String> companyNameValue = argMultimap.getValue(PREFIX_COMPANY);
        if (companyNameValue.isEmpty() || companyNameValue.get().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteCompanyRoleCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_COMPANY, PREFIX_ROLE);
        CompanyName companyName = ParserUtil.parseCompanyName(companyNameValue.get());

        Optional<String> roleNameValue = argMultimap.getValue(PREFIX_ROLE);

        if (roleNameValue.isPresent() && preamble.isEmpty()) { // use role name as identifier
            RoleName roleName = ParserUtil.parseRoleName(roleNameValue.get());
            return new DeleteCompanyRoleCommand(roleName, companyName);
        }

        if (roleNameValue.isEmpty() && !preamble.isEmpty()) { // use index as identifier
            try {
                Index roleIndex = ParserUtil.parseIndex(preamble);
                return new DeleteCompanyRoleCommand(roleIndex, companyName);
            } catch (ParseException pe) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        DeleteCompanyRoleCommand.MESSAGE_USAGE), pe);
            }
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCompanyRoleCommand.MESSAGE_USAGE));
    }
}
