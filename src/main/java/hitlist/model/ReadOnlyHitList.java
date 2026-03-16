package hitlist.model;

import hitlist.model.company.Company;
import hitlist.model.company.role.Role;
import hitlist.model.person.Person;
import javafx.collections.ObservableList;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyHitList {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the roles list.
     * This list will not contain any duplicate roles.
     */
    ObservableList<Role> getRoleList();

    /**
     * Returns an unmodifiable view of the companies list.
     * This list will not contain any duplicate companies.
     */
    ObservableList<Company> getCompanyList();
}
