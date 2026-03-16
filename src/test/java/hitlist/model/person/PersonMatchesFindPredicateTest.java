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
                new PersonMatchesFindPredicate(Arrays.asList("first"),
                        Arrays.asList("school"));
        PersonMatchesFindPredicate secondPredicate =
                new PersonMatchesFindPredicate(Arrays.asList("second"),
                        Arrays.asList("friend"));

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonMatchesFindPredicate firstPredicateCopy =
                new PersonMatchesFindPredicate(Arrays.asList("first"),
                        Arrays.asList("school"));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_namePrefixMatch_returnsTrue() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("Han"),
                        Collections.emptyList());

        assertTrue(predicate.test(new PersonBuilder().withName("Hans Gruber").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Han").build()));
    }

    @Test
    public void test_nameMidWordSubstring_returnsFalse() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("ans"),
                        Collections.emptyList());

        assertFalse(predicate.test(new PersonBuilder().withName("Hans Gruber").build()));
    }

    @Test
    public void test_tagExactIgnoreCaseMatch_returnsTrue() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Collections.emptyList(),
                        Arrays.asList("school"));

        assertTrue(predicate.test(new PersonBuilder().withTags("SCHOOL").build()));
        assertTrue(predicate.test(new PersonBuilder().withTags("school").build()));
    }

    @Test
    public void test_tagPartialMatch_returnsFalse() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Collections.emptyList(),
                        Arrays.asList("sch"));

        assertFalse(predicate.test(new PersonBuilder().withTags("school").build()));
    }

    @Test
    public void test_nameAndTagBothMustMatch_returnsTrue() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("Han"),
                        Arrays.asList("school"));

        assertTrue(predicate.test(new PersonBuilder()
                .withName("Hans Gruber")
                .withTags("school")
                .build()));
    }

    @Test
    public void test_nameAndTagBothMustMatch_returnsFalseWhenNameFails() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("Han"),
                        Arrays.asList("school"));

        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Pauline")
                .withTags("school")
                .build()));
    }

    @Test
    public void test_nameAndTagBothMustMatch_returnsFalseWhenTagFails() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("Han"),
                        Arrays.asList("school"));

        assertFalse(predicate.test(new PersonBuilder()
                .withName("Hans Gruber")
                .withTags("friend")
                .build()));
    }
}
