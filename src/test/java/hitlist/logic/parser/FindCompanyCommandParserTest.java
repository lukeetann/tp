package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import hitlist.logic.commands.FindCompanyCommand;
import hitlist.model.company.CompanyMatchesFindPredicate;

public class FindCompanyCommandParserTest {

    private final FindCompanyCommandParser parser = new FindCompanyCommandParser();

    /**
     * Helper method to get the exact expected error message for an invalid keyword.
     */
    private String getExpectedErrorMessage(String invalidKeyword) {
        return String.format(FindCompanyCommand.MESSAGE_INVALID_KEYWORD, invalidKeyword,
                FindCompanyCommand.SEARCH_KEYWORD_CONSTRAINTS);
    }

    /**
     * Uses reflection to invoke the private isValidSearchKeyword method directly,
     * so that all internal validation branches can be covered.
     */
    private boolean invokeIsValidSearchKeyword(String keyword) throws Exception {
        Method method = FindCompanyCommandParser.class.getDeclaredMethod("isValidSearchKeyword", String.class);
        method.setAccessible(true);
        return (boolean) method.invoke(null, keyword);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlySpaces_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_tabCharactersOnly_throwsParseException() {
        assertParseFailure(parser, "\t\t\t",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_newlineCharactersOnly_throwsParseException() {
        assertParseFailure(parser, "\n\n\n",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_mixedWhitespaceOnly_throwsParseException() {
        assertParseFailure(parser, " \t\n \t\n ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_singleCharacterUppercaseLetter_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("X")));
        assertParseSuccess(parser, "X", expectedCommand);
    }

    @Test
    public void parse_singleCharacterLowercaseLetter_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("x")));
        assertParseSuccess(parser, "x", expectedCommand);
    }

    @Test
    public void parse_singleCharacterDigit_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("9")));
        assertParseSuccess(parser, "9", expectedCommand);
    }

    @Test
    public void parse_singleCharacterWithWhitespace_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("X")));
        assertParseSuccess(parser, "  X  ", expectedCommand);
        assertParseSuccess(parser, "\tX\n", expectedCommand);
    }

    @Test
    public void parse_singleCharacterSpecialSymbol_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("@")));
        assertParseSuccess(parser, "@", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("#")));
        assertParseSuccess(parser, "#", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("$")));
        assertParseSuccess(parser, "$", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("&")));
        assertParseSuccess(parser, "&", expectedCommand);
    }

    @Test
    public void parse_singleCharacterUnicodeLetter_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("ñ")));
        assertParseSuccess(parser, "ñ", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("ç")));
        assertParseSuccess(parser, "ç", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("é")));
        assertParseSuccess(parser, "é", expectedCommand);
    }

    @Test
    public void parse_singleCharacterWhitespace_throwsParseException() {
        assertParseFailure(parser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_singleCharacterForwardSlash_throwsParseException() {
        assertParseFailure(parser, "/", getExpectedErrorMessage("/"));
    }

    @Test
    public void parse_singleCharacterBackslash_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("\\")));
        assertParseSuccess(parser, "\\", expectedCommand);
    }

    @Test
    public void parse_twoCharacterValidKeyword_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Go")));
        assertParseSuccess(parser, "Go", expectedCommand);
    }

    @Test
    public void parse_twoCharacterKeywordWithSpecialChar_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("G@")));
        assertParseSuccess(parser, "G@", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("G#")));
        assertParseSuccess(parser, "G#", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("G$")));
        assertParseSuccess(parser, "G$", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("G&")));
        assertParseSuccess(parser, "G&", expectedCommand);
    }

    @Test
    public void parse_twoCharacterKeywordWithForwardSlash_throwsParseException() {
        assertParseFailure(parser, "G/", getExpectedErrorMessage("G/"));
        assertParseFailure(parser, "/G", getExpectedErrorMessage("/G"));
    }

    @Test
    public void parse_twoCharacterKeywordWithBackslash_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("G\\")));
        assertParseSuccess(parser, "G\\", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("\\G")));
        assertParseSuccess(parser, "\\G", expectedCommand);
    }

    @Test
    public void parse_validCompanyName_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google")));
        assertParseSuccess(parser, "Google", expectedCommand);

        FindCompanyCommand expectedCommandNoSpace =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("MetaPlatforms")));
        assertParseSuccess(parser, "MetaPlatforms", expectedCommandNoSpace);

        FindCompanyCommand expectedCommandWithNumbers =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("24x7")));
        assertParseSuccess(parser, "24x7", expectedCommandWithNumbers);

        FindCompanyCommand expectedCommandSpecial =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("AT&T")));
        assertParseSuccess(parser, "AT&T", expectedCommandSpecial);

        FindCompanyCommand expectedCommandHyphen =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("T-Mobile")));
        assertParseSuccess(parser, "T-Mobile", expectedCommandHyphen);
    }

    @Test
    public void parse_validCompanyNameWithSpecialChars_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("AT&T")));
        assertParseSuccess(parser, "AT&T", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Johnson&Johnson")));
        assertParseSuccess(parser, "Johnson&Johnson", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("C#Corp")));
        assertParseSuccess(parser, "C#Corp", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google@Meta")));
        assertParseSuccess(parser, "Google@Meta", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google$Meta")));
        assertParseSuccess(parser, "Google$Meta", expectedCommand);
    }

    @Test
    public void parse_validCompanyNameWithHyphen_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("AT-T")));
        assertParseSuccess(parser, "AT-T", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("T-Mobile")));
        assertParseSuccess(parser, "T-Mobile", expectedCommand);
    }

    @Test
    public void parse_companyNameWithForwardSlash_throwsParseException() {
        assertParseFailure(parser, "Google/Meta", getExpectedErrorMessage("Google/Meta"));
        assertParseFailure(parser, "/Google", getExpectedErrorMessage("/Google"));
        assertParseFailure(parser, "Google/", getExpectedErrorMessage("Google/"));
    }

    @Test
    public void parse_companyNameWithBackslash_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google\\Meta")));
        assertParseSuccess(parser, "Google\\Meta", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("\\Google")));
        assertParseSuccess(parser, "\\Google", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google\\")));
        assertParseSuccess(parser, "Google\\", expectedCommand);
    }

    @Test
    public void parse_multipleValidKeywords_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google", "Meta", "Apple")));
        assertParseSuccess(parser, "Google Meta Apple", expectedCommand);
    }

    @Test
    public void parse_multipleKeywordsWithWhitespace_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google", "Meta")));

        assertParseSuccess(parser, "Google   Meta", expectedCommand);
        assertParseSuccess(parser, " \n Google \n \t Meta \t", expectedCommand);
        assertParseSuccess(parser, "  Google   Meta   ", expectedCommand);
    }

    @Test
    public void parse_mixedSingleAndMultiCharKeywords_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("X", "Google", "Y", "Meta", "1")));
        assertParseSuccess(parser, "X Google Y Meta 1", expectedCommand);
    }

    @Test
    public void parse_mixedValidAndInvalidKeywords_throwsParseException() {
        assertParseFailure(parser, "Google /n Meta", getExpectedErrorMessage("/n"));
        assertParseFailure(parser, "/n Google Meta", getExpectedErrorMessage("/n"));
        assertParseFailure(parser, "Google Meta /n", getExpectedErrorMessage("/n"));

        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google", "@", "Meta")));
        assertParseSuccess(parser, "Google @ Meta", expectedCommand);
    }

    @Test
    public void parse_keywordsWithConsecutiveSpaces_ignoresEmptyTokens() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google", "Meta")));

        assertParseSuccess(parser, "Google   Meta", expectedCommand);
        assertParseSuccess(parser, "Google     Meta", expectedCommand);
    }

    @Test
    public void parse_keywordsWithLeadingTrailingSpaces_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google", "Meta")));

        assertParseSuccess(parser, "  Google Meta  ", expectedCommand);
        assertParseSuccess(parser, "\tGoogle Meta\n", expectedCommand);
    }

    @Test
    public void parse_keywordWithOnlySpecialCharacters_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("@@@")));
        assertParseSuccess(parser, "@@@", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("!!!")));
        assertParseSuccess(parser, "!!!", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("$$$")));
        assertParseSuccess(parser, "$$$", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("***")));
        assertParseSuccess(parser, "***", expectedCommand);
    }

    @Test
    public void parse_keywordWithMixedSpecialAndAlphanumeric_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Hello@World")));
        assertParseSuccess(parser, "Hello@World", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Test#123")));
        assertParseSuccess(parser, "Test#123", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("A$B%")));
        assertParseSuccess(parser, "A$B%", expectedCommand);
    }

    @Test
    public void parse_keywordWithOnlyDigits_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("12345")));
        assertParseSuccess(parser, "12345", expectedCommand);
    }

    @Test
    public void parse_keywordWithSingleDigit_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("1")));
        assertParseSuccess(parser, "1", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("0")));
        assertParseSuccess(parser, "0", expectedCommand);
    }

    @Test
    public void parse_keywordWithLeadingDigit_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("7-Eleven")));
        assertParseSuccess(parser, "7-Eleven", expectedCommand);
    }

    @Test
    public void parse_caseInsensitiveHandling_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommandLower =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("google")));
        FindCompanyCommand expectedCommandUpper =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("GOOGLE")));

        assertParseSuccess(parser, "google", expectedCommandLower);
        assertParseSuccess(parser, "GOOGLE", expectedCommandUpper);
    }

    @Test
    public void parse_unicodeCharacters_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("café")));
        assertParseSuccess(parser, "café", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("株式会社")));
        assertParseSuccess(parser, "株式会社", expectedCommand);
    }

    @Test
    public void parse_veryLongKeyword_returnsFindCompanyCommand() {
        String longKeyword = "A".repeat(1000);
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList(longKeyword)));
        assertParseSuccess(parser, longKeyword, expectedCommand);
    }

    @Test
    public void parse_keywordWithEmbeddedControlCharacter_throwsParseException() {
        String keyword = "Go\u0007ogle";
        assertParseFailure(parser, keyword, getExpectedErrorMessage(keyword));
    }

    @Test
    public void parse_keywordWithEmbeddedForwardSlash_throwsParseException() {
        String invalidKeyword = "Go/ogle";
        assertParseFailure(parser, invalidKeyword, getExpectedErrorMessage(invalidKeyword));
    }

    @Test
    public void parse_emptyTokenAfterTrim_skippedSuccessfully() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google", "Meta")));

        assertParseSuccess(parser, "Google   Meta", expectedCommand);
        assertParseSuccess(parser, "Google    Meta", expectedCommand);
        assertParseSuccess(parser, "Google     Meta", expectedCommand);
    }

    @Test
    public void parse_onlyEmptyTokensAfterTrim_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " \t \n ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void isValidSearchKeyword_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            FindCompanyCommandParser.isValidSearchKeyword(null);
        });
    }

    @Test
    public void isValidSearchKeyword_emptyString_returnsFalse() throws Exception {
        assertFalse(invokeIsValidSearchKeyword(""));
    }

    @Test
    public void isValidSearchKeyword_trimmedEmpty_returnsFalse() throws Exception {
        assertFalse(invokeIsValidSearchKeyword("   "));
        assertFalse(invokeIsValidSearchKeyword("\t  \n"));
    }

    @Test
    public void isValidSearchKeyword_forwardSlash_returnsFalse() throws Exception {
        assertFalse(invokeIsValidSearchKeyword("/"));
        assertFalse(invokeIsValidSearchKeyword("abc/def"));
    }

    @Test
    public void isValidSearchKeyword_controlCharacter_returnsFalse() throws Exception {
        assertFalse(invokeIsValidSearchKeyword("Go\u0007ogle"));
        assertFalse(FindCompanyCommandParser.isValidSearchKeyword("\u0007"));
        assertFalse(FindCompanyCommandParser.isValidSearchKeyword("test\u0001keyword"));
    }

    @Test
    public void isValidSearchKeyword_verticalWhitespace_returnsFalse() throws Exception {
        assertFalse(invokeIsValidSearchKeyword("abc\u000Bdef")); // vertical tab
        assertFalse(invokeIsValidSearchKeyword("abc\fdef")); // form feed
    }

    @Test
    public void isValidSearchKeyword_singleCharacter_returnsTrue() throws Exception {
        assertTrue(invokeIsValidSearchKeyword("X"));
        assertTrue(invokeIsValidSearchKeyword("@"));
        assertTrue(invokeIsValidSearchKeyword("7"));
    }

    @Test
    public void isValidSearchKeyword_generalValidKeyword_returnsTrue() throws Exception {
        assertTrue(invokeIsValidSearchKeyword("Google"));
        assertTrue(invokeIsValidSearchKeyword("AT&T"));
        assertTrue(invokeIsValidSearchKeyword("T-Mobile"));
        assertTrue(invokeIsValidSearchKeyword("café"));
    }
}
