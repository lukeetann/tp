package hitlist.model.company;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import hitlist.model.company.exceptions.CompanyNotFoundException;
import hitlist.model.company.exceptions.DuplicateCompanyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of companies that enforces uniqueness between its elements and does not allow nulls.
 * A company is considered unique by comparing using {@link Company#isSameCompany(Company)}. As such, adding and
 * updating of companies uses Company#isSameCompany(Company) for equality to ensure that the company being added or
 * updated is unique in terms of identity in the UniqueCompanyList. However, the removal of a company uses
 * {@link Company#equals(Object)} to ensure that the company with exactly the same fields will be removed.
 * <br/>
 * Supports a minimal set of list operations.
 *
 * @see Company#isSameCompany(Company)
 */
public class UniqueCompanyList implements Iterable<Company> {

    private static final String DUPLICATE_COMPANY_MESSAGE = "This company %1$s already exists";
    private static final String COMPANY_NOT_FOUND_MESSAGE = "This company %1$s does not exist";

    private final ObservableList<Company> internalList = FXCollections.observableArrayList();
    private final ObservableList<Company> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent company as the given argument.
     * The company is considered equivalent if it is the same company as determined by
     * {@link Company#isSameCompany(Company)}.
     * The company must not be null.
     *
     * @param toCheck The company to check for existence in the list.
     * @return True if the list contains an equivalent company, false otherwise.
     */
    public boolean contains(Company toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameCompany);
    }

    /**
    * Adds a company to the list.
    * The company must not already exist in the list.
    *
    * @param toAdd The company to add to the list.
    * @throws DuplicateCompanyException If the company already exists in the list.
    */
    public void add(Company toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateCompanyException(DUPLICATE_COMPANY_MESSAGE);
        }
        internalList.add(toAdd);
    }

    /**
    * Replaces the company {@code target} in the list with {@code editedCompany}.
    * {@code target} must exist in the list.
    * The company identity of {@code editedCompany} must not be the same as another existing company in the list.
    *
    * @param target The company to be replaced in the list.
    * @param editedCompany The new company to replace the target company.
    * @throws CompanyNotFoundException If the target company does not exist in the list.
    * @throws DuplicateCompanyException If the edited company is not the same as the target company, and
    *                                   it already exists in the list.
    */
    public void setCompany(Company target, Company editedCompany) {
        requireAllNonNull(target, editedCompany);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new CompanyNotFoundException(COMPANY_NOT_FOUND_MESSAGE);
        }

        if (!target.isSameCompany(editedCompany) && contains(editedCompany)) {
            throw new DuplicateCompanyException(DUPLICATE_COMPANY_MESSAGE);
        }

        internalList.set(index, editedCompany);
    }

    /**
    * Removes the equivalent company from the list.
    * The company must exist in the list.
    *
    * @param toRemove The company to remove from the list.
    * @throws CompanyNotFoundException If the company does not exist in the list.
    */
    public void remove(Company toRemove) throws CompanyNotFoundException {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new CompanyNotFoundException(COMPANY_NOT_FOUND_MESSAGE);
        }
    }

    /**
     * Replaces the contents of this list with {@code replacement}.
     * {@code replacement} must not contain duplicate companies.
     *
     * @param replacement The UniqueCompanyList to replace the current list with.
     */
    public void setCompanies(UniqueCompanyList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
    * Replaces the contents of this list with {@code companies}.
    * {@code companies} must not contain duplicate companies.
    *
    * @param companies The list of companies to replace the current list with.
    * @throws DuplicateCompanyException If {@code companies} contains duplicate companies.
    */
    public void setCompanies(List<Company> companies) {
        requireNonNull(companies);
        if (!companiesAreUnique(companies)) {
            throw new DuplicateCompanyException(DUPLICATE_COMPANY_MESSAGE);
        }
        internalList.setAll(companies);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     *
     * @return An unmodifiable view of the backing list as an ObservableList.
     */
    public ObservableList<Company> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Company> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueCompanyList)) {
            return false;
        }

        UniqueCompanyList otherUniqueCompanyList = (UniqueCompanyList) other;
        return internalList.equals(otherUniqueCompanyList.internalList);
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
     * Returns true if {@code companies} contains only unique companies.
     *
     * @param companies The list of companies to check for uniqueness.
     * @return True if the list contains only unique companies, false otherwise.
     */
    private boolean companiesAreUnique(List<Company> companies) {
        for (int i = 0; i < companies.size() - 1; i++) {
            for (int j = i + 1; j < companies.size(); j++) {
                if (companies.get(i).isSameCompany(companies.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
