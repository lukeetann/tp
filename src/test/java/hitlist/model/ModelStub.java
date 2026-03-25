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
 * A default model stub that has all methods failing.
 */
public abstract class ModelStub implements Model {

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public GuiSettings getGuiSettings() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getHitListFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setHitListFilePath(Path hitListFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setHitList(ReadOnlyHitList newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyHitList getHitList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deletePerson(Person target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Group> getGroupList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Company> getFilteredCompanyList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredCompanyList(Predicate<Company> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasGroup(Group group) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<Group> getGroup(GroupName groupName) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addGroup(Group group) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteGroup(Group group) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public List<Person> getPersonsByName(Name name) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasCompany(Company company) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addCompany(Company company) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<Company> getCompany(CompanyName companyName) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteCompany(Company company) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasCompanyRole(CompanyName companyName, Role role) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addCompanyRole(CompanyName companyName, Role role) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<Role> getCompanyRole(CompanyName companyName, String roleName) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setCompanyRole(CompanyName companyName, Role target, Role editedRole) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteCompanyRole(CompanyName companyName, Role role) {
        throw new AssertionError("This method should not be called.");
    }
}
