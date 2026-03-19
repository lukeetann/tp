package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.commands.CommandTestUtil.GROUP_NAME_DESC_STUDENTS;
import static hitlist.logic.commands.CommandTestUtil.GROUP_NAME_DESC_UNEMPLOYED;
import static hitlist.logic.commands.CommandTestUtil.INVALID_GROUP_NAME_DESC;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static hitlist.logic.commands.CommandTestUtil.VALID_GROUP_NAME_STUDENTS;
import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.DeleteGroupCommand;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;

public class DeleteGroupCommandParserTest {

    private final DeleteGroupCommandParser parser = new DeleteGroupCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Group expectedGroup = new Group(new GroupName(VALID_GROUP_NAME_STUDENTS));

        assertParseSuccess(parser, PREAMBLE_WHITESPACE + GROUP_NAME_DESC_STUDENTS,
                new DeleteGroupCommand(expectedGroup));
        assertParseSuccess(parser, GROUP_NAME_DESC_STUDENTS,
                new DeleteGroupCommand(expectedGroup));
    }

    @Test
    public void parse_repeatedGroupName_failure() {
        String validExpectedGroupString = GROUP_NAME_DESC_STUDENTS;

        assertParseFailure(parser, GROUP_NAME_DESC_STUDENTS + validExpectedGroupString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GROUP));
        assertParseFailure(parser, GROUP_NAME_DESC_UNEMPLOYED + validExpectedGroupString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GROUP));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteGroupCommand.MESSAGE_USAGE);

        assertParseFailure(parser, VALID_GROUP_NAME_STUDENTS, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_GROUP_NAME_DESC, GroupName.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, PREAMBLE_NON_EMPTY + GROUP_NAME_DESC_STUDENTS,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteGroupCommand.MESSAGE_USAGE));
    }
}
