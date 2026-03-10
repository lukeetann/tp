package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.commands.CommandTestUtil.GROUP_NAME_DESC_STUDENTS;
import static hitlist.logic.commands.CommandTestUtil.GROUP_NAME_DESC_UNEMPLOYED;
import static hitlist.logic.commands.CommandTestUtil.INVALID_GROUP_NAME_DESC;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static hitlist.logic.commands.CommandTestUtil.VALID_GROUP_NAME_STUDENTS;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.AddGroupCommand;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;

public class AddGroupCommandParserTest {
    private AddGroupCommandParser parser = new AddGroupCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Group expectedGroup = new Group(new GroupName(VALID_GROUP_NAME_STUDENTS));

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + GROUP_NAME_DESC_STUDENTS,
                new AddGroupCommand(expectedGroup));

        // simple name without extra whitespace
        assertParseSuccess(parser, GROUP_NAME_DESC_STUDENTS,
                new AddGroupCommand(expectedGroup));
    }

    @Test
    public void parse_repeatedGroupName_failure() {
        String validExpectedGroupString = GROUP_NAME_DESC_STUDENTS;

        // duplicate names
        assertParseFailure(parser, GROUP_NAME_DESC_STUDENTS + validExpectedGroupString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple names
        assertParseFailure(parser, GROUP_NAME_DESC_UNEMPLOYED + validExpectedGroupString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }


    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGroupCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_GROUP_NAME_STUDENTS, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_GROUP_NAME_DESC, GroupName.MESSAGE_CONSTRAINTS);
    }
}
