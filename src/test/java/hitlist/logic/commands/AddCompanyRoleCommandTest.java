package hitlist.logic.commands;

import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_GOOGLE;
import static hitlist.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_META;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_DESCRIPTION_SOFTWARE_ENGINEER;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_NAME_PRODUCT_MANAGER;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_NAME_SOFTWARE_ENGINEER;
import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import hitlist.logic.Messages;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.ModelStub;
import hitlist.model.company.Company;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.Role;
import hitlist.model.company.role.RoleDescription;
import hitlist.model.company.role.RoleName;
import hitlist.testutil.CompanyBuilder;
import hitlist.testutil.RoleBuilder;

public class AddCompanyRoleCommandTest {

    @Test
    public void constructor_nullRole_throwsNullPointerException() {
        CompanyName companyName = new CompanyBuilder().withName(VALID_COMPANY_NAME_GOOGLE).build().getCompanyName();
        assertThrows(NullPointerException.class, () -> new AddCompanyRoleCommand(null, companyName));
    }

    @Test
    public void constructor_nullCompanyName_throwsNullPointerException() {
        Role role = new RoleBuilder()
                .withName("Valid Role Name")
                .withDescription("Valid Role Description")
                .build();
        assertThrows(NullPointerException.class, () -> new AddCompanyRoleCommand(role, null));
    }

    @Test
    public void execute_roleAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingCompanyRoleAdded modelStub = new ModelStubAcceptingCompanyRoleAdded();
        Role validCompanyRole = new RoleBuilder()
                .withName("Valid Role Name")
                .withDescription("Valid Role Description")
                .build();
        Company google = new CompanyBuilder().withName(VALID_COMPANY_NAME_GOOGLE).build();
        modelStub.addCompany(google);
        CompanyName companyName = google.getCompanyName();

        CommandResult commandResult = new AddCompanyRoleCommand(validCompanyRole, companyName).execute(modelStub);

        assertEquals(
                String.format(AddCompanyRoleCommand.MESSAGE_SUCCESS, Messages.formatCompanyRole(validCompanyRole)),
                commandResult.getFeedbackToUser());
        assertEquals(List.of(validCompanyRole), modelStub.companyRolesAdded);
        assertEquals(List.of(companyName), modelStub.companyNamesAdded);
        assertTrue(commandResult.isShowRoleList());
        assertFalse(commandResult.isShowHelp());
        assertFalse(commandResult.isExit());
    }

    @Test
    public void execute_duplicateCompanyRole_throwsCommandException() {
        Role validCompanyRole = new RoleBuilder()
                .withName("Valid Role Name")
                .withDescription("Valid Role Description")
                .build();
        CompanyName companyName = new CompanyBuilder().withName(VALID_COMPANY_NAME_GOOGLE).build().getCompanyName();
        AddCompanyRoleCommand addCompanyRoleCommand = new AddCompanyRoleCommand(validCompanyRole, companyName);
        ModelStub modelStub = new ModelStubWithCompanyRole(validCompanyRole, companyName);

        String expectedMessage = String.format(AddCompanyRoleCommand.MESSAGE_DUPLICATE_COMPANY_ROLE,
                validCompanyRole.getRoleName());

        assertThrows(CommandException.class, expectedMessage, () -> addCompanyRoleCommand.execute(modelStub));
    }

    @Test
    public void execute_companyNotFound_throwsCommandException() {
        Role validCompanyRole = new RoleBuilder()
                .withName("Valid Role Name")
                .withDescription("Valid Role Description")
                .build();
        CompanyName companyName = new CompanyName("NonExistentCompany");
        AddCompanyRoleCommand addCompanyRoleCommand = new AddCompanyRoleCommand(validCompanyRole, companyName);
        ModelStub modelStub = new ModelStubWithNoCompany(validCompanyRole, companyName);

        String expectedMessage = String.format(ListCompanyCommand.MESSAGE_NO_COMPANY_FOUND, companyName);

        assertThrows(CommandException.class, expectedMessage, () -> addCompanyRoleCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Role roleA = new RoleBuilder()
                .withName(VALID_ROLE_NAME_SOFTWARE_ENGINEER)
                .withDescription(VALID_ROLE_DESCRIPTION_SOFTWARE_ENGINEER)
                .build();
        Role roleB = new RoleBuilder()
                .withName(VALID_ROLE_NAME_PRODUCT_MANAGER)
                .withDescription(VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER)
                .build();

        CompanyName companyA = new CompanyBuilder().withName(VALID_COMPANY_NAME_GOOGLE).build().getCompanyName();
        CompanyName companyB = new CompanyBuilder().withName(VALID_COMPANY_NAME_META).build().getCompanyName();

        AddCompanyRoleCommand addRoleACommand = new AddCompanyRoleCommand(roleA, companyA);
        AddCompanyRoleCommand addRoleBCommand = new AddCompanyRoleCommand(roleB, companyA);

        assertTrue(addRoleACommand.equals(addRoleACommand));

        AddCompanyRoleCommand addRoleACommandCopy = new AddCompanyRoleCommand(roleA, companyA);
        assertTrue(addRoleACommand.equals(addRoleACommandCopy));

        assertFalse(addRoleACommand.equals(1));
        assertFalse(addRoleACommand.equals(null));
        assertFalse(addRoleACommand.equals(addRoleBCommand));

        AddCompanyRoleCommand addRoleDifferentCompany = new AddCompanyRoleCommand(roleA, companyB);
        assertFalse(addRoleACommand.equals(addRoleDifferentCompany));
    }

    @Test
    public void toStringMethod() {
        Role role = new RoleBuilder()
                .withName(VALID_ROLE_NAME_SOFTWARE_ENGINEER)
                .withDescription(VALID_ROLE_DESCRIPTION_SOFTWARE_ENGINEER)
                .build();
        CompanyName companyName = new CompanyBuilder().withName(VALID_COMPANY_NAME_GOOGLE).build().getCompanyName();

        AddCompanyRoleCommand addCompanyRoleCommand = new AddCompanyRoleCommand(role, companyName);

        String expectedString = AddCompanyRoleCommand.class.getCanonicalName()
                + "{companyName=" + companyName + ", role=" + role + "}";
        assertEquals(expectedString, addCompanyRoleCommand.toString());
    }

    @Test
    public void toString_containsRoleAndCompanyName() {
        Role role = new Role(new RoleName("Software Engineer"), new RoleDescription("Builds"));
        CompanyName company = new CompanyName("Google Inc.");

        AddCompanyRoleCommand command = new AddCompanyRoleCommand(role, company);
        String value = command.toString();

        assertTrue(value.contains("Software Engineer"));
        assertTrue(value.contains("Google Inc."));
    }

    private static class ModelStubWithCompanyRole extends ModelStub {
        private final Role role;
        private final CompanyName companyName;

        ModelStubWithCompanyRole(Role role, CompanyName companyName) {
            this.role = role;
            this.companyName = companyName;
        }

        @Override
        public boolean hasCompanyByName(CompanyName companyName) {
            return this.companyName.equals(companyName);
        }

        @Override
        public boolean hasCompanyRole(CompanyName companyName, Role role) {
            return this.companyName.equals(companyName) && this.role.isSameRole(role);
        }
    }

    private static class ModelStubWithNoCompany extends ModelStub {
        private final Role role;
        private final CompanyName companyName;

        ModelStubWithNoCompany(Role role, CompanyName companyName) {
            this.role = role;
            this.companyName = companyName;
        }

        @Override
        public boolean hasCompanyByName(CompanyName companyName) {
            return false;
        }

        @Override
        public boolean hasCompanyRole(CompanyName companyName, Role role) {
            return this.companyName.equals(companyName) && this.role.isSameRole(role);
        }
    }

    private static class ModelStubAcceptingCompanyRoleAdded extends ModelStub {
        final List<Role> companyRolesAdded = new ArrayList<>();
        final List<CompanyName> companyNamesAdded = new ArrayList<>();

        @Override
        public boolean hasCompanyRole(CompanyName companyName, Role role) {
            return companyRolesAdded.contains(role);
        }

        @Override
        public boolean hasCompanyByName(CompanyName companyName) {
            return companyNamesAdded.contains(companyName);
        }

        @Override
        public void addCompany(Company company) {
            companyNamesAdded.add(company.getCompanyName());
        }

        @Override
        public void addCompanyRole(CompanyName companyName, Role role) {
            companyRolesAdded.add(role);
        }

        @Override
        public void updateRoleList(CompanyName companyName) {
            // no-op for test
        }
    }
}
