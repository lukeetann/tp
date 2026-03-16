package hitlist.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hitlist.model.person.Person;

public class SampleDataUtilTest {
    @Test
    public void getSamplePersons_returnsValidPersons() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals(6, persons.length); // Example assertion
        // Assert each person has present email/address, etc.
    }

}
