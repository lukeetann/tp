package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.commands.CommandTestUtil.GROUP_NAME_DESC_STUDENTS;
import static hitlist.logic.commands.CommandTestUtil.GROUP_NAME_DESC_UNEMPLOYED;
import static hitlist.logic.commands.CommandTestUtil.INVALID_GROUP_NAME_DESC;
import static hitlist.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static hitlist.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static hitlist.logic.commands.CommandTestUtil.VALID_GROUP_NAME_STUDENTS;
import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.ListGroupCommand;
import hitlist.model.group.GroupName;

public class ListGroupCommandParserTest {

    private ListGroupCommandParser parser = new ListGroupCommandParser();

    @Test
    public void parse_empty_success() {
        ListGroupCommand expectedCommand = new ListGroupCommand();

        assertParseSuccess(parser, PREAMBLE_WHITESPACE, expectedCommand);
    }

    @Test
    public void parse_allFieldsPresent_success() {
        ListGroupCommand expectedCommand =
                new ListGroupCommand(new GroupName(VALID_GROUP_NAME_STUDENTS));

        assertParseSuccess(parser, PREAMBLE_WHITESPACE + GROUP_NAME_DESC_STUDENTS, expectedCommand);
    }

    @Test
    public void parse_repeatedPrefixes_failure() {
        assertParseFailure(parser, GROUP_NAME_DESC_STUDENTS + GROUP_NAME_DESC_UNEMPLOYED,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GROUP));
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_GROUP_NAME_DESC, GroupName.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListGroupCommand.MESSAGE_USAGE));
    }
}
