package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import hitlist.logic.commands.FindCommand;
import hitlist.model.person.PersonMatchesFindPredicate;
import hitlist.model.tag.Tag;

public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nameKeywords_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("Alice", "Bob"),
                        Collections.emptyList()));

        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);
        assertParseSuccess(parser, " \n Alice \n \t Bob \t", expectedFindCommand);
    }

    @Test
    public void parse_tagKeywords_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Collections.emptyList(),
                        Arrays.asList("school", "friend")));

        assertParseSuccess(parser, "/tschool /tfriend", expectedFindCommand);
        assertParseSuccess(parser, " /t school \n \t /t friend \t", expectedFindCommand);
    }

    @Test
    public void parse_nameAndTagKeywords_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("Han", "Bo"),
                        Arrays.asList("school")));

        assertParseSuccess(parser, "Han Bo /tschool", expectedFindCommand);
    }

    @Test
    public void parse_tagThenNameKeywords_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("Han"),
                        Arrays.asList("school")));

        assertParseSuccess(parser, "/tschool Han", expectedFindCommand);
    }

    @Test
    public void parse_interleavedNameAndTagKeywords_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("A", "B", "C", "D"),
                        Arrays.asList("MyTag")));

        assertParseSuccess(parser, "A B /tMyTag C D", expectedFindCommand);
    }

    @Test
    public void parse_emptyTag_throwsParseException() {
        assertParseFailure(parser, "Han /t", Tag.MESSAGE_CONSTRAINTS);
    }
}
