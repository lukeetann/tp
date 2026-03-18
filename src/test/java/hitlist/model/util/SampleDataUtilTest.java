package hitlist.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import hitlist.model.ReadOnlyHitList;
import hitlist.model.person.Person;

public class SampleDataUtilTest {
    @Test
    public void getSamplePersons_returnsValidPersons() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals(6, persons.length); // Example assertion
        // Assert each person has present email/address, etc.
    }

    @Test
    public void getSampleHitList_returnsValidHitList() {
        ReadOnlyHitList hitList = SampleDataUtil.getSampleHitList();
        assertNotNull(hitList);
    }
}
