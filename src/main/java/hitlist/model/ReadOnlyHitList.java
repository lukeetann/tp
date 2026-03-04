package hitlist.model;

import javafx.collections.ObservableList;
import hitlist.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyHitList {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
