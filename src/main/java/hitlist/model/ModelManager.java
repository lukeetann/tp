package hitlist.model;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import hitlist.commons.core.GuiSettings;
import hitlist.commons.core.LogsCenter;
import hitlist.model.company.Company;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.Role;
import hitlist.model.company.role.UniqueRoleList;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.person.Name;
import hitlist.model.person.Person;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Represents the in-memory model of the HitList data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final HitList hitList;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Company> filteredCompanies;
    private final ObservableList<Role> roleList;

    /**
     * Initializes a ModelManager with the given HitList and userPrefs.
     */
    public ModelManager(ReadOnlyHitList hitList, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(hitList, userPrefs);

        logger.fine("Initializing with HitList: " + hitList + " and user prefs " + userPrefs);

        this.hitList = new HitList(hitList);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.hitList.getPersonList());
        filteredCompanies = new FilteredList<>(this.hitList.getCompanyList());
        roleList = this.hitList.getRoleList();
    }

    /**
     * Initializes a ModelManager with a new HitList and userPrefs.
     */
    public ModelManager() {
        this(new HitList(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getHitListFilePath() {
        return userPrefs.getHitListFilePath();
    }

    @Override
    public void setHitListFilePath(Path hitListFilePath) {
        requireNonNull(hitListFilePath);
        userPrefs.setHitListFilePath(hitListFilePath);
    }

    //=========== HitList ================================================================================

    @Override
    public void setHitList(ReadOnlyHitList hitList) {
        this.hitList.resetData(hitList);
    }

    @Override
    public ReadOnlyHitList getHitList() {
        return hitList;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return hitList.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        hitList.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        hitList.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        hitList.setPerson(target, editedPerson);
    }

    @Override
    public boolean hasGroup(Group group) {
        requireNonNull(group);
        return hitList.hasGroup(group);
    }

    @Override
    public Optional<Group> getGroup(GroupName groupName) {
        requireNonNull(groupName);
        List<Group> groupList = hitList.getGroupList();
        for (Group group : groupList) {
            if (group.getName().equals(groupName)) {
                return Optional.of(group);
            }
        }
        return Optional.empty();
    }

    public ObservableList<Group> getGroupList() {
        return hitList.getGroupList();
    }

    @Override
    public void addGroup(Group group) {
        hitList.addGroup(group);
    }

    @Override
    public void deleteGroup(Group group) {
        hitList.deleteGroup(group);
    }

    @Override
    public List<Person> getPersonsByName(Name name) {
        requireNonNull(name);
        return hitList.getPersonList().stream()
                .filter(person -> person.getName().equals(name))
                .toList();
    }

    @Override
    public boolean hasCompany(Company company) {
        requireNonNull(company);
        return hitList.hasCompany(company);
    }

    @Override
    public void addCompany(Company company) {
        hitList.addCompany(company);
        updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);
    }

    @Override
    public Optional<Company> getCompany(CompanyName companyName) {
        requireNonNull(companyName);
        List<Company> companyList = hitList.getCompanyList();
        for (Company company : companyList) {
            if (company.getName().equals(companyName)) {
                return Optional.of(company);
            }
        }
        return Optional.empty();
    }

    @Override
    public void deleteCompany(Company company) {
        hitList.removeCompany(company);
    }

    @Override
    public boolean hasCompanyRole(CompanyName companyName, Role role) {
        requireAllNonNull(role, companyName);
        return hitList.hasCompanyRole(companyName, role);
    }

    @Override
    public void addCompanyRole(CompanyName companyName, Role role) {
        requireAllNonNull(companyName, role);
        hitList.addCompanyRole(companyName, role);
        updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);
    }

    @Override
    public Optional<Role> getCompanyRole(CompanyName companyName, String roleName) {
        requireAllNonNull(companyName, roleName);
        return hitList.getCompanyRole(companyName, roleName);
    }

    @Override
    public void setCompanyRole(CompanyName companyName, Role target, Role editedRole) {
        requireAllNonNull(companyName, target, editedRole);
        hitList.setCompanyRole(companyName, target, editedRole);
        updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);
    }

    @Override
    public void deleteCompanyRole(CompanyName companyName, Role role) {
        requireAllNonNull(companyName, role);
        hitList.removeCompanyRole(companyName, role);
        updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedHitList}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public ObservableList<Company> getFilteredCompanyList() {
        return filteredCompanies;
    }

    @Override
    public ObservableList<Role> getRoleList() {
        return this.roleList;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public void updateFilteredCompanyList(Predicate<Company> predicate) {
        requireNonNull(predicate);
        filteredCompanies.setPredicate(predicate);
    }

    @Override
    public void updateRoleList(CompanyName companyName) {
        requireNonNull(companyName);
        Optional<Company> companyOptional = getCompany(companyName);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            UniqueRoleList roleList = company.getUniqueRoleList();
            this.roleList.setAll(roleList.asUnmodifiableObservableList());
        } else {
            roleList.clear();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }

        return hitList.equals(otherModelManager.hitList)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredCompanies.equals(otherModelManager.filteredCompanies);
    }
}
