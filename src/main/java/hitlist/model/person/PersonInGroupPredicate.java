package hitlist.model.person;

import java.util.function.Predicate;

import hitlist.commons.util.ToStringBuilder;
import hitlist.model.group.Group;

/**
 * Tests whether a {@code Person} belongs in a {@code Group}.
 */
public class PersonInGroupPredicate implements Predicate<Person> {
    private final Group group;

    public PersonInGroupPredicate(Group group) {
        this.group = group;
    }

    @Override
    public boolean test(Person person) {
        return group.getMembers().contains(person);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PersonInGroupPredicate)) {
            return false;
        }
        PersonInGroupPredicate otherPredicate = (PersonInGroupPredicate) other;
        return group.equals(otherPredicate.group);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("group", group)
                .toString();
    }
}
