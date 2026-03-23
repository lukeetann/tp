package hitlist.model;

import static hitlist.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_DESCRIPTION_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalCompanies.GOOGLE;
import static hitlist.testutil.TypicalGroups.STUDENTS;
import static hitlist.testutil.TypicalPersons.ALICE;
import static hitlist.testutil.TypicalPersons.getTypicalHitList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hitlist.model.company.Company;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.Role;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.group.exceptions.DuplicateGroupException;
import hitlist.model.group.exceptions.GroupNotFoundException;
import hitlist.model.person.Person;
import hitlist.model.person.exceptions.DuplicatePersonException;
import hitlist.testutil.CompanyBuilder;
import hitlist.testutil.PersonBuilder;
import hitlist.testutil.RoleBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HitListTest {

    private HitList hitList;

    @BeforeEach
    public void setUp() {
        hitList = new HitList();
    }

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), hitList.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> hitList.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyHitList_replacesData() {
        HitList newData = getTypicalHitList();
        hitList.resetData(newData);
        assertEquals(newData, hitList);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        HitListStub newData = new HitListStub(newPersons,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        assertThrows(DuplicatePersonException.class, () -> hitList.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> hitList.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInHitList_returnsFalse() {
        assertFalse(hitList.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInHitList_returnsTrue() {
        hitList.addPerson(ALICE);
        assertTrue(hitList.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInHitList_returnsTrue() {
        hitList.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(hitList.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> hitList.getPersonList().remove(0));
    }

    @Test
    public void removePerson_personNotFound_throwsPersonNotFoundException() {
        Person missing = new PersonBuilder()
                .withName("Missing Person")
                .withPhone("99999999")
                .build();

        assertThrows(hitlist.model.person.exceptions.PersonNotFoundException.class, ()
                -> hitList.removePerson(missing));
    }

    @Test
    public void setPerson_targetNotFound_throwsPersonNotFoundException() {
        Person targetMissing = new PersonBuilder()
                .withName("Missing Person")
                .withPhone("99999999")
                .build();
        Person edited = new PersonBuilder()
                .withName("Edited Person")
                .withPhone("88888888")
                .build();

        assertThrows(hitlist.model.person.exceptions.PersonNotFoundException.class, ()
                -> hitList.setPerson(targetMissing, edited));
    }

    @Test
    public void setPerson_duplicateEditedPerson_throwsDuplicatePersonException() {
        Person a = new PersonBuilder()
                .withName("Alpha Person")
                .withPhone("81111111")
                .build();
        Person b = new PersonBuilder()
                .withName("Beta Person")
                .withPhone("82222222")
                .build();
        hitList.addPerson(a);
        hitList.addPerson(b);

        assertThrows(hitlist.model.person.exceptions.DuplicatePersonException.class, ()
                -> hitList.setPerson(a, b));
    }

    @Test
    public void hasCompany_nullCompany_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> hitList.hasCompany(null));
    }

    @Test
    public void hasCompany_companyNotInHitList_returnsFalse() {
        assertFalse(hitList.hasCompany(GOOGLE));
    }

    @Test
    public void hasCompany_companyInHitList_returnsTrue() {
        hitList.addCompany(GOOGLE);
        assertTrue(hitList.hasCompany(GOOGLE));
    }

    @Test
    public void hasCompany_companyWithSameIdentityFieldsInHitList_returnsTrue() {
        hitList.addCompany(GOOGLE);
        Company editedGoogle = new CompanyBuilder(GOOGLE)
                .withDescription(VALID_COMPANY_DESCRIPTION_GOOGLE)
                .build();
        assertTrue(hitList.hasCompany(editedGoogle));
    }

    @Test
    public void getCompanyList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> hitList.getCompanyList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = HitList.class.getCanonicalName()
                + "{persons=" + hitList.getPersonList()
                + ", companies=" + hitList.getCompanyList()
                + ", groups=" + hitList.getGroupList() + "}";
        assertEquals(expected, hitList.toString());
    }

    @Test
    public void hasGroup_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> hitList.hasGroup(null));
    }

    @Test
    public void hasGroup_groupNotInAddressBook_returnsFalse() {
        assertFalse(hitList.hasGroup(STUDENTS));
    }

    @Test
    public void hasGroup_groupInAddressBook_returnsTrue() {
        hitList.addGroup(STUDENTS);
        assertTrue(hitList.hasGroup(STUDENTS));
    }

    @Test
    public void addGroup_groupNotInAddressBook_success() {
        hitList.addGroup(STUDENTS);
    }

    @Test
    public void addGroup_groupInAddressBook_throwsDuplicateGroupException() {
        hitList.addGroup(STUDENTS);
        assertThrows(DuplicateGroupException.class, () -> hitList.addGroup(STUDENTS));
    }

    @Test
    public void deleteGroup_groupInAddressBook_success() {
        hitList.addGroup(STUDENTS);
        hitList.deleteGroup(STUDENTS);
    }

    @Test
    public void deleteGroup_groupNotInAddressBook_throwsGroupNotFoundException() {
        assertThrows(GroupNotFoundException.class, () -> hitList.deleteGroup(STUDENTS));
    }

    @Test
    public void hasCompanyRole_nullCompanyName_throwsNullPointerException() {
        Role role = new RoleBuilder().withName("Role_HasNullCompany").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.hasCompanyRole(null, role));
    }

    @Test
    public void hasCompanyRole_nullRole_throwsNullPointerException() {
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.hasCompanyRole(GOOGLE.getName(), null));
    }

    @Test
    public void hasCompanyRole_roleNotInCompany_returnsFalse() {
        Role role = new RoleBuilder().withName("Role_NotInCompany_Unique").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        assertFalse(hitList.hasCompanyRole(GOOGLE.getName(), role));
    }

    @Test
    public void hasCompanyRole_roleInCompany_returnsTrue() {
        Role role = new RoleBuilder().withName("Role_InCompany_Unique").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        hitList.addCompanyRole(GOOGLE.getName(), role);
        assertTrue(hitList.hasCompanyRole(GOOGLE.getName(), role));
    }

    @Test
    public void addCompanyRole_nullCompanyName_throwsNullPointerException() {
        Role role = new RoleBuilder().withName("Role_AddNullCompany").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.addCompanyRole(null, role));
    }

    @Test
    public void addCompanyRole_nullRole_throwsNullPointerException() {
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.addCompanyRole(GOOGLE.getName(), null));
    }

    @Test
    public void addCompanyRole_validRole_success() {
        Role role = new RoleBuilder().withName("Role_AddValid_Unique").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        hitList.addCompanyRole(GOOGLE.getName(), role);
        assertTrue(hitList.hasCompanyRole(GOOGLE.getName(), role));
    }

    @Test
    public void setCompany_validTargetAndEditedCompany_success() {
        Company original = new CompanyBuilder()
                .withName("SetCompany Original")
                .withDescription("Original desc")
                .build();
        Company edited = new CompanyBuilder()
                .withName("SetCompany Edited")
                .withDescription("Edited desc")
                .build();

        hitList.addCompany(original);
        hitList.setCompany(original, edited);

        assertFalse(hitList.hasCompany(original));
        assertTrue(hitList.hasCompany(edited));
    }

    @Test
    public void setCompany_nullEditedCompany_throwsNullPointerException() {
        Company original = new CompanyBuilder()
                .withName("SetCompany Null Edited")
                .withDescription("Desc")
                .build();
        hitList.addCompany(original);

        assertThrows(NullPointerException.class, () -> hitList.setCompany(original, null));
    }

    @Test
    public void setCompany_targetNotFound_throwsCompanyNotFoundException() {
        Company targetMissing = new CompanyBuilder()
                .withName("Target Missing")
                .withDescription("Desc")
                .build();
        Company edited = new CompanyBuilder()
                .withName("Edited Value")
                .withDescription("Desc")
                .build();

        assertThrows(hitlist.model.company.exceptions.CompanyNotFoundException.class, ()
                -> hitList.setCompany(targetMissing, edited));
    }

    @Test
    public void setCompany_duplicateEditedCompany_throwsDuplicateCompanyException() {
        Company a = new CompanyBuilder().withName("Company A").withDescription("Desc A").build();
        Company b = new CompanyBuilder().withName("Company B").withDescription("Desc B").build();
        hitList.addCompany(a);
        hitList.addCompany(b);

        assertThrows(hitlist.model.company.exceptions.DuplicateCompanyException.class, ()
                -> hitList.setCompany(a, b));
    }

    @Test
    public void removeCompany_companyNotFound_throwsCompanyNotFoundException() {
        Company missing = new CompanyBuilder()
                .withName("Missing Company")
                .withDescription("Desc")
                .build();

        assertThrows(hitlist.model.company.exceptions.CompanyNotFoundException.class, ()
                -> hitList.removeCompany(missing));
    }

    @Test
    public void addCompanyRole_duplicateRole_throwsDuplicateRoleException() {
        Role role = new RoleBuilder().withName("Role_AddDuplicate_Unique").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        hitList.addCompanyRole(GOOGLE.getName(), role);

        assertThrows(hitlist.model.company.role.exceptions.DuplicateRoleException.class, ()
                -> hitList.addCompanyRole(GOOGLE.getName(), role));
    }

    @Test
    public void getCompanyRole_nullCompanyName_throwsNullPointerException() {
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.getCompanyRole(null, "Any"));
    }

    @Test
    public void getCompanyRole_nullRoleName_throwsNullPointerException() {
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.getCompanyRole(GOOGLE.getName(), null));
    }

    @Test
    public void getCompanyRole_roleExists_returnsRole() {
        Role role = new RoleBuilder().withName("Role_GetExists_Unique").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        hitList.addCompanyRole(GOOGLE.getName(), role);

        Optional<Role> result = hitList.getCompanyRole(GOOGLE.getName(), "Role_GetExists_Unique");

        assertTrue(result.isPresent());
        assertEquals(role, result.get());
    }

    @Test
    public void getCompanyRole_roleDoesNotExist_returnsEmptyOptional() {
        Role existingRole = new RoleBuilder().withName("Role_GetMissing_Base").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        hitList.addCompanyRole(GOOGLE.getName(), existingRole);

        Optional<Role> result = hitList.getCompanyRole(GOOGLE.getName(), "Role_Not_There");

        assertFalse(result.isPresent());
    }

    @Test
    public void setCompanyRole_nullCompanyName_throwsNullPointerException() {
        Role target = new RoleBuilder().withName("Role_SetNullCompany_Target").withDescription("desc").build();
        Role edited = new RoleBuilder().withName("Role_SetNullCompany_Edited").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.setCompanyRole(null, target, edited));
    }

    @Test
    public void setCompanyRole_nullTargetRole_throwsNullPointerException() {
        Role edited = new RoleBuilder().withName("Role_SetNullTarget_Edited").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.setCompanyRole(GOOGLE.getName(), null, edited));
    }

    @Test
    public void setCompanyRole_nullEditedRole_throwsNullPointerException() {
        Role target = new RoleBuilder().withName("Role_SetNullEdited_Target").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.setCompanyRole(GOOGLE.getName(), target, null));
    }

    @Test
    public void setCompanyRole_validParameters_success() {
        Role target = new RoleBuilder().withName("Role_SetValid_Target").withDescription("desc").build();
        Role edited = new RoleBuilder().withName("Role_SetValid_Edited").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        hitList.addCompanyRole(GOOGLE.getName(), target);

        hitList.setCompanyRole(GOOGLE.getName(), target, edited);

        assertFalse(hitList.hasCompanyRole(GOOGLE.getName(), target));
        assertTrue(hitList.hasCompanyRole(GOOGLE.getName(), edited));
    }

    @Test
    public void setCompanyRole_targetRoleNotFound_throwsRoleNotFoundException() {
        Role target = new RoleBuilder().withName("Role_SetMissing_Target").withDescription("desc").build();
        Role edited = new RoleBuilder().withName("Role_SetMissing_Edited").withDescription("desc").build();
        hitList.addCompany(GOOGLE);

        assertThrows(hitlist.model.company.role.exceptions.RoleNotFoundException.class, ()
                -> hitList.setCompanyRole(GOOGLE.getName(), target, edited));
    }

    @Test
    public void setCompanyRole_duplicateEditedRole_throwsDuplicateRoleException() {
        Role target = new RoleBuilder().withName("Role_SetDup_Target").withDescription("desc").build();
        Role alreadyExists = new RoleBuilder().withName("Role_SetDup_Exists").withDescription("desc").build();
        Role editedSameIdentityAsExisting =
                new RoleBuilder().withName("Role_SetDup_Exists").withDescription("another desc").build();

        hitList.addCompany(GOOGLE);
        hitList.addCompanyRole(GOOGLE.getName(), target);
        hitList.addCompanyRole(GOOGLE.getName(), alreadyExists);

        assertThrows(hitlist.model.company.role.exceptions.DuplicateRoleException.class, ()
                -> hitList.setCompanyRole(GOOGLE.getName(), target, editedSameIdentityAsExisting));
    }

    @Test
    public void removeCompanyRole_nullCompanyName_throwsNullPointerException() {
        Role role = new RoleBuilder().withName("Role_RemoveNullCompany").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.removeCompanyRole(null, role));
    }

    @Test
    public void removeCompanyRole_nullRole_throwsNullPointerException() {
        hitList.addCompany(GOOGLE);
        assertThrows(NullPointerException.class, () -> hitList.removeCompanyRole(GOOGLE.getName(), null));
    }

    @Test
    public void removeCompanyRole_validParameters_success() {
        Role role = new RoleBuilder().withName("Role_RemoveValid_Unique").withDescription("desc").build();
        hitList.addCompany(GOOGLE);
        hitList.addCompanyRole(GOOGLE.getName(), role);

        hitList.removeCompanyRole(GOOGLE.getName(), role);

        assertFalse(hitList.hasCompanyRole(GOOGLE.getName(), role));
    }

    @Test
    public void removeCompanyRole_roleNotFound_throwsRoleNotFoundException() {
        Role role = new RoleBuilder().withName("Role_RemoveMissing_Unique").withDescription("desc").build();
        hitList.addCompany(GOOGLE);

        assertThrows(hitlist.model.company.role.exceptions.RoleNotFoundException.class, ()
                -> hitList.removeCompanyRole(GOOGLE.getName(), role));
    }

    @Test
    public void roleOperations_companyNotFound_throwsCompanyNotFoundException() {
        CompanyName missing = new CompanyName("Missing Co");
        Role role = new RoleBuilder().withName("Role_MissingCompany_Unique").withDescription("desc").build();

        assertThrows(hitlist.model.company.exceptions.CompanyNotFoundException.class, ()
                -> hitList.hasCompanyRole(missing, role));
        assertThrows(hitlist.model.company.exceptions.CompanyNotFoundException.class, ()
                -> hitList.addCompanyRole(missing, role));
        assertThrows(hitlist.model.company.exceptions.CompanyNotFoundException.class, ()
                -> hitList.getCompanyRole(missing, "Role_MissingCompany_Unique"));
        assertThrows(hitlist.model.company.exceptions.CompanyNotFoundException.class, ()
                -> hitList.setCompanyRole(missing, role, role));
        assertThrows(hitlist.model.company.exceptions.CompanyNotFoundException.class, ()
                -> hitList.removeCompanyRole(missing, role));
    }

    @Test
    public void equals_differentCompanies_returnsFalse() {
        HitList first = new HitList();
        HitList second = new HitList();

        first.addCompany(new CompanyBuilder().withName("Equal ABCD").withDescription("ABCD").build());
        second.addCompany(new CompanyBuilder().withName("Equal BCDE").withDescription("BCDE").build());

        assertFalse(first.equals(second));
    }

    @Test
    public void equals_differentGroups_returnsFalse() {
        HitList first = new HitList();
        HitList second = new HitList();

        first.addGroup(new Group(new GroupName("Group A")));
        second.addGroup(new Group(new GroupName("Group B")));

        assertFalse(first.equals(second));
    }

    @Test
    public void equals_differentPersons_returnsFalse() {
        HitList first = new HitList();
        HitList second = new HitList();

        first.addPerson(new PersonBuilder().withName("POne").withPhone("90000001").build());
        second.addPerson(new PersonBuilder().withName("PTwo").withPhone("90000002").build());

        assertFalse(first.equals(second));
    }

    /**
     * A stub ReadOnlyHitList whose persons list can violate interface constraints.
     */
    private static class HitListStub implements ReadOnlyHitList {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Company> companies = FXCollections.observableArrayList();
        private final ObservableList<Role> roles = FXCollections.observableArrayList();
        private final ObservableList<Group> groups = FXCollections.observableArrayList();

        HitListStub(Collection<Person> persons,
                    Collection<Company> companies,
                    Collection<Role> roles,
                    Collection<Group> groups) {
            this.persons.setAll(persons);
            this.companies.setAll(companies);
            this.roles.setAll(roles);
            this.groups.setAll(groups);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Company> getCompanyList() {
            return companies;
        }

        @Override
        public ObservableList<Group> getGroupList() {
            return groups;
        }
    }
}
