package hitlist.model.company.role;

import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_NAME_SOFTWARE_ENGINEER;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalRoles.PRODUCT_MANAGER;
import static hitlist.testutil.TypicalRoles.SOFTWARE_ENGINEER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hitlist.model.company.role.exceptions.DuplicateRoleException;
import hitlist.model.company.role.exceptions.RoleNotFoundException;
import hitlist.testutil.RoleBuilder;

public class UniqueRoleListTest {

    private UniqueRoleList uniqueRoleList;

    @BeforeEach
    public void setUp() {
        uniqueRoleList = new UniqueRoleList();
    }

    @AfterEach
    public void tearDown() {
        uniqueRoleList = null;
    }

    @Test
    public void contains_nullRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.contains(null));
    }

    @Test
    public void contains_roleNotInList_returnsFalse() {
        assertFalse(uniqueRoleList.contains(SOFTWARE_ENGINEER));
    }

    @Test
    public void contains_roleInList_returnsTrue() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        assertTrue(uniqueRoleList.contains(SOFTWARE_ENGINEER));
    }

    @Test
    public void contains_roleWithSameFieldsInList_returnsTrue() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        Role editedRole = new RoleBuilder()
                .withName(VALID_ROLE_NAME_SOFTWARE_ENGINEER)
                .withDescription(VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER)
                .build();
        assertTrue(uniqueRoleList.contains(editedRole));
    }

    @Test
    public void add_nullRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.add(null));
    }

    @Test
    public void add_duplicateRole_throwsDuplicateRoleException() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        assertThrows(DuplicateRoleException.class, () -> uniqueRoleList.add(SOFTWARE_ENGINEER));
    }

    @Test
    public void setRole_nullTargetRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.setRole(null, SOFTWARE_ENGINEER));
    }

    @Test
    public void setRole_nullEditedRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.setRole(SOFTWARE_ENGINEER, null));
    }

    @Test
    public void setRole_targetRoleNotInList_throwsRoleNotFoundException() {
        assertThrows(RoleNotFoundException.class, () -> uniqueRoleList.setRole(SOFTWARE_ENGINEER, SOFTWARE_ENGINEER));
    }

    @Test
    public void setRole_editedRoleIsSameRole_success() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        uniqueRoleList.setRole(SOFTWARE_ENGINEER, SOFTWARE_ENGINEER);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(SOFTWARE_ENGINEER);
        assertEquals(uniqueRoleList, expectedUniqueRoleList);
    }

    @Test
    public void setRole_editedRoleHasSameRoleName_success() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        Role editedRole = new RoleBuilder()
                .withName(VALID_ROLE_NAME_SOFTWARE_ENGINEER)
                .withDescription(VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER)
                .build();
        uniqueRoleList.setRole(SOFTWARE_ENGINEER, editedRole);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(editedRole);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRole_editedRoleHasDifferentRole_success() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        uniqueRoleList.setRole(SOFTWARE_ENGINEER, PRODUCT_MANAGER);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(PRODUCT_MANAGER);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRole_editedRoleHasNonUniqueIdentity_throwsDuplicateRoleException() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        uniqueRoleList.add(PRODUCT_MANAGER);
        assertThrows(DuplicateRoleException.class, () -> uniqueRoleList.setRole(SOFTWARE_ENGINEER, PRODUCT_MANAGER));
    }

    @Test
    public void remove_nullRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.remove(null));
    }

    @Test
    public void remove_roleDoesNotExist_throwsRoleNotFoundException() {
        assertThrows(RoleNotFoundException.class, () -> uniqueRoleList.remove(SOFTWARE_ENGINEER));
    }

    @Test
    public void remove_existingRole_removesRole() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        uniqueRoleList.remove(SOFTWARE_ENGINEER);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRoles_nullUniqueRoleList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.setRoles((UniqueRoleList) null));
    }

    @Test
    public void setRoles_uniqueRoleList_replacesOwnListWithProvidedUniqueRoleList() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(PRODUCT_MANAGER);
        uniqueRoleList.setRoles(expectedUniqueRoleList);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRoles_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.setRoles((List<Role>) null));
    }

    @Test
    public void setRoles_list_replacesOwnListWithProvidedList() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        List<Role> roleList = Collections.singletonList(PRODUCT_MANAGER);
        uniqueRoleList.setRoles(roleList);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(PRODUCT_MANAGER);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRoles_listWithDuplicateRoles_throwsDuplicateRoleException() {
        List<Role> listWithDuplicateRoles = Arrays.asList(SOFTWARE_ENGINEER, SOFTWARE_ENGINEER);
        assertThrows(DuplicateRoleException.class, () -> uniqueRoleList.setRoles(listWithDuplicateRoles));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueRoleList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void iterator_defaultList_isNotNull() {
        assertNotNull(uniqueRoleList.iterator());
    }

    @Test
    public void iterator_populatedList_iteratesInOrder() {
        uniqueRoleList.add(PRODUCT_MANAGER);
        uniqueRoleList.add(SOFTWARE_ENGINEER);

        Iterator<Role> iterator = uniqueRoleList.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(PRODUCT_MANAGER, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(SOFTWARE_ENGINEER, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        assertTrue(uniqueRoleList.equals(uniqueRoleList));
    }

    @Test
    public void equals_null_returnsFalse() {
        assertFalse(uniqueRoleList.equals(null));
    }

    @Test
    public void equals_differentTypes_returnsFalse() {
        assertFalse(uniqueRoleList.equals(5));
    }

    @Test
    public void equals_listsWithSameRoles_returnsTrue() {
        uniqueRoleList.add(PRODUCT_MANAGER);

        UniqueRoleList otherList = new UniqueRoleList();
        otherList.add(PRODUCT_MANAGER);

        assertTrue(uniqueRoleList.equals(otherList));
    }

    @Test
    public void equals_listsWithDifferentRoles_returnsFalse() {
        uniqueRoleList.add(PRODUCT_MANAGER);

        UniqueRoleList otherList = new UniqueRoleList();
        otherList.add(SOFTWARE_ENGINEER);

        assertFalse(uniqueRoleList.equals(otherList));
    }

    @Test
    public void hashCode_listsWithSameRoles_haveSameHashCode() {
        uniqueRoleList.add(PRODUCT_MANAGER);

        UniqueRoleList otherList = new UniqueRoleList();
        otherList.add(PRODUCT_MANAGER);

        assertEquals(uniqueRoleList.hashCode(), otherList.hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueRoleList.asUnmodifiableObservableList().toString(), uniqueRoleList.toString());
    }

    @Test
    public void setRole_editedRoleIsStrictlySameRole_success() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        uniqueRoleList.setRole(SOFTWARE_ENGINEER, SOFTWARE_ENGINEER);

        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(SOFTWARE_ENGINEER);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRole_editedRoleIsDifferentAndNotContained_success() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        uniqueRoleList.setRole(SOFTWARE_ENGINEER, PRODUCT_MANAGER);

        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(PRODUCT_MANAGER);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRoles_listWithSingleRole_success() {
        List<Role> singleRoleList = Collections.singletonList(SOFTWARE_ENGINEER);
        uniqueRoleList.setRoles(singleRoleList);

        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(SOFTWARE_ENGINEER);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRoles_listWithMultipleUniqueRoles_success() {
        List<Role> uniqueRoles = Arrays.asList(SOFTWARE_ENGINEER, PRODUCT_MANAGER);
        uniqueRoleList.setRoles(uniqueRoles);

        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(SOFTWARE_ENGINEER);
        expectedUniqueRoleList.add(PRODUCT_MANAGER);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRoles_listWithDuplicatesSpacedOut_throwsDuplicateRoleException() {
        Role duplicateSoftwareEngineer = new RoleBuilder(SOFTWARE_ENGINEER).build();
        List<Role> listWithDuplicates = Arrays.asList(PRODUCT_MANAGER, SOFTWARE_ENGINEER, duplicateSoftwareEngineer);

        assertThrows(DuplicateRoleException.class, () -> uniqueRoleList.setRoles(listWithDuplicates));
    }

    @Test
    public void setRole_sameIdentity_replacesSuccessfully() {
        UniqueRoleList list = new UniqueRoleList();
        Role target = new Role(new RoleName("Software Engineer"), new RoleDescription("Desc A"));
        Role edited = new Role(new RoleName("Software Engineer"), new RoleDescription("Desc B"));

        assertFalse(list.contains(target));

        list.add(target);
        list.setRole(target, edited);

        assertTrue(list.contains(edited));
    }

    @Test
    public void equals_branches() {
        UniqueRoleList list = new UniqueRoleList();
        list.add(new Role(new RoleName("Software Engineer"), new RoleDescription("Builds software")));

        assertTrue(list.equals(list));
        assertFalse(list.equals(null));
        assertFalse(list.equals(1));
    }

    @Test
    public void toString_nonEmptyList_containsCount() {
        UniqueRoleList list = new UniqueRoleList();
        list.add(new Role(new RoleName("Software Engineer"), new RoleDescription("Builds software")));

        assertTrue(list.toString().contains("Software Engineer"));
    }
}
