package hitlist.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ArgumentTokenizerTest {

    private final Prefix unknownPrefix = new Prefix("/u");
    private final Prefix pSlash = new Prefix("/p");
    private final Prefix dashT = new Prefix("/t");
    private final Prefix hatQ = new Prefix("/q");

    @Test
    public void tokenize_emptyArgsString_noValues() {
        String argsString = "  ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash);

        assertPreambleEmpty(argMultimap);
        assertArgumentAbsent(argMultimap, pSlash);
    }

    private void assertPreamblePresent(ArgumentMultimap argMultimap, String expectedPreamble) {
        assertEquals(expectedPreamble, argMultimap.getPreamble());
    }

    private void assertPreambleEmpty(ArgumentMultimap argMultimap) {
        assertTrue(argMultimap.getPreamble().isEmpty());
    }

    /**
     * Asserts all the arguments in {@code argMultimap} with {@code prefix} match the {@code expectedValues}
     * and only the last value is returned upon calling {@code ArgumentMultimap#getValue(Prefix)}.
     */
    private void assertArgumentPresent(ArgumentMultimap argMultimap, Prefix prefix, String... expectedValues) {

        // Verify the last value is returned
        assertEquals(expectedValues[expectedValues.length - 1], argMultimap.getValue(prefix).get());

        // Verify the number of values returned is as expected
        assertEquals(expectedValues.length, argMultimap.getAllValues(prefix).size());

        // Verify all values returned are as expected and in order
        for (int i = 0; i < expectedValues.length; i++) {
            assertEquals(expectedValues[i], argMultimap.getAllValues(prefix).get(i));
        }
    }

    private void assertArgumentAbsent(ArgumentMultimap argMultimap, Prefix prefix) {
        assertFalse(argMultimap.getValue(prefix).isPresent());
    }

    @Test
    public void tokenize_noPrefixes_allTakenAsPreamble() {
        String argsString = "  some random string /t tag with leading and trailing spaces ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString);

        // Same string expected as preamble, but leading/trailing spaces should be trimmed
        assertPreamblePresent(argMultimap, argsString.trim());
    }

    @Test
    public void tokenize_oneArgument() {
        // Preamble present with space after prefix
        String argsString = "  Some preamble string /p Argument value ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash);
        assertPreamblePresent(argMultimap, "Some preamble string");
        assertArgumentPresent(argMultimap, pSlash, "Argument value");

        // No preamble with space after prefix
        argsString = " /p   Argument value ";
        argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash);
        assertPreambleEmpty(argMultimap);
        assertArgumentPresent(argMultimap, pSlash, "Argument value");

        // Preamble present without space after prefix
        argsString = "  Some preamble string /pArgument value ";
        argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash);
        assertPreamblePresent(argMultimap, "Some preamble string");
        assertArgumentPresent(argMultimap, pSlash, "Argument value");

        // No preamble without space after prefix
        argsString = " /pArgument value ";
        argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash);
        assertPreambleEmpty(argMultimap);
        assertArgumentPresent(argMultimap, pSlash, "Argument value");
    }

    @Test
    public void tokenize_multipleArguments() {
        // Only two arguments are present
        String argsString = "SomePreambleString /t dashT-Value /p pSlash value";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash, dashT, hatQ);
        assertPreamblePresent(argMultimap, "SomePreambleString");
        assertArgumentPresent(argMultimap, pSlash, "pSlash value");
        assertArgumentPresent(argMultimap, dashT, "dashT-Value");
        assertArgumentAbsent(argMultimap, hatQ);

        // All three arguments are present with spaces
        argsString = "Different Preamble String /q 111 /t dashT-Value /p pSlash value";
        argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash, dashT, hatQ);
        assertPreamblePresent(argMultimap, "Different Preamble String");
        assertArgumentPresent(argMultimap, pSlash, "pSlash value");
        assertArgumentPresent(argMultimap, dashT, "dashT-Value");
        assertArgumentPresent(argMultimap, hatQ, "111");

        // All three arguments are present without spaces
        argsString = "Different Preamble String /q111 /tdashT-Value /ppSlash value";
        argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash, dashT, hatQ);
        assertPreamblePresent(argMultimap, "Different Preamble String");
        assertArgumentPresent(argMultimap, pSlash, "pSlash value");
        assertArgumentPresent(argMultimap, dashT, "dashT-Value");
        assertArgumentPresent(argMultimap, hatQ, "111");

        /* Also covers: Reusing of the tokenizer multiple times */

        // Reuse tokenizer on an empty string to ensure ArgumentMultimap is correctly reset
        // (i.e. no stale values from the previous tokenizing remain)
        argsString = "";
        argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash, dashT, hatQ);
        assertPreambleEmpty(argMultimap);
        assertArgumentAbsent(argMultimap, pSlash);

        /* Also covers: testing for prefixes not specified as a prefix */

        // Prefixes not previously given to the tokenizer should not return any values
        argsString = unknownPrefix + " some value";
        argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash, dashT, hatQ);
        assertArgumentAbsent(argMultimap, unknownPrefix);
        assertPreamblePresent(argMultimap, argsString); // Unknown prefix is taken as part of preamble
    }

    @Test
    public void tokenize_multipleArgumentsWithRepeats() {
        // Two arguments repeated, some have empty values
        String argsString = "SomePreambleString /t dashT-Value /q /q /t another dashT value /p pSlash value /t";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash, dashT, hatQ);
        assertPreamblePresent(argMultimap, "SomePreambleString");
        assertArgumentPresent(argMultimap, pSlash, "pSlash value");
        assertArgumentPresent(argMultimap, dashT, "dashT-Value", "another dashT value", "");
        assertArgumentPresent(argMultimap, hatQ, "", "");
    }

    @Test
    public void tokenize_multipleArgumentsJoined() {
        String argsString = "SomePreambleString/p pSlash joined/tjoined /t not joined/qjoined";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, pSlash, dashT, hatQ);
        assertPreamblePresent(argMultimap, "SomePreambleString/p pSlash joined/tjoined");
        assertArgumentAbsent(argMultimap, pSlash);
        assertArgumentPresent(argMultimap, dashT, "not joined/qjoined");
        assertArgumentAbsent(argMultimap, hatQ);
    }

    @Test
    public void tokenize_mixedPrefixStyles() {
        // Mix of spaced and unspaced prefixes
        String argsString = "add /nJohn Doe /p 91234567 /e john@example.com /a123 Main St /t friends /tfamily";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString,
                new Prefix("/n"), new Prefix("/p"), new Prefix("/e"), new Prefix("/a"), new Prefix("/t"));

        assertPreamblePresent(argMultimap, "add");
        assertArgumentPresent(argMultimap, new Prefix("/n"), "John Doe");
        assertArgumentPresent(argMultimap, new Prefix("/p"), "91234567");
        assertArgumentPresent(argMultimap, new Prefix("/e"), "john@example.com");
        assertArgumentPresent(argMultimap, new Prefix("/a"), "123 Main St");
        assertArgumentPresent(argMultimap, new Prefix("/t"), "friends", "family");
    }

    @Test
    public void tokenize_prefixAtStart() {
        // Prefix at the very beginning of the string
        String argsString = "/n John Doe /p 91234567";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString,
                new Prefix("/n"), new Prefix("/p"));

        assertPreambleEmpty(argMultimap);
        assertArgumentPresent(argMultimap, new Prefix("/n"), "John Doe");
        assertArgumentPresent(argMultimap, new Prefix("/p"), "91234567");
    }

    @Test
    public void tokenize_prefixWithNoValue() {
        // Prefix with no value (should capture empty string)
        String argsString = "add /n John Doe /p /e john@example.com";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString,
                new Prefix("/n"), new Prefix("/p"), new Prefix("/e"));

        assertPreamblePresent(argMultimap, "add");
        assertArgumentPresent(argMultimap, new Prefix("/n"), "John Doe");
        assertArgumentPresent(argMultimap, new Prefix("/p"), "");
        assertArgumentPresent(argMultimap, new Prefix("/e"), "john@example.com");
    }

    @Test
    public void equalsMethod() {
        Prefix aaa = new Prefix("/aaa");

        assertEquals(aaa, aaa);
        assertEquals(aaa, new Prefix("/aaa"));

        assertNotEquals(aaa, "/aaa");
        assertNotEquals(aaa, new Prefix("/aab"));
    }

}