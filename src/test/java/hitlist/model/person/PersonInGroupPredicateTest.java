package hitlist.model.person;

import static hitlist.testutil.TypicalGroups.STUDENTS;
import static hitlist.testutil.TypicalGroups.UNEMPLOYED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.testutil.PersonBuilder;


public class PersonInGroupPredicateTest {

    @Test
    public void equals() {
        PersonInGroupPredicate firstPredicate =
            new PersonInGroupPredicate(STUDENTS);
        PersonInGroupPredicate secondPredicate =
            new PersonInGroupPredicate(UNEMPLOYED);

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonInGroupPredicate firstPredicateCopy =
                new PersonInGroupPredicate(STUDENTS);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personInGroup_returnsTrue() {
        Group group = new Group(new GroupName("group"));

        Person person = new PersonBuilder().withName("Hans Gruber").build();

        group.addMember(person);

        PersonInGroupPredicate predicate =
                new PersonInGroupPredicate(group);

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_personNotInGroup_returnsTrue() {
        Group group = new Group(new GroupName("group"));

        Person person = new PersonBuilder().withName("Hans Gruber").build();

        // group.addMember(person);

        PersonInGroupPredicate predicate =
                new PersonInGroupPredicate(group);

        assertFalse(predicate.test(person));
    }
}
