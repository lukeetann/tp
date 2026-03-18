package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.commands.CommandTestUtil.INVALID_COMPANY_NAME;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_GOOGLE;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.DeleteCompanyCommand;
import hitlist.model.company.CompanyName;

public class DeleteCompanyCommandParserTest {

    private final DeleteCompanyCommandParser parser = new DeleteCompanyCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCompanyCommand() {
        String userInput = " " + PREFIX_COMPANY + VALID_COMPANY_NAME_GOOGLE;
        DeleteCompanyCommand expectedCommand = new DeleteCompanyCommand(new CompanyName(VALID_COMPANY_NAME_GOOGLE));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCompanyCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "   ", expectedMessage);
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCompanyCommand.MESSAGE_USAGE);

        assertParseFailure(parser, VALID_COMPANY_NAME_GOOGLE, expectedMessage);
    }

    @Test
    public void parse_missingCompanyName_throwsParseException() {
        String expectedMessage = CompanyName.MESSAGE_CONSTRAINTS;

        assertParseFailure(parser, " " + PREFIX_COMPANY, expectedMessage);
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCompanyCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " preamble " + PREFIX_COMPANY + VALID_COMPANY_NAME_GOOGLE, expectedMessage);
    }

    @Test
    public void parse_invalidCompanyName_throwsParseException() {
        String expectedMessage = CompanyName.MESSAGE_CONSTRAINTS;

        assertParseFailure(parser, " " + PREFIX_COMPANY + INVALID_COMPANY_NAME, expectedMessage);
    }

    @Test
    public void parse_duplicateCompanyPrefix_throwsParseException() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY);

        assertParseFailure(parser,
                " " + PREFIX_COMPANY + VALID_COMPANY_NAME_GOOGLE
                        + " " + PREFIX_COMPANY + "Meta",
                expectedMessage);
    }
}
