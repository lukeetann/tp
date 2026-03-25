package hitlist.testutil;

import hitlist.model.company.Company;
import hitlist.model.company.CompanyDescription;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.UniqueRoleList;

/**
 * A utility class to help with building Company objects.
 */
public class CompanyBuilder {

    public static final String DEFAULT_NAME = "Google Inc.";
    public static final String DEFAULT_DESCRIPTION =
            "A multinational technology company that specializes in Internet-related services and products.";

    private CompanyName name;
    private CompanyDescription description;
    private UniqueRoleList uniqueRoleList;

    /**
     * Creates a {@code CompanyBuilder} with the default details.
     */
    public CompanyBuilder() {
        name = new CompanyName(DEFAULT_NAME);
        description = new CompanyDescription(DEFAULT_DESCRIPTION);
        uniqueRoleList = new UniqueRoleList();
    }

    /**
    * Initializes the CompanyBuilder with the data of {@code companyToCopy}.
    */
    public CompanyBuilder(Company companyToCopy) {
        name = companyToCopy.getName();
        description = companyToCopy.getDescription();
        uniqueRoleList = companyToCopy.getUniqueRoleList();
    }

    /**
     * Sets the {@code Name} of the {@code Company} that we are building.
     */
    public CompanyBuilder withName(String name) {
        this.name = new CompanyName(name);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Company} that we are building.
     */
    public CompanyBuilder withDescription(String description) {
        this.description = new CompanyDescription(description);
        return this;
    }

    /**
     * Sets the {@code UniqueRoleList} of the {@code Company} that we are building.
     */
    public CompanyBuilder withUniqueRoleList(UniqueRoleList uniqueRoleList) {
        this.uniqueRoleList = uniqueRoleList;
        return this;
    }

    /**
     * Builds a Company object with the given details.
     */
    public Company build() {
        return new Company(name, description, uniqueRoleList);
    }
}
