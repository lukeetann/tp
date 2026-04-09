package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.commands.CommandTestUtil.COMPANY_NAME_DESC_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.INVALID_COMPANY_NAME;
import static hitlist.logic.commands.CommandTestUtil.INVALID_ROLE_NAME;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_NAME_PRODUCT_MANAGER;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_NAME_SOFTWARE_ENGINEER;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hitlist.commons.core.index.Index;
import hitlist.logic.Messages;
import hitlist.logic.commands.DeleteCompanyRoleCommand;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.RoleName;

public class DeleteCompanyRoleCommandParserTest {

    private final DeleteCompanyRoleCommandParser parser = new DeleteCompanyRoleCommandParser();

    @Test
    public void parse_validRoleName_success() {
        String userInput = PREFIX_ROLE + VALID_ROLE_NAME_SOFTWARE_ENGINEER + " " + COMPANY_NAME_DESC_GOOGLE;
        DeleteCompanyRoleCommand expectedCommand = new DeleteCompanyRoleCommand(
                new RoleName(VALID_ROLE_NAME_SOFTWARE_ENGINEER),
                new CompanyName(VALID_COMPANY_NAME_GOOGLE));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validRoleIndex_success() {
        String userInput = "1" + " " + COMPANY_NAME_DESC_GOOGLE;
        DeleteCompanyRoleCommand expectedCommand = new DeleteCompanyRoleCommand(
                Index.fromOneBased(1),
                new CompanyName(VALID_COMPANY_NAME_GOOGLE));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validRoleIndexWithWhitespace_success() {
        String userInput = "   5   " + COMPANY_NAME_DESC_GOOGLE;
        DeleteCompanyRoleCommand expectedCommand = new DeleteCompanyRoleCommand(
                Index.fromOneBased(5),
                new CompanyName(VALID_COMPANY_NAME_GOOGLE));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_reverseOrder_success() {
        String userInput = COMPANY_NAME_DESC_GOOGLE + " " + PREFIX_ROLE + VALID_ROLE_NAME_SOFTWARE_ENGINEER;
        DeleteCompanyRoleCommand expectedCommand = new DeleteCompanyRoleCommand(
                new RoleName(VALID_ROLE_NAME_SOFTWARE_ENGINEER),
                new CompanyName(VALID_COMPANY_NAME_GOOGLE));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompanyPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCompanyRoleCommand.MESSAGE_USAGE);

        assertParseFailure(parser, PREFIX_ROLE + VALID_ROLE_NAME_SOFTWARE_ENGINEER,
                expectedMessage);
        assertParseFailure(parser, "1", expectedMessage);
    }

    @Test
    public void parse_missingCompanyName_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCompanyRoleCommand.MESSAGE_USAGE);

        assertParseFailure(parser, PREFIX_ROLE + VALID_ROLE_NAME_SOFTWARE_ENGINEER + " " + PREFIX_COMPANY,
                expectedMessage);
        assertParseFailure(parser, "1 " + PREFIX_COMPANY, expectedMessage);
    }

    @Test
    public void parse_missingRoleSpecification_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCompanyRoleCommand.MESSAGE_USAGE);

        assertParseFailure(parser, COMPANY_NAME_DESC_GOOGLE, expectedMessage);
    }

    @Test
    public void parse_invalidCompanyName_failure() {
        String expectedMessage = CompanyName.MESSAGE_CONSTRAINTS;

        assertParseFailure(parser,
                PREFIX_ROLE + VALID_ROLE_NAME_SOFTWARE_ENGINEER + " " + PREFIX_COMPANY + INVALID_COMPANY_NAME,
                expectedMessage);
        assertParseFailure(parser,
                "1 " + PREFIX_COMPANY + INVALID_COMPANY_NAME,
                expectedMessage);
    }

    @Test
    public void parse_invalidRoleName_failure() {
        String expectedMessage = RoleName.MESSAGE_CONSTRAINTS;

        assertParseFailure(parser,
                PREFIX_ROLE + INVALID_ROLE_NAME + " " + COMPANY_NAME_DESC_GOOGLE,
                expectedMessage);
    }

    @Test
    public void parse_invalidRoleIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCompanyRoleCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "abc" + COMPANY_NAME_DESC_GOOGLE, expectedMessage);
        assertParseFailure(parser, "0" + COMPANY_NAME_DESC_GOOGLE, expectedMessage);
        assertParseFailure(parser, "-5" + COMPANY_NAME_DESC_GOOGLE, expectedMessage);
    }

    @Test
    public void parse_duplicateCompanyPrefixes_failure() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY);

        assertParseFailure(parser,
                PREFIX_ROLE + VALID_ROLE_NAME_SOFTWARE_ENGINEER
                        + COMPANY_NAME_DESC_GOOGLE
                        + " " + PREFIX_COMPANY + "Meta",
                expectedMessage);
    }

    @Test
    public void parse_duplicateRolePrefixes_failure() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE);

        assertParseFailure(parser,
                PREFIX_ROLE + VALID_ROLE_NAME_SOFTWARE_ENGINEER + " "
                        + PREFIX_ROLE + VALID_ROLE_NAME_PRODUCT_MANAGER
                        + COMPANY_NAME_DESC_GOOGLE,
                expectedMessage);
    }

    @Test
    public void parse_nonEmptyPreambleWithRoleName_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCompanyRoleCommand.MESSAGE_USAGE);

        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + PREFIX_ROLE + VALID_ROLE_NAME_SOFTWARE_ENGINEER
                        + COMPANY_NAME_DESC_GOOGLE,
                expectedMessage);
    }

    @Test
    public void parse_nonEmptyPreambleWithRoleIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCompanyRoleCommand.MESSAGE_USAGE);

        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + "1" + COMPANY_NAME_DESC_GOOGLE,
                expectedMessage);
    }

    @Test
    public void parse_roleNameAndRoleIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCompanyRoleCommand.MESSAGE_USAGE);

        assertParseFailure(parser,
                "1 " + PREFIX_ROLE + VALID_ROLE_NAME_SOFTWARE_ENGINEER
                        + COMPANY_NAME_DESC_GOOGLE,
                expectedMessage);
    }
}
