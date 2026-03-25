package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.commands.CommandTestUtil.COMPANY_NAME_DESC_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.COMPANY_NAME_DESC_META;
import static hitlist.logic.commands.CommandTestUtil.INVALID_ROLE_DESC;
import static hitlist.logic.commands.CommandTestUtil.INVALID_ROLE_NAME_DESC;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static hitlist.logic.commands.CommandTestUtil.ROLE_DESC_PRODUCT_MANAGER;
import static hitlist.logic.commands.CommandTestUtil.ROLE_DESC_SOFTWARE_ENGINEER;
import static hitlist.logic.commands.CommandTestUtil.ROLE_NAME_DESC_PRODUCT_MANAGER;
import static hitlist.logic.commands.CommandTestUtil.ROLE_NAME_DESC_SOFTWARE_ENGINEER;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_NAME_PRODUCT_MANAGER;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE_DESC;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.AddCompanyRoleCommand;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.Role;
import hitlist.model.company.role.RoleDescription;
import hitlist.model.company.role.RoleName;

public class AddCompanyRoleCommandParserTest {

    private AddCompanyRoleCommandParser parser = new AddCompanyRoleCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Role expectedRole = new Role(
                new RoleName(VALID_ROLE_NAME_PRODUCT_MANAGER),
                new RoleDescription(VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER)
        );
        CompanyName expectedCompanyName = new CompanyName(VALID_COMPANY_NAME_GOOGLE);

        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE
                        + ROLE_NAME_DESC_PRODUCT_MANAGER
                        + ROLE_DESC_PRODUCT_MANAGER
                        + COMPANY_NAME_DESC_GOOGLE,
                new AddCompanyRoleCommand(expectedRole, expectedCompanyName));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage =
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyRoleCommand.MESSAGE_USAGE);

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + VALID_ROLE_NAME_PRODUCT_MANAGER
                        + ROLE_DESC_PRODUCT_MANAGER
                        + COMPANY_NAME_DESC_GOOGLE,
                expectedMessage);

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + ROLE_NAME_DESC_PRODUCT_MANAGER
                        + VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER
                        + COMPANY_NAME_DESC_GOOGLE,
                expectedMessage);

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + ROLE_NAME_DESC_PRODUCT_MANAGER
                        + ROLE_DESC_PRODUCT_MANAGER
                        + VALID_COMPANY_NAME_GOOGLE,
                expectedMessage);

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + VALID_ROLE_NAME_PRODUCT_MANAGER
                        + VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER
                        + VALID_COMPANY_NAME_GOOGLE,
                expectedMessage);
    }

    @Test
    public void parse_repeatedNonUniqueFields_failure() {
        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + ROLE_NAME_DESC_PRODUCT_MANAGER
                        + ROLE_DESC_PRODUCT_MANAGER
                        + COMPANY_NAME_DESC_GOOGLE
                        + ROLE_NAME_DESC_SOFTWARE_ENGINEER,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + ROLE_NAME_DESC_PRODUCT_MANAGER
                        + ROLE_DESC_PRODUCT_MANAGER
                        + COMPANY_NAME_DESC_GOOGLE
                        + ROLE_DESC_SOFTWARE_ENGINEER,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE_DESC));

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + ROLE_NAME_DESC_PRODUCT_MANAGER
                        + ROLE_DESC_PRODUCT_MANAGER
                        + COMPANY_NAME_DESC_GOOGLE
                        + COMPANY_NAME_DESC_META,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY));

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + ROLE_NAME_DESC_PRODUCT_MANAGER
                        + ROLE_DESC_PRODUCT_MANAGER
                        + COMPANY_NAME_DESC_GOOGLE
                        + ROLE_NAME_DESC_SOFTWARE_ENGINEER
                        + ROLE_DESC_SOFTWARE_ENGINEER,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE, PREFIX_ROLE_DESC));
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser,
                INVALID_ROLE_NAME_DESC
                        + ROLE_DESC_PRODUCT_MANAGER
                        + COMPANY_NAME_DESC_GOOGLE,
                RoleName.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                ROLE_NAME_DESC_PRODUCT_MANAGER
                        + INVALID_ROLE_DESC
                        + COMPANY_NAME_DESC_GOOGLE,
                RoleDescription.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY
                        + ROLE_NAME_DESC_PRODUCT_MANAGER
                        + ROLE_DESC_PRODUCT_MANAGER
                        + COMPANY_NAME_DESC_GOOGLE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyRoleCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_oneOrMorePrefixesMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyRoleCommand.MESSAGE_USAGE);

        assertParseFailure(parser, ROLE_NAME_DESC_PRODUCT_MANAGER + ROLE_DESC_PRODUCT_MANAGER, expectedMessage);

        assertParseFailure(parser, ROLE_NAME_DESC_PRODUCT_MANAGER + COMPANY_NAME_DESC_GOOGLE, expectedMessage);

        assertParseFailure(parser, ROLE_DESC_PRODUCT_MANAGER + COMPANY_NAME_DESC_GOOGLE, expectedMessage);
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser,
                "preamble /r Software Engineer /d Builds software /c Google Inc.",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyRoleCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        assertParseFailure(parser,
                " /r Software Engineer /r PM /d Builds software /c Google Inc.",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));

        assertParseFailure(parser,
                " /r Software Engineer /d Builds software /d Owns roadmap /c Google Inc.",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE_DESC));

        assertParseFailure(parser,
                " /r Software Engineer /d Builds software /c Google Inc. /c Meta",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY));
    }
}
