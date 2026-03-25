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
        assertThrows(IllegalArgumentException.class, () -> new CompanyDescription(""));
        assertThrows(IllegalArgumentException.class, () -> new CompanyDescription(" "));
        assertThrows(IllegalArgumentException.class, () -> new CompanyDescription("/"));
    }

    @Test
    public void isValidCompanyDescription() {
        assertThrows(NullPointerException.class, () -> CompanyDescription.isValidCompanyDescription(null));

        assertFalse(CompanyDescription.isValidCompanyDescription(""));
        assertFalse(CompanyDescription.isValidCompanyDescription(" "));
        assertFalse(CompanyDescription.isValidCompanyDescription("/"));
        assertFalse(CompanyDescription.isValidCompanyDescription("Company/Name"));
        assertFalse(CompanyDescription.isValidCompanyDescription("Company\nDescription"));

        assertTrue(CompanyDescription.isValidCompanyDescription("Google"));
        assertTrue(CompanyDescription.isValidCompanyDescription("12345"));
        assertTrue(CompanyDescription.isValidCompanyDescription("A".repeat(999)));
    }

    @Test
    public void constructor_trimsWhitespace_success() {
        CompanyDescription description = new CompanyDescription("  Valid Description  ");
        assertEquals("Valid Description", description.toString());
    }

    @Test
    public void toString_validDescription_returnsDescription() {
        CompanyDescription description = new CompanyDescription("Valid Company Description");
        assertEquals("Valid Company Description", description.toString());
    }

    @Test
    public void equals() {
        CompanyDescription description = new CompanyDescription("Valid Company Description");

        assertTrue(description.equals(new CompanyDescription("Valid Company Description")));
        assertTrue(description.equals(description));
        assertFalse(description.equals(null));
        assertFalse(description.equals(5.0f));
        assertNotEquals(description, new CompanyDescription("Other Valid Company Description"));
    }

    @Test
    public void hashCode_validDescription_returnsHashCode() {
        CompanyDescription description1 = new CompanyDescription("Valid Company Description");
        CompanyDescription description2 = new CompanyDescription("Valid Company Description");
        CompanyDescription description3 = new CompanyDescription("Other Valid Company Description");

        assertEquals(description1.hashCode(), description2.hashCode());
        assertNotEquals(description1.hashCode(), description3.hashCode());
    }
}
