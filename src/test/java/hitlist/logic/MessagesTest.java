package hitlist.logic;

import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY_DESC;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import hitlist.model.company.Company;
import hitlist.model.company.CompanyDescription;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.Role;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.person.Person;
import hitlist.testutil.PersonBuilder;
import hitlist.testutil.RoleBuilder;

/**
 * Tests for {@link Messages}.
 */
public class MessagesTest {

    @Test
    public void getErrorMessageForDuplicatePrefixes_singlePrefix_success() {
        String message = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME);

        assertTrue(message.contains("Multiple values specified for the following single-valued field(s):"));
        assertTrue(message.contains(PREFIX_NAME.toString()));
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_multiplePrefixes_success() {
        String message = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY, PREFIX_COMPANY_DESC);

        assertTrue(message.contains(PREFIX_COMPANY.toString()));
        assertTrue(message.contains(PREFIX_COMPANY_DESC.toString()));
    }

    @Test
    public void format_validPerson_success() {
        Person person = new PersonBuilder()
                .withName("Amy Bee")
                .withPhone("88888888")
                .withEmail("amy@example.com")
                .withAddress("123 Clementi Ave")
                .withTags("friends")
                .build();

        String formatted = Messages.format(person);

        assertTrue(formatted.contains("Amy Bee"));
        assertTrue(formatted.contains("88888888"));
        assertTrue(formatted.contains("amy@example.com"));
        assertTrue(formatted.contains("123 Clementi Ave"));
        assertTrue(formatted.contains("friends"));
    }

    @Test
    public void formatGroup_validGroup_success() {
        Group group = new Group(new GroupName("Students"));

        String formatted = Messages.formatGroup(group);

        assertTrue(formatted.contains("Students"));
    }

    @Test
    public void formatCompany_validCompany_success() {
        Company company = new Company(new CompanyName("Google"), new CompanyDescription("Tech company"));

        String formatted = Messages.formatCompany(company);

        assertTrue(formatted.contains("Google"));
        assertTrue(formatted.contains("Tech company"));
    }

    @Test
    public void formatCompany_exactOutput_success() {
        Company company = new Company(new CompanyName("Meta"), new CompanyDescription("Social platform"));

        String formatted = Messages.formatCompany(company);

        String expectedStart = "Name: Meta; Description: Social platform";
        assertEquals(expectedStart, formatted);
    }

    @Test
    public void formatCompanyRole_validRole_success() {
        Role role = new RoleBuilder()
                .withName("Software Engineer")
                .withDescription("Develops software")
                .build();

        String formatted = Messages.formatCompanyRole(role);

        assertTrue(formatted.contains("Software Engineer"));
        assertTrue(formatted.contains("Develops software"));
    }

    @Test
    public void formatCompanyRole_exactOutput_success() {
        Role role = new RoleBuilder()
                .withName("Product Manager")
                .withDescription("Manages product roadmap")
                .build();

        String formatted = Messages.formatCompanyRole(role);

        String expected = "Role: Product Manager; Description: Manages product roadmap";
        assertEquals(expected, formatted);
    }
}
