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
}
