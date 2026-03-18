package hitlist.model;

import static hitlist.model.Model.PREDICATE_SHOW_ALL_COMPANIES;
import static hitlist.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalCompanies.GOOGLE;
import static hitlist.testutil.TypicalCompanies.META;
import static hitlist.testutil.TypicalGroups.STUDENTS;
import static hitlist.testutil.TypicalPersons.ALICE;
import static hitlist.testutil.TypicalPersons.BENSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import hitlist.commons.core.GuiSettings;
import hitlist.model.person.NameContainsKeywordsPredicate;
import hitlist.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new HitList(), new HitList(modelManager.getHitList()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setHitListFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setHitListFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setHitListFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setHitListFilePath(null));
    }

    @Test
    public void setHitListFilePath_validPath_setsHitListFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setHitListFilePath(path);
        assertEquals(path, modelManager.getHitListFilePath());
    }

    @Test
    public void setHitList_validReadOnlyHitList_replacesData() {
        HitList hitList = new AddressBookBuilder()
                .withPerson(ALICE)
                .withCompany(GOOGLE)
                .build();

        modelManager.setHitList(hitList);

        assertEquals(hitList, modelManager.getHitList());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInHitList_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInHitList_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void deletePerson_personInHitList_success() {
        modelManager.addPerson(ALICE);
        modelManager.deletePerson(ALICE);

        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void addPerson_personNotInHitList_success() {
        modelManager.addPerson(ALICE);

        HitList expectedHitList = new HitList();
        expectedHitList.addPerson(ALICE);

        assertEquals(expectedHitList, modelManager.getHitList());
    }

    @Test
    public void setPerson_nullTarget_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setPerson(null, ALICE));
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        modelManager.addPerson(ALICE);
        assertThrows(NullPointerException.class, () -> modelManager.setPerson(ALICE, null));
    }

    @Test
    public void setPerson_validTargetAndEditedPerson_success() {
        modelManager.addPerson(ALICE);
        modelManager.setPerson(ALICE, BENSON);

        assertFalse(modelManager.hasPerson(ALICE));
        assertTrue(modelManager.hasPerson(BENSON));
    }

    @Test
    public void updateFilteredPersonList_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.updateFilteredPersonList(null));
    }

    @Test
    public void updateFilteredPersonList_validPredicate_filtersList() {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BENSON);

        modelManager.updateFilteredPersonList(person -> person.getName().toString().contains("Alice"));

        assertEquals(1, modelManager.getFilteredPersonList().size());
        assertEquals(ALICE, modelManager.getFilteredPersonList().get(0));
    }

    @Test
    public void hasGroup_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasGroup(null));
    }

    @Test
    public void hasGroup_groupNotInHitList_returnsFalse() {
        assertFalse(modelManager.hasGroup(STUDENTS));
    }

    @Test
    public void hasGroup_groupInHitList_returnsTrue() {
        modelManager.addGroup(STUDENTS);
        assertTrue(modelManager.hasGroup(STUDENTS));
    }

    @Test
    public void addGroup_groupNotInHitList_success() {
        modelManager.addGroup(STUDENTS);
    }

    @Test
    public void deleteGroup_groupInHitList_success() {
        modelManager.addGroup(STUDENTS);
        modelManager.deleteGroup(STUDENTS);
    }

    @Test
    public void hasCompany_nullCompany_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasCompany(null));
    }

    @Test
    public void hasCompany_companyNotInHitList_returnsFalse() {
        assertFalse(modelManager.hasCompany(GOOGLE));
    }

    @Test
    public void hasCompany_companyInHitList_returnsTrue() {
        modelManager.addCompany(GOOGLE);
        assertTrue(modelManager.hasCompany(GOOGLE));
    }

    @Test
    public void addCompany_companyNotInHitList_success() {
        modelManager.addCompany(GOOGLE);

        HitList expectedHitList = new HitList();
        expectedHitList.addCompany(GOOGLE);

        assertEquals(expectedHitList, modelManager.getHitList());
    }

    @Test
    public void deleteCompany_companyInHitList_success() {
        modelManager.addCompany(GOOGLE);
        modelManager.deleteCompany(GOOGLE);

        HitList expectedHitList = new HitList();

        assertEquals(expectedHitList, modelManager.getHitList());
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void getFilteredCompanyList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredCompanyList().remove(0));
    }

    @Test
    public void updateFilteredCompanyList_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.updateFilteredCompanyList(null));
    }

    @Test
    public void updateFilteredCompanyList_validPredicate_filtersList() {
        modelManager.addCompany(GOOGLE);
        modelManager.addCompany(META);

        modelManager.updateFilteredCompanyList(company -> company.getName().toString().contains("Google"));

        assertEquals(1, modelManager.getFilteredCompanyList().size());
        assertEquals(GOOGLE, modelManager.getFilteredCompanyList().get(0));
    }

    @Test
    public void equals() {
        HitList hitList = new AddressBookBuilder()
                .withPerson(ALICE)
                .withPerson(BENSON)
                .withCompany(GOOGLE)
                .withCompany(META)
                .build();
        HitList differentHitList = new HitList();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(hitList, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(hitList, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentHitList, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(hitList, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        modelManager.updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setHitListFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(hitList, differentUserPrefs)));
    }
}
