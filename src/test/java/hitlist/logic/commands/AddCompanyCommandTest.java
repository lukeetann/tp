package hitlist.logic.commands;

import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_DESCRIPTION_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_DESCRIPTION_META;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_META;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalCompanies.GOOGLE;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.HitList;
import hitlist.model.ModelStub;
import hitlist.model.ReadOnlyHitList;
import hitlist.model.company.Company;
import hitlist.testutil.CompanyBuilder;

public class AddCompanyCommandTest {

    @Test
    public void constructor_nullCompany_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCompanyCommand(null));
    }

    @Test
    public void execute_companyAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingCompanyAdded modelStub = new ModelStubAcceptingCompanyAdded();
        Company validCompany = new CompanyBuilder()
                .withName("Valid Company")
                .withDescription("Valid Description")
                .build();

        CommandResult commandResult = new AddCompanyCommand(validCompany).execute(modelStub);

        assertEquals(String.format(AddCompanyCommand.MESSAGE_SUCCESS, Messages.formatCompany(validCompany)),
                commandResult.getFeedbackToUser());
        assertEquals(List.of(validCompany), modelStub.companiesAdded);
    }

    @Test
    public void execute_duplicateCompany_throwsCommandException() {
        Company validCompany = new CompanyBuilder()
                .withName("Valid Company")
                .withDescription("Valid Description")
                .build();
        AddCompanyCommand addCompanyCommand = new AddCompanyCommand(validCompany);
        ModelStub modelStub = new ModelStubWithCompany(validCompany);

        String expectedMessage = String.format(AddCompanyCommand.MESSAGE_DUPLICATE_COMPANY, validCompany.getName());

        assertThrows(CommandException.class, expectedMessage, () -> addCompanyCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Company companyA = new CompanyBuilder()
                .withName(VALID_COMPANY_NAME_GOOGLE)
                .withDescription(VALID_COMPANY_DESCRIPTION_GOOGLE)
                .build();
        Company companyB = new CompanyBuilder()
                .withName(VALID_COMPANY_NAME_META)
                .withDescription(VALID_COMPANY_DESCRIPTION_META)
                .build();
        AddCompanyCommand addCompanyACommand = new AddCompanyCommand(companyA);
        AddCompanyCommand addCompanyBCommand = new AddCompanyCommand(companyB);

        // same object -> returns true
        assertTrue(addCompanyACommand.equals(addCompanyACommand));

        // same values -> returns true
        AddCompanyCommand addCompanyACommandCopy = new AddCompanyCommand(companyA);
        assertTrue(addCompanyACommand.equals(addCompanyACommandCopy));

        // different types -> returns false
        assertFalse(addCompanyACommand.equals(1));

        // null -> returns false
        assertFalse(addCompanyACommand.equals(null));

        // different company -> returns false
        assertFalse(addCompanyACommand.equals(addCompanyBCommand));
    }

    @Test
    public void toStringMethod() {
        AddCompanyCommand addCompanyCommand = new AddCompanyCommand(GOOGLE);
        String expectedString = AddCompanyCommand.class.getCanonicalName()
                + "{company=companyName=Google Inc., "
                + "companyDescription=A multinational technology company that specializes in Internet-related services "
                + "and products, roles=[]}";
        assertEquals(expectedString, addCompanyCommand.toString());
    }

    /**
     * A Model stub that contains a single company.
     */
    private class ModelStubWithCompany extends ModelStub {
        private final Company company;

        ModelStubWithCompany(Company company) {
            requireNonNull(company);
            this.company = company;
        }

        @Override
        public boolean hasCompany(Company company) {
            requireNonNull(company);
            return this.company.isSameCompany(company);
        }
    }

    /**
     * A Model stub that always accept the company being added.
     */
    private class ModelStubAcceptingCompanyAdded extends ModelStub {
        final ArrayList<Company> companiesAdded = new ArrayList<>();

        @Override
        public boolean hasCompany(Company company) {
            requireNonNull(company);
            return companiesAdded.stream().anyMatch(company::isSameCompany);
        }

        @Override
        public void addCompany(Company company) {
            requireNonNull(company);
            companiesAdded.add(company);
        }

        @Override
        public ReadOnlyHitList getHitList() {
            return new HitList();
        }
    }
}
