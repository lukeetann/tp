package hitlist.model.person;

import java.util.List;
import java.util.function.Predicate;

import hitlist.commons.util.StringUtil;
import hitlist.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Person}'s name matches any of the given name keywords
 * and/or whether the person's tags match any of the given tag keywords.
 */
public class PersonMatchesFindPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> tagKeywords;

    /**
     * Creates a predicate with the given name and tag keywords.
     */
    public PersonMatchesFindPredicate(List<String> nameKeywords, List<String> tagKeywords) {
        this.nameKeywords = nameKeywords;
        this.tagKeywords = tagKeywords;
    }

    @Override
    public boolean test(Person person) {
        boolean matchesName = nameKeywords.isEmpty()
                || nameKeywords.stream()
                .anyMatch(keyword ->
                        StringUtil.containsPrefixIgnoreCase(person.getName().fullName, keyword));

        boolean matchesTag = tagKeywords.isEmpty()
                || person.getTags().stream()
                .map(tag -> tag.tagName)
                .anyMatch(tagName ->
                        tagKeywords.stream().anyMatch(tagName::equalsIgnoreCase));

        return matchesName && matchesTag;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PersonMatchesFindPredicate)) {
            return false;
        }
        PersonMatchesFindPredicate otherPredicate = (PersonMatchesFindPredicate) other;
        return nameKeywords.equals(otherPredicate.nameKeywords)
                && tagKeywords.equals(otherPredicate.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("tagKeywords", tagKeywords)
                .toString();
    }
}
