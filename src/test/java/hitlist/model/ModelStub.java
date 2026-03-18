package hitlist.model;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

import hitlist.commons.core.GuiSettings;
import hitlist.model.company.Company;
import hitlist.model.company.CompanyName;
import hitlist.model.group.Group;
import hitlist.model.person.Person;
import javafx.collections.ObservableList;

/**
 * A default model stub that have all methods failing.
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
    public void setHitListFilePath(Path addressBookFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addPerson(Person person) {
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
    public void setPerson(Person target, Person editedPerson) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasGroup(Group group) {
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
}
