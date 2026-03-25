package hitlist.model;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import hitlist.commons.util.ToStringBuilder;
import hitlist.model.company.Company;
import hitlist.model.company.CompanyName;
import hitlist.model.company.UniqueCompanyList;
import hitlist.model.company.exceptions.CompanyNotFoundException;
import hitlist.model.company.role.Role;
import hitlist.model.group.Group;
import hitlist.model.group.UniqueGroupList;
import hitlist.model.person.Person;
import hitlist.model.person.UniquePersonList;
import javafx.collections.ObservableList;

/**
 * Wraps all data at the HitList level
 * Duplicates are not allowed (by .isSamePerson, .isSameGroup, .isSameCompany comparison)
 */
public class HitList implements ReadOnlyHitList {

    private final UniquePersonList persons;
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
    * Replaces the contents of the company list with {@code companies}.
    * {@code companies} must not contain duplicate companies.
    */
    public void setCompanies(List<Company> companies) {
        this.companies.setCompanies(companies);
    }

    /**
    * Replaces the contents of the company list with {@code companies}.
    * {@code companies} must not contain duplicate companies.
    */
    public void setGroups(List<Group> groups) {
        this.groups.setGroups(groups);
    }

    /**
     * Resets the existing data of this {@code HitList} with {@code newData}.
     */
    public void resetData(ReadOnlyHitList newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setCompanies(newData.getCompanyList());
        setGroups(newData.getGroupList());
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

        for (Group group : groups) {
            if (group.getMembers().contains(target)) {
                group.removeMember(target);
                group.addMember(editedPerson);
            }
        }
    }

    /**
     * Removes {@code key} from this {@code HitList}.
     * {@code key} must exist in hitList.
     */
    public void removePerson(Person key) {
        for (Group group : groups) {
            if (group.getMembers().contains(key)) {
                group.removeMember(key);
            }
        }

        persons.remove(key);
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
     * Returns an {@code Optional} containing the company with the same identity as {@code companyName} if it exists,
     * or an empty {@code Optional} otherwise.
     */
    private Company findCompanyOrThrow(CompanyName companyName) {
        return companies.asUnmodifiableObservableList().stream()
                .filter(company -> company.getName().equals(companyName))
                .findFirst()
                .orElseThrow(() -> new CompanyNotFoundException(
                        String.format("Company not found: %s", companyName)));
    }

    //// role-level operations (roles are stored inside each company)

    /**
     * Returns true if a company role with the same identity as {@code role} exists
     * in the company identified by {@code companyName}.
     */
    public boolean hasCompanyRole(CompanyName companyName, Role role) {
        requireAllNonNull(companyName, role);
        Company company = findCompanyOrThrow(companyName);
        return company.hasRole(role);
    }

    /**
     * Adds {@code role} to the company identified by {@code companyName}.
     * The role must not already exist in that company.
     */
    public void addCompanyRole(CompanyName companyName, Role role) {
        requireAllNonNull(companyName, role);
        Company company = findCompanyOrThrow(companyName);
        company.addRole(role);
    }

    /**
     * Returns the role with role name {@code roleName} from the company
     * identified by {@code companyName}, if present.
     */
    public Optional<Role> getCompanyRole(CompanyName companyName, String roleName) {
        requireAllNonNull(companyName, roleName);
        Company company = findCompanyOrThrow(companyName);
        return company.getUniqueRoleList().asUnmodifiableObservableList().stream()
                .filter(role -> role.getRoleName().toString().equals(roleName))
                .findFirst();
    }

    /**
     * Replaces {@code target} with {@code editedRole} in the company identified by
     * {@code companyName}.
     */
    public void setCompanyRole(CompanyName companyName, Role target, Role editedRole) {
        requireAllNonNull(companyName, target, editedRole);
        Company company = findCompanyOrThrow(companyName);
        company.setRole(target, editedRole);
    }

    /**
     * Removes {@code role} from the company identified by {@code companyName}.
     */
    public void removeCompanyRole(CompanyName companyName, Role role) {
        requireAllNonNull(companyName, role);
        Company company = findCompanyOrThrow(companyName);
        company.removeRole(role);
    }

    /// group-level operations

    /**
     * Returns true if a group with the same identity as {@code group} exists.
     */
    public boolean hasGroup(Group group) {
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
    public void deleteGroup(Group group) {
        groups.remove(group);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("companies", companies)
                .add("groups", groups)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Company> getCompanyList() {
        return companies.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Group> getGroupList() {
        return groups.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof HitList otherHitList)) {
            return false;
        }

        return persons.equals(otherHitList.persons)
            && groups.equals(otherHitList.groups)
            && companies.equals(otherHitList.companies);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(persons, groups, companies);
    }
}
