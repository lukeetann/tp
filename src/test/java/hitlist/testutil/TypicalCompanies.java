package hitlist.testutil;

import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_DESCRIPTION_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_DESCRIPTION_META;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_META;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hitlist.model.HitList;
import hitlist.model.company.Company;

/**
 * A utility class containing a list of {@code Company} objects to be used in tests.
 */
public class TypicalCompanies {

    public static final Company MICROSOFT = new CompanyBuilder()
            .withName("Microsoft Corporation")
            .withDescription("An American multinational technology company with headquarters in Redmond, Washington. "
                    + "It develops, manufactures, licenses, supports and sells computer software, consumer electronics,"
                    + " personal computers, and related services")
            .build();

    public static final Company DELL = new CompanyBuilder()
            .withName("Dell Technologies Inc.")
            .withDescription("An American multinational technology company based in Round Rock, Texas. It develops, "
                    + "sells, repairs, and supports computers and related products and services")
            .build();

    // Manually added - Company's details found in {@code CommandTestUtil}
    public static final Company GOOGLE = new CompanyBuilder()
            .withName(VALID_COMPANY_NAME_GOOGLE)
            .withDescription(VALID_COMPANY_DESCRIPTION_GOOGLE)
            .build();

    public static final Company META = new CompanyBuilder()
            .withName(VALID_COMPANY_NAME_META)
            .withDescription(VALID_COMPANY_DESCRIPTION_META)
            .build();

    private TypicalCompanies() {} // prevents instantiation

    /**
     * Returns an {@code HitList} with all the typical companies.
     */
    public static HitList getTypicalHitList() {
        HitList hl = new HitList();
        for (Company company : getTypicalCompanies()) {
            hl.addCompany(company);
        }
        return hl;
    }

    public static List<Company> getTypicalCompanies() {
        return new ArrayList<>(Arrays.asList(MICROSOFT, DELL, GOOGLE, META));
    }
}
