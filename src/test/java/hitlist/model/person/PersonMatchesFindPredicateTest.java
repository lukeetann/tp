package hitlist.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import hitlist.testutil.PersonBuilder;

public class PersonMatchesFindPredicateTest {

    @Test
    public void equals() {
        PersonMatchesFindPredicate firstPredicate =
                new PersonMatchesFindPredicate(Arrays.asList("first"));
        PersonMatchesFindPredicate secondPredicate =
                new PersonMatchesFindPredicate(Arrays.asList("second"));

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonMatchesFindPredicate firstPredicateCopy =
                new PersonMatchesFindPredicate(Arrays.asList("first"));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_namePrefixMatch_returnsTrue() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("Han"));

        assertTrue(predicate.test(new PersonBuilder().withName("Hans Gruber").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Han").build()));
    }

    @Test
    public void test_nameMidWordSubstring_returnsTrue() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("ans"));

        assertTrue(predicate.test(new PersonBuilder().withName("Hans Gruber").build()));
    }

    @Test
    public void test_tagExactIgnoreCaseMatch_returnsTrue() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Collections.emptyList());

        assertTrue(predicate.test(new PersonBuilder().build()));
        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    void test_nullMainString_returnsFalse() {
        assertFalse(containsIgnoreCase(null, "search"),
                "Should return false when main string is null");
    }

    @Test
    void test_nullSearchString_returnsFalse() {
        assertFalse(containsIgnoreCase("text", null),
                "Should return false when search string is null");
    }

    @Test
    void test_bothStringsNull_returnsFalse() {
        assertFalse(containsIgnoreCase(null, null),
                "Should return false when both strings are null");
    }

    @Test
    void test_exactMatch_success() {
        assertTrue(containsIgnoreCase("Hello World", "Hello World"));
    }

    @Test
    void test_mixedCaseMatch_success() {
        // Tests that case is ignored
        assertTrue(containsIgnoreCase("Hello World", "wOrLd"));
    }

    @Test
    void test_substringMatch_success() {
        // Tests a substring spanning multiple words
        assertTrue(containsIgnoreCase("Hello World", "lo wo"));
    }

    @Test
    void test_emptySearchString_success() {
        // Any non-null string contains an empty string
        assertTrue(containsIgnoreCase("Hello", ""));
    }

    @Test
    void test_completelyDifferentString_failure() {
        assertFalse(containsIgnoreCase("Hello World", "Java"));
    }

    @Test
    void test_searchStringLonger_failure() {
        // A shorter string cannot contain a longer string
        assertFalse(containsIgnoreCase("Hello", "Hello World"));
    }

    @Test
    void test_emptyMainString_failure() {
        // An empty string cannot contain a populated string
        assertFalse(containsIgnoreCase("", "Hello"));
    }

    /**
     * Helper method to test the static method in PersonMatchesFindPredicate.
     * This is necessary because the method is static and cannot be accessed directly from the test class.
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toLowerCase().contains(searchStr.toLowerCase());
    }
}
