package hitlist.model.company.role;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import hitlist.model.company.role.exceptions.DuplicateRoleException;
import hitlist.model.company.role.exceptions.RoleNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of roles that enforces uniqueness between its elements and does not allow nulls.
 * A role is considered unique by comparing using {@link Role#isSameRole(Role)}. As such, adding and updating of
 * roles uses {@link Role#isSameRole(Role)} for equality to ensure that the role being added or updated is unique in
 * terms of identity in the UniqueRoleList. However, the removal of a role uses Role#equals(Object) to ensure that the
 * role with exactly the same fields will be removed.
 * <br />
 * Supports a minimal set of list operations.
 *
 * @see Role#isSameRole(Role)
 */
public class UniqueRoleList implements Iterable<Role> {

    private static final String DUPLICATE_ROLE_MESSAGE = "This role %1$s already exists";
    private static final String ROLE_NOT_FOUND_MESSAGE = "This role %1$s does not exist";

    private final ObservableList<Role> internalList = FXCollections.observableArrayList();
    private final ObservableList<Role> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent role as the given argument.
     * The role is considered equivalent if it is the same role as determined by
     * {@link Role#isSameRole(Role)}.
     * The role must not be null.
     *
     * @param toCheck The role to check for existence in the list.
     * @return True if the list contains an equivalent role, false otherwise.
     */
    public boolean contains(Role toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameRole);
    }

    /**
     * Adds a role to the list.
     * The role must not already exist in the list.
     *
     * @param toAdd The role to add to the list.
     * @throws DuplicateRoleException If the role already exists in the list.
     */
    public void add(Role toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateRoleException(DUPLICATE_ROLE_MESSAGE);
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the role {@code target} in the list with {@code editedRole}.
     * {@code target} must exist in the list.
     * The role identity of {@code editedRole} must not be the same as another existing role in the list.
     *
     * @param target The role to be replaced in the list.
     * @param editedRole The role to replace the target role with.
     * @throws RoleNotFoundException If the target role does not exist in the list.
     * @throws DuplicateRoleException If the edited role's identity is the same as another existing role in the list.
     */
    public void setRole(Role target, Role editedRole) {
        requireAllNonNull(target, editedRole);

        int index = -1;
        for (int i = 0; i < internalList.size(); i++) {
            if (internalList.get(i).isSameRole(target)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new RoleNotFoundException(ROLE_NOT_FOUND_MESSAGE);
        }

        if (!target.isSameRole(editedRole) && contains(editedRole)) {
            throw new DuplicateRoleException(DUPLICATE_ROLE_MESSAGE);
        }

        internalList.set(index, editedRole);
        assert internalList.get(index).equals(editedRole);
    }

    /**
     * Removes the equivalent role from the list.
     * The role must exist in the list.
     *
     * @param toRemove The role to remove from the list.
     * @throws RoleNotFoundException If no such role could be found in the list.
     */
    public void remove(Role toRemove) throws RoleNotFoundException {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new RoleNotFoundException(ROLE_NOT_FOUND_MESSAGE);
        }
        assert !internalList.contains(toRemove);
    }

    /**
     * Replaces the contents of this list with {@code replacement}.
     * {@code replacement} must not be null.
     *
     * @param replacement The new list of roles to replace the existing list of roles.
     */
    public void setRoles(UniqueRoleList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
        assert internalList.equals(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code roles}.
     * {@code roles} must not contain duplicate roles.
     *
     * @param roles The new list of roles to replace the existing list of roles.
     * @throws DuplicateRoleException If {@code roles} contains duplicate roles.
     */
    public void setRoles(List<Role> roles) {
        requireNonNull(roles);
        if (!rolesAreUnique(roles)) {
            throw new DuplicateRoleException(DUPLICATE_ROLE_MESSAGE);
        }

        internalList.setAll(roles);
        assert internalList.size() == roles.size();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Role> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Role> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueRoleList)) {
            return false;
        }

        UniqueRoleList otherUniqueRoleList = (UniqueRoleList) other;
        return internalList.equals(otherUniqueRoleList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code roles} contains only unique roles.
     *
     * @param roles The list of roles to check for uniqueness.
     * @return True if the list of roles contains only unique roles, false otherwise.
     */
    private boolean rolesAreUnique(List<Role> roles) {
        requireNonNull(roles);
        for (int i = 0; i < roles.size(); i++) {
            for (int j = i + 1; j < roles.size(); j++) {
                if (roles.get(i).isSameRole(roles.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
