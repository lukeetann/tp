package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.commands.CommandTestUtil.GROUP_NAME_DESC_STUDENTS;
import static hitlist.logic.commands.CommandTestUtil.GROUP_NAME_DESC_UNEMPLOYED;
import static hitlist.logic.commands.CommandTestUtil.INVALID_GROUP_NAME_DESC;
import static hitlist.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static hitlist.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static hitlist.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static hitlist.logic.commands.CommandTestUtil.VALID_GROUP_NAME_STUDENTS;
import static hitlist.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.AssignGroupCommand;
import hitlist.model.group.GroupName;
import hitlist.model.person.Name;

public class AssignGroupCommandParserTest {

    private AssignGroupCommandParser parser = new AssignGroupCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        AssignGroupCommand expectedCommand =
                new AssignGroupCommand(new Name(VALID_NAME_AMY), new GroupName(VALID_GROUP_NAME_STUDENTS));

        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_AMY + GROUP_NAME_DESC_STUDENTS,
                expectedCommand);

        assertParseSuccess(parser, GROUP_NAME_DESC_STUDENTS + NAME_DESC_AMY,
                expectedCommand);
    }

    @Test
    public void parse_repeatedPrefixes_failure() {
        assertParseFailure(parser, NAME_DESC_AMY + NAME_DESC_BOB + GROUP_NAME_DESC_STUDENTS,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        assertParseFailure(parser, NAME_DESC_AMY + GROUP_NAME_DESC_STUDENTS + GROUP_NAME_DESC_UNEMPLOYED,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GROUP));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignGroupCommand.MESSAGE_USAGE);

        assertParseFailure(parser, NAME_DESC_AMY, expectedMessage);
        assertParseFailure(parser, GROUP_NAME_DESC_STUDENTS, expectedMessage);
        assertParseFailure(parser, VALID_NAME_AMY + " " + VALID_GROUP_NAME_STUDENTS, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_NAME_DESC + GROUP_NAME_DESC_STUDENTS, Name.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, NAME_DESC_AMY + INVALID_GROUP_NAME_DESC, GroupName.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_AMY + GROUP_NAME_DESC_STUDENTS,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignGroupCommand.MESSAGE_USAGE));
    }
}
