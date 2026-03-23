package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.commands.CommandTestUtil.COMPANY_DESC_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.COMPANY_DESC_META;
import static hitlist.logic.commands.CommandTestUtil.COMPANY_NAME_DESC_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.COMPANY_NAME_DESC_META;
import static hitlist.logic.commands.CommandTestUtil.INVALID_COMPANY_DESC;
import static hitlist.logic.commands.CommandTestUtil.INVALID_COMPANY_NAME_DESC;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_DESCRIPTION_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_GOOGLE;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY_DESC;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static hitlist.testutil.TypicalCompanies.GOOGLE;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.AddCompanyCommand;
import hitlist.model.company.CompanyDescription;
import hitlist.model.company.CompanyName;

public class AddCompanyCommandParserTest {

    private AddCompanyCommandParser parser = new AddCompanyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE
                        + COMPANY_NAME_DESC_GOOGLE
                        + COMPANY_DESC_GOOGLE,
                new AddCompanyCommand(GOOGLE));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage =
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyCommand.MESSAGE_USAGE);

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + VALID_COMPANY_NAME_GOOGLE
                        + COMPANY_DESC_GOOGLE,
                expectedMessage);

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + COMPANY_NAME_DESC_GOOGLE
                        + VALID_COMPANY_DESCRIPTION_GOOGLE,
                expectedMessage);

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + VALID_COMPANY_NAME_GOOGLE
                        + VALID_COMPANY_DESCRIPTION_GOOGLE,
                expectedMessage);
    }

    @Test
    public void parse_repeatedNonUniqueFields_failure() {
        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + COMPANY_NAME_DESC_GOOGLE
                        + COMPANY_DESC_GOOGLE
                        + COMPANY_NAME_DESC_META,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY));

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + COMPANY_NAME_DESC_GOOGLE
                        + COMPANY_DESC_GOOGLE
                        + COMPANY_DESC_META,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY_DESC));

        assertParseFailure(parser,
                PREAMBLE_WHITESPACE
                        + COMPANY_NAME_DESC_GOOGLE
                        + COMPANY_DESC_GOOGLE
                        + COMPANY_NAME_DESC_META
                        + COMPANY_DESC_META,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY, PREFIX_COMPANY_DESC));
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser,
                INVALID_COMPANY_NAME_DESC
                        + COMPANY_DESC_GOOGLE,
                CompanyName.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                COMPANY_NAME_DESC_GOOGLE
                        + INVALID_COMPANY_DESC,
                CompanyDescription.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY
                        + COMPANY_NAME_DESC_GOOGLE
                        + COMPANY_DESC_GOOGLE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyOnePrefixPresent_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyCommand.MESSAGE_USAGE);

        assertParseFailure(parser, COMPANY_NAME_DESC_GOOGLE, expectedMessage);

        assertParseFailure(parser, COMPANY_DESC_GOOGLE, expectedMessage);
    }
}
