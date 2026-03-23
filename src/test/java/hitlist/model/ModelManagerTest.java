package hitlist.model;

import static hitlist.model.Model.PREDICATE_SHOW_ALL_COMPANIES;
import static hitlist.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalCompanies.GOOGLE;
import static hitlist.testutil.TypicalCompanies.META;
import static hitlist.testutil.TypicalGroups.STUDENTS;
import static hitlist.testutil.TypicalGroups.UNEMPLOYED;
import static hitlist.testutil.TypicalPersons.ALICE;
import static hitlist.testutil.TypicalPersons.BENSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hitlist.commons.core.GuiSettings;
import hitlist.model.company.Company;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.Role;
import hitlist.model.person.NameContainsKeywordsPredicate;
import hitlist.testutil.HitListBuilder;
import hitlist.testutil.RoleBuilder;

public class ModelManagerTest {

    private ModelManager modelManager;

    @BeforeEach
    public void setUp() {
        modelManager = new ModelManager();
    }

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
        HitList hitList = new HitListBuilder()
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
    public void getGroupList_noGroupsInHitList_returnsEmptyList() {
        assertTrue(modelManager.getGroupList().size() == 0);
    }

    @Test
    public void getGroupList_oneGroupInHitList_returnsEmptyList() {
        modelManager.addGroup(STUDENTS);
        assertTrue(modelManager.getGroupList().size() == 1);
        assertTrue(modelManager.getGroupList().contains(STUDENTS));
    }

    @Test
    public void getGroupList_twoGroupsInHitList_returnsEmptyList() {
        modelManager.addGroup(STUDENTS);
        modelManager.addGroup(UNEMPLOYED);
        assertTrue(modelManager.getGroupList().size() == 2);
        assertTrue(modelManager.getGroupList().contains(STUDENTS));
        assertTrue(modelManager.getGroupList().contains(UNEMPLOYED));
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
    public void getCompany_companyExists_returnsCompany() {
        modelManager.addCompany(GOOGLE);

        Optional<Company> result = modelManager.getCompany(GOOGLE.getName());

        assertTrue(result.isPresent());
        assertEquals(GOOGLE, result.get());
    }

    @Test
    public void getCompany_companyDoesNotExist_returnsEmptyOptional() {
        modelManager.addCompany(GOOGLE);
        CompanyName missingCompanyName = new CompanyName("Meta Platforms, Inc.");

        Optional<Company> result = modelManager.getCompany(missingCompanyName);

        assertFalse(result.isPresent());
    }

    @Test
    public void getCompany_nullCompanyName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.getCompany(null));
    }

    @Test
    public void deleteCompany_companyInHitList_success() {
        modelManager.addCompany(GOOGLE);
        modelManager.deleteCompany(GOOGLE);

        HitList expectedHitList = new HitList();

        assertEquals(expectedHitList, modelManager.getHitList());
    }

    @Test
    public void hasCompanyRole_nullCompanyName_throwsNullPointerException() {
        Role role = new RoleBuilder().withName("MM_HasRole_NullCompany").withDescription("desc").build();
        assertThrows(NullPointerException.class, () -> modelManager.hasCompanyRole(null, role));
    }

    @Test
    public void hasCompanyRole_nullRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasCompanyRole(GOOGLE.getName(), null));
    }

    @Test
    public void hasCompanyRole_roleNotInCompany_returnsFalse() {
        modelManager.addCompany(GOOGLE);
        Role role = new RoleBuilder().withName("MM_HasRole_NotInCompany").withDescription("desc").build();
        assertFalse(modelManager.hasCompanyRole(GOOGLE.getName(), role));
    }

    @Test
    public void hasCompanyRole_roleInCompany_returnsTrue() {
        modelManager.addCompany(GOOGLE);
        Role role = new RoleBuilder().withName("MM_HasRole_InCompany").withDescription("desc").build();
        modelManager.addCompanyRole(GOOGLE.getName(), role);
        assertTrue(modelManager.hasCompanyRole(GOOGLE.getName(), role));
    }

    @Test
    public void addCompanyRole_nullCompanyName_throwsNullPointerException() {
        Role role = new RoleBuilder().withName("MM_AddRole_NullCompany").withDescription("desc").build();
        assertThrows(NullPointerException.class, () -> modelManager.addCompanyRole(null, role));
    }

    @Test
    public void addCompanyRole_nullRole_throwsNullPointerException() {
        modelManager.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> modelManager.addCompanyRole(GOOGLE.getName(), null));
    }

    @Test
    public void addCompanyRole_roleNotInCompany_success() {
        modelManager.addCompany(GOOGLE);
        Role role = new RoleBuilder().withName("MM_AddRole_Success").withDescription("desc").build();

        modelManager.addCompanyRole(GOOGLE.getName(), role);

        assertTrue(modelManager.hasCompanyRole(GOOGLE.getName(), role));
    }

    @Test
    public void getCompanyRole_nullCompanyName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> modelManager.getCompanyRole(null, "AnyRole"));
    }

    @Test
    public void getCompanyRole_nullRoleName_throwsNullPointerException() {
        modelManager.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> modelManager.getCompanyRole(GOOGLE.getName(), null));
    }

    @Test
    public void getCompanyRole_roleExists_returnsRole() {
        modelManager.addCompany(GOOGLE);
        Role role = new RoleBuilder().withName("MM_GetRole_Exists").withDescription("desc").build();
        modelManager.addCompanyRole(GOOGLE.getName(), role);

        Optional<Role> result = modelManager.getCompanyRole(GOOGLE.getName(), "MM_GetRole_Exists");

        assertTrue(result.isPresent());
        assertEquals(role, result.get());
    }

    @Test
    public void getCompanyRole_roleDoesNotExist_returnsEmptyOptional() {
        modelManager.addCompany(GOOGLE);
        Role existingRole = new RoleBuilder().withName("MM_GetRole_Base").withDescription("desc").build();
        modelManager.addCompanyRole(GOOGLE.getName(), existingRole);

        Optional<Role> result = modelManager.getCompanyRole(GOOGLE.getName(), "MM_GetRole_Missing");

        assertFalse(result.isPresent());
    }

    @Test
    public void getCompanyRole_companyNotFound_throwsCompanyNotFoundException() {
        assertThrows(hitlist.model.company.exceptions.CompanyNotFoundException.class, ()
                -> modelManager.getCompanyRole(new CompanyName("Missing Co"), "AnyRole"));
    }

    @Test
    public void setCompanyRole_nullCompanyName_throwsNullPointerException() {
        Role target = new RoleBuilder().withName("MM_SetRole_NullCompany_Target").withDescription("desc").build();
        Role edited = new RoleBuilder().withName("MM_SetRole_NullCompany_Edited").withDescription("desc").build();
        assertThrows(NullPointerException.class, ()
                -> modelManager.setCompanyRole(null, target, edited));
    }

    @Test
    public void setCompanyRole_nullTargetRole_throwsNullPointerException() {
        modelManager.addCompany(GOOGLE);
        Role edited = new RoleBuilder().withName("MM_SetRole_NullTarget_Edited").withDescription("desc").build();
        assertThrows(NullPointerException.class, ()
                -> modelManager.setCompanyRole(GOOGLE.getName(), null, edited));
    }

    @Test
    public void setCompanyRole_nullEditedRole_throwsNullPointerException() {
        modelManager.addCompany(GOOGLE);
        Role target = new RoleBuilder().withName("MM_SetRole_NullEdited_Target").withDescription("desc").build();
        assertThrows(NullPointerException.class, ()
                -> modelManager.setCompanyRole(GOOGLE.getName(), target, null));
    }

    @Test
    public void setCompanyRole_validParameters_success() {
        modelManager.addCompany(GOOGLE);
        Role target = new RoleBuilder().withName("MM_SetRole_Target").withDescription("desc").build();
        Role edited = new RoleBuilder().withName("MM_SetRole_Edited").withDescription("desc").build();
        modelManager.addCompanyRole(GOOGLE.getName(), target);

        modelManager.setCompanyRole(GOOGLE.getName(), target, edited);

        assertFalse(modelManager.hasCompanyRole(GOOGLE.getName(), target));
        assertTrue(modelManager.hasCompanyRole(GOOGLE.getName(), edited));
    }

    @Test
    public void setCompanyRole_targetNotFound_throwsRoleNotFoundException() {
        modelManager.addCompany(GOOGLE);
        Role missingTarget = new RoleBuilder().withName("MM_SetRole_TargetMissing").withDescription("desc").build();
        Role edited = new RoleBuilder().withName("MM_SetRole_TargetMissing_Edited").withDescription("desc").build();

        assertThrows(hitlist.model.company.role.exceptions.RoleNotFoundException.class, ()
                -> modelManager.setCompanyRole(GOOGLE.getName(), missingTarget, edited));
    }

    @Test
    public void setCompanyRole_duplicateEditedRole_throwsDuplicateRoleException() {
        modelManager.addCompany(GOOGLE);
        Role target = new RoleBuilder().withName("MM_SetRole_Dup_Target").withDescription("desc").build();
        Role exists = new RoleBuilder().withName("MM_SetRole_Dup_Exists").withDescription("desc").build();
        Role editedSameIdentity = new RoleBuilder().withName("MM_SetRole_Dup_Exists")
                .withDescription("another")
                .build();

        modelManager.addCompanyRole(GOOGLE.getName(), target);
        modelManager.addCompanyRole(GOOGLE.getName(), exists);

        assertThrows(hitlist.model.company.role.exceptions.DuplicateRoleException.class, ()
                -> modelManager.setCompanyRole(GOOGLE.getName(), target, editedSameIdentity));
    }

    @Test
    public void setCompanyRole_companyNotFound_throwsCompanyNotFoundException() {
        Role target = new RoleBuilder().withName("MM_SetRole_MissingCompany_Target").withDescription("desc").build();
        Role edited = new RoleBuilder().withName("MM_SetRole_MissingCompany_Edited").withDescription("desc").build();

        assertThrows(hitlist.model.company.exceptions.CompanyNotFoundException.class, ()
                -> modelManager.setCompanyRole(new CompanyName("Missing Company"), target, edited));
    }

    @Test
    public void deleteCompanyRole_nullCompanyName_throwsNullPointerException() {
        Role role = new RoleBuilder().withName("MM_DeleteRole_NullCompany").withDescription("desc").build();
        assertThrows(NullPointerException.class, () -> modelManager.deleteCompanyRole(null, role));
    }

    @Test
    public void deleteCompanyRole_nullRole_throwsNullPointerException() {
        modelManager.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> modelManager.deleteCompanyRole(GOOGLE.getName(), null));
    }

    @Test
    public void deleteCompanyRole_validParameters_success() {
        modelManager.addCompany(GOOGLE);
        Role role = new RoleBuilder().withName("MM_DeleteRole_Success").withDescription("desc").build();
        modelManager.addCompanyRole(GOOGLE.getName(), role);

        modelManager.deleteCompanyRole(GOOGLE.getName(), role);

        assertFalse(modelManager.hasCompanyRole(GOOGLE.getName(), role));
    }

    @Test
    public void deleteCompanyRole_companyNotFound_throwsCompanyNotFoundException() {
        Role role = new RoleBuilder().withName("MM_DeleteRole_MissingCompany").withDescription("desc").build();

        assertThrows(hitlist.model.company.exceptions.CompanyNotFoundException.class, ()
                -> modelManager.deleteCompanyRole(new CompanyName("Missing Co"), role));
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
        HitList hitList = new HitListBuilder()
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

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setHitListFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(hitList, differentUserPrefs)));

        // different filteredPersonList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(hitList, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different filteredCompanyList -> returns false
        modelManager.updateFilteredCompanyList(company -> false);
        assertFalse(modelManager.equals(new ModelManager(hitList, userPrefs)));

        // resets modelManager to initial state
        modelManager.updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);
    }
}
