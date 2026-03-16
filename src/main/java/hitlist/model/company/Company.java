package hitlist.model.company;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import hitlist.model.company.role.UniqueRoleList;

/**
 * Represents a Company in the hitlist.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Company {
    private final CompanyName name;
    private final CompanyDescription description;
    private final UniqueRoleList uniqueRoleList;

    /**
     * Constructs a {@code Company} with the given name and description.
     *
     * @param name The name of the company.
     * @param description The description of the company.
     */
    public Company(CompanyName name, CompanyDescription description) {
        requireAllNonNull(name, description);
        this.name = name;
        this.description = description;
        this.uniqueRoleList = new UniqueRoleList();
    }

    /**
     * Constructs a {@code Company} with the given name, description and list of roles.
     *
     * @param name The name of the company.
     * @param description The description of the company.
     * @param uniqueRoleList The list of roles associated with the company.
     */
    public Company(CompanyName name, CompanyDescription description, UniqueRoleList uniqueRoleList) {
        requireAllNonNull(name, description, uniqueRoleList);
        this.name = name;
        this.description = description;
        this.uniqueRoleList = uniqueRoleList;
    }

    /**
     * Gets the name of the company.
     *
     * @return The name of the company.
     */
    public CompanyName getName() {
        return this.name;
    }

    /**
     * Gets the description of the company.
     *
     * @return The description of the company.
     */
    public CompanyDescription getDescription() {
        return this.description;
    }

    /**
     * Gets the list of roles associated with the company.
     *
     * @return The list of roles associated with the company.
     */
    public UniqueRoleList getUniqueRoleList() {
        return this.uniqueRoleList;
    }

    /**
    * Returns true if both companies have the same name and description.
    * This defines a weaker notion of equality between two companies.
    *
    * @param otherCompany The company to compare with this company.
    * @return True if both companies have the same name and description, false otherwise.
    */
    public boolean isSameCompany(Company otherCompany) {
        if (otherCompany == this) {
            return true;
        }

        return otherCompany != null
                && this.name.equals(otherCompany.name)
                && this.description.equals(otherCompany.description);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Company)) {
            return false;
        }

        Company otherCompany = (Company) other;
        return this.name.equals(otherCompany.name)
                && this.description.equals(otherCompany.description)
                && this.uniqueRoleList.equals(otherCompany.uniqueRoleList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, uniqueRoleList);
    }

    @Override
    public String toString() {
        return "companyName=" + name + ", companyDescription=" + description + ", roles=" + uniqueRoleList;
    }
}
