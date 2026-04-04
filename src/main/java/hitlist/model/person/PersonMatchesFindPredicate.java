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

    /**
     * Creates a predicate with the given name keywords.
     */
    public PersonMatchesFindPredicate(List<String> nameKeywords) {
        this.nameKeywords = nameKeywords;
    }

    @Override
    public boolean test(Person person) {
        boolean matchesName = nameKeywords.isEmpty()
                || nameKeywords.stream()
                .anyMatch(keyword ->
                        containsIgnoreCase(person.getName().fullName, keyword));

        return matchesName;
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
        return nameKeywords.equals(otherPredicate.nameKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .toString();
    }

    /**
     * Returns true if the given string contains the search string, ignoring case.
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toLowerCase().contains(searchStr.toLowerCase());
    }
}
