package hitlist.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import hitlist.commons.util.ToStringBuilder;
import hitlist.model.company.Company;
import hitlist.model.company.UniqueCompanyList;
import hitlist.model.company.role.Role;
import hitlist.model.company.role.UniqueRoleList;
import hitlist.model.group.Group;
import hitlist.model.group.UniqueGroupList;
import hitlist.model.person.Person;
import hitlist.model.person.UniquePersonList;
import javafx.collections.ObservableList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class HitList implements ReadOnlyHitList {

    private final UniquePersonList persons;
    private final UniqueRoleList roles;
    private final UniqueCompanyList companies;
    private final UniqueGroupList groups;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        roles = new UniqueRoleList();
        companies = new UniqueCompanyList();
        groups = new UniqueGroupList();
    }

    public HitList() {}

    /**
     * Creates an HitList using the Persons in the {@code toBeCopied}
     */
    public HitList(ReadOnlyHitList toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the role list with {@code roles}.
     * {@code roles} must not contain duplicate roles.
     */
    public void setRoles(List<Role> roles) {
        this.roles.setRoles(roles);
    }

    /**
    * Replaces the contents of the company list with {@code companies}.
    * {@code companies} must not contain duplicate companies.
    */
    public void setCompanies(List<Company> companies) {
        this.companies.setCompanies(companies);
    }

    /**
     * Resets the existing data of this {@code HitList} with {@code newData}.
     */
    public void resetData(ReadOnlyHitList newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setRoles(newData.getRoleList());
        setCompanies(newData.getCompanyList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in hitList.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to hitList.
     * The person must not already exist in hitList.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in hitList.
     * The person identity of {@code editedPerson} must not be the same as another existing person in hitList.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code HitList}.
     * {@code key} must exist in hitList.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// role-level operations

    /**
     * Returns true if a role with the same identity as {@code role} exists in hitList.
     */
    public boolean hasRole(Role role) {
        requireNonNull(role);
        return roles.contains(role);
    }

    /**
     * Adds a role to hitList.
     * The role must not already exist in hitList.
     */
    public void addRole(Role r) {
        roles.add(r);
    }

    /**
     * Replaces the given role {@code target} in the list with {@code editedRole}.
     * {@code target} must exist in hitList.
     * The role identity of {@code editedRole} must not be the same as another existing role in hitList.
     */
    public void setRole(Role target, Role editedRole) {
        requireNonNull(editedRole);

        roles.setRole(target, editedRole);
    }

    /**
     * Removes {@code key} from this {@code HitList}.
     * {@code key} must exist in hitList.
     */
    public void removeRole(Role key) {
        roles.remove(key);
    }

    //// company-level operations

    /**
     * Returns true if a company with the same identity as {@code company} exists in hitList.
     */
    public boolean hasCompany(Company company) {
        requireNonNull(company);
        return companies.contains(company);
    }

    /**
     * Adds a company to hitList.
     * The company must not already exist in hitList.
     */
    public void addCompany(Company c) {
        companies.add(c);
    }

    /**
     * Replaces the given company {@code target} in the list with {@code editedCompany}.
     * {@code target} must exist in hitList.
     * The company identity of {@code editedCompany} must not be the same as another existing company in hitList.
     */
    public void setCompany(Company target, Company editedCompany) {
        requireNonNull(editedCompany);

        companies.setCompany(target, editedCompany);
    }

    /**
     * Removes {@code key} from this {@code HitList}.
     * {@code key} must exist in hitList.
     */
    public void removeCompany(Company key) {
        companies.remove(key);
    }

    /**
     * Returns true if a group with the same identity as {@code group} exists.
     */
    boolean hasGroup(Group group) {
        requireNonNull(group);
        return groups.contains(group);
    }

    /**
     * Adds a group to hitList.
     * The group must not already exist in hitList.
     */
    public void addGroup(Group g) {
        groups.add(g);
    }

    /**
     * Deletes the given group.
     * {@code group} must already exist.
     */
    void deleteGroup(Group group) {
        groups.remove(group);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("roles", roles)
                .add("companies", companies)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Role> getRoleList() {
        return roles.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Company> getCompanyList() {
        return companies.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof HitList)) {
            return false;
        }

        HitList otherAddressBook = (HitList) other;
        return persons.equals(otherAddressBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
