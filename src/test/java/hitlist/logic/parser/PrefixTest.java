package hitlist.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PrefixTest {

    @Test
    public void constructor_withoutSlash_addsSlash() {
        Prefix prefix = new Prefix("n");
        assertEquals("/n", prefix.getPrefix());
    }

    @Test
    public void constructor_withSlash_keepsSlash() {
        Prefix prefix = new Prefix("/n");
        assertEquals("/n", prefix.getPrefix());
    }

    @Test
    public void constructor_emptyString_returnsEmptyString() {
        Prefix prefix = new Prefix("");
        assertEquals("", prefix.getPrefix());
    }

    @Test
    public void constructor_null_returnsNull() {
        Prefix prefix = new Prefix(null);
        assertNull(prefix.getPrefix());
    }

    @Test
    public void constructor_withSpecialCharacters_keepsOriginal() {
        Prefix prefix = new Prefix("/@#$");
        assertEquals("/@#$", prefix.getPrefix());
    }

    @Test
    public void toString_returnsPrefix() {
        Prefix prefix = new Prefix("/test");
        assertEquals("/test", prefix.toString());
    }

    @Test
    public void hashCode_samePrefix_sameHashCode() {
        Prefix prefix1 = new Prefix("/n");
        Prefix prefix2 = new Prefix("/n");
        assertEquals(prefix1.hashCode(), prefix2.hashCode());
    }

    @Test
    public void hashCode_differentPrefix_differentHashCode() {
        Prefix prefix1 = new Prefix("/n");
        Prefix prefix2 = new Prefix("/p");
        assertNotEquals(prefix1.hashCode(), prefix2.hashCode());
    }

    @Test
    public void hashCode_nullPrefix_consistentHashCode() {
        Prefix prefix1 = new Prefix(null);
        Prefix prefix2 = new Prefix(null);
        assertEquals(prefix1.hashCode(), prefix2.hashCode());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Prefix prefix = new Prefix("/n");
        assertTrue(prefix.equals(prefix));
    }

    @Test
    public void equals_samePrefix_returnsTrue() {
        Prefix prefix1 = new Prefix("/n");
        Prefix prefix2 = new Prefix("/n");
        assertTrue(prefix1.equals(prefix2));
    }

    @Test
    public void equals_differentPrefix_returnsFalse() {
        Prefix prefix1 = new Prefix("/n");
        Prefix prefix2 = new Prefix("/p");
        assertNotEquals(prefix1, prefix2);
    }

    @Test
    public void equals_withSlashAndWithoutSlash_returnsTrue() {
        Prefix prefix1 = new Prefix("n");
        Prefix prefix2 = new Prefix("/n");
        assertEquals(prefix1, prefix2);
    }

    @Test
    public void equals_null_returnsFalse() {
        Prefix prefix = new Prefix("/n");
        assertNotEquals(null, prefix);
    }

    @Test
    public void equals_differentClass_returnsFalse() {
        Prefix prefix = new Prefix("/n");
        String notAPrefix = "/n";
        assertNotEquals(prefix, notAPrefix);
    }

    @Test
    public void equals_bothNull_returnsTrue() {
        Prefix prefix1 = new Prefix(null);
        Prefix prefix2 = new Prefix(null);
        assertTrue(prefix1.equals(prefix2));
    }

    @Test
    public void equals_oneNullOneNotNull_returnsFalse() {
        Prefix prefix1 = new Prefix(null);
        Prefix prefix2 = new Prefix("/n");
        assertNotEquals(prefix1, prefix2);
    }
}
