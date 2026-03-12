package hitlist.model.company;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import hitlist.model.company.exceptions.CompanyNotFoundException;
import hitlist.model.company.exceptions.DuplicateCompanyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of companies that enforces uniqueness between its elements and does not allow nulls.
 * A company is considered unique by comparing using {@code Company#isSameCompany(Company)}.
 * As such, adding and updating of companies uses Company#isSameCompany(Company) for equality to
 * ensure that the company being added or updated is unique in terms of identity in the UniqueCompanyList.
 * However, the removal of a company uses {@code Company#equals(Object)}to ensure that the company
 * with exactly the same fields will be removed.
 * Supports a minimal set of list operations.
 *
 * @see Company#isSameCompany(Company)
 */
public class UniqueCompanyList implements Iterable<Company> {

    private final ObservableList<Company> internalList = FXCollections.observableArrayList();
    private final ObservableList<Company> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent company as the given argument.
     */
    public boolean contains(Company toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameCompany);
    }

    /**
     * Adds a company to the list.
     * The company must not already exist in the list.
     */
    public void add(Company toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateCompanyException("This company " + toAdd.getName() + " already exists");
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the company {@code target} in the list with {@code editedCompany}.
     * {@code target} must exist in the list.
     * The company identity of {@code editedCompany} must not be the same as another existing company in the list.
     */
    public void setCompany(Company target, Company editedCompany) {
        requireNonNull(target);
        requireNonNull(editedCompany);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new CompanyNotFoundException("Target company does not exist");
        }

        if (!target.isSameCompany(editedCompany) && contains(editedCompany)) {
            throw new DuplicateCompanyException("This company " + editedCompany.getName() + " already exists");
        }

        internalList.set(index, editedCompany);
    }

    /**
     * Removes the equivalent company from the list.
     * The company must exist in the list.
     */
    public void remove(Company toRemove) {
        requireNonNull(toRemove);
        internalList.remove(toRemove);
    }

    /**
     * Replaces the contents of this list with the companies from the given UniqueCompanyList.
     * Since the parameter is already a UniqueCompanyList, it is inherently guaranteed
     * to contain no duplicate companies, so no further validation is needed.
     *
     * @param replacement The UniqueCompanyList containing the replacement companies.
     */
    public void setCompanies(UniqueCompanyList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with the provided standard list of Company objects.
     * Because a standard List does not enforce uniqueness, this method explicitly
     * checks that all companies are unique before updating the list.
     *
     * @param companies The list of companies to set.
     * @throws DuplicateCompanyException If the provided list contains any duplicate companies.
     */
    public void setCompanies(List<Company> companies) {
        requireNonNull(companies);
        if (!companiesAreUnique(companies)) {
            throw new DuplicateCompanyException("There is already a company with the same name and description");
        }
        internalList.setAll(companies);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
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
