package hitlist.model.company;

import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CompanyDescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CompanyDescription(null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        String invalidDescription = "";
        assertThrows(IllegalArgumentException.class, () -> new CompanyDescription(invalidDescription));
    }

    @Test
    public void isValidCompanyDescription() {
        // null description
        assertThrows(NullPointerException.class, () -> CompanyDescription.isValidCompanyDescription(null));

        // invalid description
        assertFalse(CompanyDescription.isValidCompanyDescription("")); // empty string
        assertFalse(CompanyDescription.isValidCompanyDescription(" ")); // spaces only
        assertFalse(CompanyDescription.isValidCompanyDescription("/")); // forward slash only
        assertFalse(CompanyDescription.isValidCompanyDescription("Company/Name")); // contains forward slash
        assertFalse(CompanyDescription.isValidCompanyDescription("Company\nDescription")); // contains line break

        // valid description
        assertTrue(CompanyDescription.isValidCompanyDescription("Google")); // alphabets only
        assertTrue(CompanyDescription.isValidCompanyDescription("12345")); // numbers only
        assertTrue(CompanyDescription.isValidCompanyDescription("An American multinational technology company with "
                + "headquarters in Redmond, Washington. It develops, manufactures, licenses, supports and sells "
                + "computer software, consumer electronics, personal computers, "
                + "and related services")); // long description
    }

    @Test
    public void toString_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CompanyDescription(null).toString());
    }

    @Test
    public void toString_validDescription_returnsDescription() {
        CompanyDescription description = new CompanyDescription("Valid Company Description");
        assertTrue(description.toString().equals("Valid Company Description"));
    }

    @Test
    public void equals() {
        CompanyDescription description = new CompanyDescription("Valid Company Description");

        // same values -> returns true
        assertTrue(description.equals(new CompanyDescription("Valid Company Description")));

        // same object -> returns true
        assertTrue(description.equals(description));

        // null -> returns false
        assertFalse(description.equals(null));

        // different types -> returns false
        assertFalse(description.equals(5.0f));

        // different values -> returns false
        assertNotEquals(description, new CompanyDescription("Other Valid Company Description"));
    }

    @Test
    public void hashCode_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CompanyDescription(null).hashCode());
    }

    @Test
    public void hashCode_validDescription_returnsHashCode() {
        CompanyDescription description1 = new CompanyDescription("Valid Company Description");
        CompanyDescription description2 = new CompanyDescription("Valid Company Description");
        CompanyDescription description3 = new CompanyDescription("Other Valid Company Description");

        // same values -> same hash code
        assertEquals(description1.hashCode(), description2.hashCode());

        // different values -> different hash code
        assertNotEquals(description1.hashCode(), description3.hashCode());
    }
}
