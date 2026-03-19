package hitlist.logic;

import java.nio.file.Path;

import hitlist.commons.core.GuiSettings;
import hitlist.logic.commands.CommandResult;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.HitList;
import hitlist.model.ReadOnlyHitList;
import hitlist.model.company.Company;
import hitlist.model.group.Group;
import hitlist.model.person.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A concrete Logic stub for tests.
 */
public class LogicStub implements Logic {

    private final ReadOnlyHitList hitList = new HitList();
    private final ObservableList<Person> filteredPersons = FXCollections.observableArrayList();
    private final ObservableList<Company> filteredCompanies = FXCollections.observableArrayList();
    private GuiSettings guiSettings = new GuiSettings();
    private Path filePath = Path.of("data", "hitlist.json");

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        return new CommandResult("ok");
    }

    @Override
    public ReadOnlyHitList getAddressBook() {
        return hitList;
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public ObservableList<Group> getGroupList() {
        return hitList.getGroupList();
    }

    @Override
    public ObservableList<Company> getFilteredCompanyList() {
        return FXCollections.unmodifiableObservableList(filteredCompanies);
    }

    @Override
    public Path getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        this.guiSettings = guiSettings == null ? new GuiSettings() : guiSettings;
    }
}
