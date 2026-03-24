package hitlist.model.company;

import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CompanyNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CompanyName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CompanyName(""));
        assertThrows(IllegalArgumentException.class, () -> new CompanyName(" "));
        assertThrows(IllegalArgumentException.class, () -> new CompanyName("/"));
    }

    @Test
    public void isValidCompanyName() {
        assertThrows(NullPointerException.class, () -> CompanyName.isValidCompanyName(null));

        assertFalse(CompanyName.isValidCompanyName(""));
        assertFalse(CompanyName.isValidCompanyName(" "));
        assertFalse(CompanyName.isValidCompanyName("/"));
        assertFalse(CompanyName.isValidCompanyName("Company/Name"));
        assertFalse(CompanyName.isValidCompanyName("Company\nName"));

        assertTrue(CompanyName.isValidCompanyName("Google"));
        assertTrue(CompanyName.isValidCompanyName("12345"));
        assertTrue(CompanyName.isValidCompanyName("Google Inc."));
        assertTrue(CompanyName.isValidCompanyName("Google LLC"));
        assertTrue(CompanyName.isValidCompanyName("A".repeat(30)));
    }

    @Test
    public void toString_validName_returnsName() {
        CompanyName name = new CompanyName("Valid Company Name");
        assertEquals("Valid Company Name", name.toString());
    }

    @Test
    public void equals() {
        CompanyName name = new CompanyName("Valid Company Name");

        assertTrue(name.equals(new CompanyName("Valid Company Name")));
        assertTrue(name.equals(name));
        assertFalse(name.equals(null));
        assertFalse(name.equals(5.0f));
        assertFalse(name.equals(new CompanyName("Other Valid Company Name")));
    }

    @Test
    public void hashCode_sameValue_sameHash() {
        CompanyName name1 = new CompanyName("Valid Company Name");
        CompanyName name2 = new CompanyName("Valid Company Name");
        assertEquals(name1.hashCode(), name2.hashCode());
    }
}
