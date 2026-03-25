package hitlist.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import hitlist.commons.core.GuiSettings;
import hitlist.model.company.Company;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.Role;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.person.Name;
import hitlist.model.person.Person;
import javafx.collections.ObservableList;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Company> PREDICATE_SHOW_ALL_COMPANIES = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' hit list file path.
     */
    Path getHitListFilePath();

    /**
     * Sets the user prefs' hit list file path.
     */
    void setHitListFilePath(Path addressBookFilePath);

    /**
     * Replaces hit list data with the data in {@code hitList}.
     */
    void setHitList(ReadOnlyHitList hitList);

    /** Returns the HitList */
    ReadOnlyHitList getHitList();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the hit list.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the hit list.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the hit list.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the hit list.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the hit list.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the group list */
    ObservableList<Group> getGroupList();

    /** Returns an unmodifiable view of the filtered company list */
    ObservableList<Company> getFilteredCompanyList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Updates the filter of the filtered company list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredCompanyList(Predicate<Company> predicate);

    /**
     * Returns true if a group with the same identity as {@code group} exists.
     */
    boolean hasGroup(Group group);

    /**
     * Returns an {@code Optional} containing the group with the same identity as {@code groupName} if it exists,
     * or an empty {@code Optional} otherwise.
     */
    Optional<Group> getGroup(GroupName groupName);

    /**
     * Adds the given group.
     * {@code group} must not already exist.
     */
    void addGroup(Group group);

    /**
     * Deletes the given group.
     * {@code group} must already exist.
     */
    void deleteGroup(Group group);
    /**
     * Returns all persons whose names exactly match {@code name}.
     */
    List<Person> getPersonsByName(Name name);

    /**
     * Returns true if a company with the same identity as {@code company} exists.
     */
    boolean hasCompany(Company company);

    /**
    * Adds the given company.
    * {@code company} must not already exist.
    */
    void addCompany(Company company);

    /**
     * Returns an {@code Optional} containing the company with the same identity as {@code companyName} if it exists,
     * or an empty {@code Optional} otherwise.
     */
    Optional<Company> getCompany(CompanyName companyName);

    /**
     * Deletes the given company.
     * {@code company} must already exist.
     */
    void deleteCompany(Company company);

    /**
     * Returns true if a company role with the same identity as {@code role} exists.
     */
    boolean hasCompanyRole(CompanyName companyName, Role role);

    /**
     * Adds the given company role to the company with the given company name.
     * {@code companyName} must already exist.
     * {@code role} must not already exist for the company.
     */
    void addCompanyRole(CompanyName companyName, Role role);

    /**
     * Returns an {@code Optional} containing the company role with the same identity as {@code roleName}
     * for the company with the given company name if it exists,
     * or an empty {@code Optional} otherwise.
     */
    Optional<Role> getCompanyRole(CompanyName companyName, String roleName);

    /**
     * Replaces the given company role {@code target} with {@code editedRole} in the company with the given
     * company name.
     * {@code companyName} must already exist.
     * {@code target} must already exist for the company.
     * The company role identity of {@code editedRole} must not be the same as another existing company role
     * for the company.
     */
    void setCompanyRole(CompanyName companyName, Role target, Role editedRole);

    /**
     * Deletes the given company role from the company with the given company name.
     * {@code companyName} must already exist.
     * {@code role} must already exist for the company.
     */
    void deleteCompanyRole(CompanyName companyName, Role role);
}
