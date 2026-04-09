package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import hitlist.logic.commands.FindCommand;
import hitlist.model.person.Name;
import hitlist.model.person.PersonMatchesFindPredicate;

public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameKeywords_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("Alice", "Bob")));

        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);
        assertParseSuccess(parser, " \n Alice \n \t Bob \t", expectedFindCommand);
    }

    @Test
    public void parse_validNameWithHyphen_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("Jean-Luc", "Mary-Jane")));

        assertParseSuccess(parser, "Jean-Luc Mary-Jane", expectedFindCommand);
    }

    @Test
    public void parse_validNameWithApostrophe_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("O'Connor", "D'Souza")));

        assertParseSuccess(parser, "O'Connor D'Souza", expectedFindCommand);
    }

    @Test
    public void parse_validSingleCharacterKeyword_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("J", "A")));

        assertParseSuccess(parser, "J A", expectedFindCommand);
    }

    @Test
    public void parse_invalidKeywordWithSpecialCharacters_throwsParseException() {
        // Test each invalid keyword individually
        String expectedMessageForEquals = "Invalid search keyword: '='\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "=", expectedMessageForEquals);

        String expectedMessageForCurlyBrace = "Invalid search keyword: '{'\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "{", expectedMessageForCurlyBrace);

        String expectedMessageForBracket = "Invalid search keyword: '['\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "[", expectedMessageForBracket);

        // Test with valid keyword followed by invalid
        String expectedMessageForEqualsInMiddle = "Invalid search keyword: '='\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "John =", expectedMessageForEqualsInMiddle);
    }

    @Test
    public void parse_invalidKeywordWithNumbers_throwsParseException() {
        String expectedMessage = "Invalid search keyword: 'John123'\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "John123", expectedMessage);

        String expectedMessageForNumbers = "Invalid search keyword: '123'\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "123", expectedMessageForNumbers);
    }

    @Test
    public void parse_invalidKeywordWithPrefixSlash_throwsParseException() {
        String expectedMessage = "Invalid search keyword: '/n'\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "/n", expectedMessage);

        String expectedMessageForSuffixSlash = "Invalid search keyword: 'n/'\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "n/", expectedMessageForSuffixSlash);
    }

    @Test
    public void parse_mixedValidAndInvalidKeywords_throwsParseException() {
        String expectedMessage = "Invalid search keyword: '='\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "John = Doe", expectedMessage);
        assertParseFailure(parser, "= John Doe", expectedMessage);
        assertParseFailure(parser, "John Doe =", expectedMessage);
    }

    @Test
    public void parse_keywordWithOnlySpaces_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleSpacesBetweenKeywords_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("John", "Doe")));

        assertParseSuccess(parser, "John   Doe", expectedFindCommand);
    }

    @Test
    public void parse_singleKeyword_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("Alice")));

        assertParseSuccess(parser, "Alice", expectedFindCommand);
    }

    @Test
    public void parse_keywordWithLeadingAndTrailingSpaces_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindPredicate(Arrays.asList("Alice", "Bob")));

        assertParseSuccess(parser, "  Alice   Bob   ", expectedFindCommand);
    }

    @Test
    public void parse_keywordWithInvalidCharacterInside_throwsParseException() {
        String expectedMessage = "Invalid search keyword: 'John=Doe'\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "John=Doe", expectedMessage);

        expectedMessage = "Invalid search keyword: 'John@Doe'\n"
                + "Search keywords must be valid names. " + Name.MESSAGE_CONSTRAINTS + "\n"
                + "Example: find John Doe";
        assertParseFailure(parser, "John@Doe", expectedMessage);
    }
}
