package hitlist.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PersonMatchesFindPredicate predicate = new PersonMatchesFindPredicate(keywords);

        String expected = PersonMatchesFindPredicate.class.getCanonicalName() + "{nameKeywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
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
    public void test_nameDoesNotMatch_returnsFalse() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("Zebra"));

        assertFalse(predicate.test(new PersonBuilder().withName("Hans Gruber").build()));
    }

    @Test
    void test_nullMainString_returnsFalse() {
        assertFalse(PersonMatchesFindPredicate.containsIgnoreCase(null, "search"),
                "Should return false when main string is null");
    }

    @Test
    void test_nullSearchString_returnsFalse() {
        assertFalse(PersonMatchesFindPredicate.containsIgnoreCase("text", null),
                "Should return false when search string is null");
    }

    @Test
    void test_bothStringsNull_returnsFalse() {
        assertFalse(PersonMatchesFindPredicate.containsIgnoreCase(null, null),
                "Should return false when both strings are null");
    }

    @Test
    void test_exactMatch_success() {
        assertTrue(PersonMatchesFindPredicate.containsIgnoreCase("Hello World", "Hello World"));
    }

    @Test
    void test_mixedCaseMatch_success() {
        assertTrue(PersonMatchesFindPredicate.containsIgnoreCase("Hello World", "wOrLd"));
    }

    @Test
    void test_substringMatch_success() {
        assertTrue(PersonMatchesFindPredicate.containsIgnoreCase("Hello World", "lo wo"));
    }

    @Test
    void test_emptySearchString_success() {
        assertTrue(PersonMatchesFindPredicate.containsIgnoreCase("Hello", ""));
    }

    @Test
    void test_completelyDifferentString_failure() {
        assertFalse(PersonMatchesFindPredicate.containsIgnoreCase("Hello World", "Java"));
    }

    @Test
    void test_searchStringLonger_failure() {
        assertFalse(PersonMatchesFindPredicate.containsIgnoreCase("Hello", "Hello World"));
    }

    @Test
    void test_emptyMainString_failure() {
        assertFalse(PersonMatchesFindPredicate.containsIgnoreCase("", "Hello"));
    }
}
