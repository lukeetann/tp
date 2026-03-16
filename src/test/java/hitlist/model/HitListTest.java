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

import org.junit.jupiter.api.Test;

import hitlist.model.company.Company;
import hitlist.model.company.role.Role;
import hitlist.model.group.exceptions.DuplicateGroupException;
import hitlist.model.group.exceptions.GroupNotFoundException;
import hitlist.model.person.Person;
import hitlist.model.person.exceptions.DuplicatePersonException;
import hitlist.testutil.CompanyBuilder;
import hitlist.testutil.PersonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HitListTest {

    private final HitList hitList = new HitList();

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
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        HitListStub newData = new HitListStub(newPersons, Collections.emptyList(), Collections.emptyList());

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
                + ", roles=" + hitList.getRoleList()
                + ", companies=" + hitList.getCompanyList() + "}";
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

    /**
     * A stub ReadOnlyHitList whose persons list can violate interface constraints.
     */
    private static class HitListStub implements ReadOnlyHitList {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Company> companies = FXCollections.observableArrayList();
        private final ObservableList<Role> roles = FXCollections.observableArrayList();

        HitListStub(Collection<Person> persons, Collection<Company> companies, Collection<Role> roles) {
            this.persons.setAll(persons);
            this.companies.setAll(companies);
            this.roles.setAll(roles);
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
        public ObservableList<Role> getRoleList() {
            return roles;
        }
    }
}
