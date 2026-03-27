package hitlist.logic.commands;

import static hitlist.logic.commands.CommandTestUtil.assertCommandSuccess;
import static hitlist.model.Model.PREDICATE_SHOW_ALL_COMPANIES;
import static hitlist.testutil.TypicalCompanies.getTypicalHitList;
import static hitlist.ui.UiPaneVisibility.SHOW_COMPANY_LIST;
import static hitlist.ui.UiPaneVisibility.SHOW_ROLE_LIST;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hitlist.model.Model;
import hitlist.model.ModelManager;
import hitlist.model.UserPrefs;
import hitlist.model.company.CompanyName;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCompanyCommand.
 */
public class ListCompanyCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalHitList(), new UserPrefs());
        expectedModel = new ModelManager(model.getHitList(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        expectedModel.updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);
        assertCommandSuccess(new ListCompanyCommand(), model,
                new CommandResult(ListCompanyCommand.DEFAULT_MESSAGE_SUCCESS, SHOW_COMPANY_LIST), expectedModel);
    }

    @Test
    public void execute_listSpecificCompany_showsCompany() {
        // Assume "Google" exists in typical hit list
        CompanyName companyName = new CompanyName("Google Inc.");
        ListCompanyCommand command = new ListCompanyCommand(companyName);

        // Execute branch that lists a specific company
        expectedModel.updateRoleList(companyName); // sets model to only show Google's roles
        assertCommandSuccess(command, model,
                new CommandResult(String.format(ListCompanyCommand.MESSAGE_SUCCESS, companyName), SHOW_ROLE_LIST),
                expectedModel);
    }
}
