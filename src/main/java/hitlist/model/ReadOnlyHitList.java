package hitlist.model;

import hitlist.model.company.Company;
import hitlist.model.group.Group;
import hitlist.model.person.Person;
import javafx.collections.ObservableList;

/**
 * Unmodifiable view of an HitList
 */
public interface ReadOnlyHitList {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the companies list.
     * This list will not contain any duplicate companies.
     */
    ObservableList<Company> getCompanyList();

    /**
     * Returns an unmodifiable view of the group list.
     * This list will not contain any duplicate groups.
     */
    ObservableList<Group> getGroupList();
}
