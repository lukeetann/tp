package hitlist.model.group;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;

import hitlist.commons.util.ToStringBuilder;
import hitlist.model.person.Person;
import hitlist.model.person.UniquePersonList;

/**
 * Represents a Contact Group.
 */
public class Group {
    private final GroupName name;
    private UniquePersonList members;

    /**
     * Every field must be present and not null.
     */
    public Group(GroupName name) {
        requireAllNonNull(name);
        this.name = name;
        this.members = new UniquePersonList();
    }

    public GroupName getName() {
        return name;
    }

    public UniquePersonList getMembers() {
        return members;
    }

    public boolean hasMember(Person person) {
        return members.contains(person);
    }

    public void addMember(Person person) {
        members.add(person);
    }

    public void removeMember(Person person) {
        members.remove(person);
    }

    /**
     * Returns true if both groups have the same name.
     */
    public boolean isSameGroup(Group otherGroup) {
        if (otherGroup == this) {
            return true;
        }

        return otherGroup != null
                && otherGroup.getName().equals(getName());
    }

    /**
     * Returns true if both groups have the same identity and data fields.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Group)) {
            return false;
        }

        Group otherGroup = (Group) other;
        return name.equals(otherGroup.name)
               && members.equals(otherGroup.members);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .toString();
    }
}
