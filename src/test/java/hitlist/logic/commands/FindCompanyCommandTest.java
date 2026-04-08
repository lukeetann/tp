package hitlist.logic.commands;

import static hitlist.logic.Messages.MESSAGE_COMPANY_LISTED_OVERVIEW;
import static hitlist.logic.Messages.MESSAGE_NO_COMPANIES_FOUND;
import static hitlist.logic.commands.CommandTestUtil.assertCommandSuccess;
import static hitlist.testutil.TypicalCompanies.GOOGLE;
import static hitlist.testutil.TypicalCompanies.META;
import static hitlist.testutil.TypicalCompanies.MICROSOFT;
import static hitlist.testutil.TypicalCompanies.getTypicalHitList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import hitlist.model.Model;
import hitlist.model.ModelManager;
import hitlist.model.UserPrefs;
import hitlist.model.company.CompanyMatchesFindPredicate;
import hitlist.ui.UiPaneVisibility;

public class FindCompanyCommandTest {

    private final Model model = new ModelManager(getTypicalHitList(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalHitList(), new UserPrefs());

    @Test
    public void equals() {
        CompanyMatchesFindPredicate firstPredicate =
                new CompanyMatchesFindPredicate(Collections.singletonList("first"));
        CompanyMatchesFindPredicate secondPredicate =
                new CompanyMatchesFindPredicate(Collections.singletonList("second"));

        FindCompanyCommand findFirstCompanyCommand = new FindCompanyCommand(firstPredicate);
        FindCompanyCommand findSecondCompanyCommand = new FindCompanyCommand(secondPredicate);

        assertTrue(findFirstCompanyCommand.equals(findFirstCompanyCommand));

        FindCompanyCommand findFirstCommandCopy = new FindCompanyCommand(firstPredicate);
        assertTrue(findFirstCompanyCommand.equals(findFirstCommandCopy));

        assertFalse(findFirstCompanyCommand.equals(1));
        assertFalse(findFirstCompanyCommand.equals(null));
        assertFalse(findFirstCompanyCommand.equals(findSecondCompanyCommand));
    }

    @Test
    public void execute_noMatchingKeyword_noCompanyFound() {
        CompanyMatchesFindPredicate predicate = preparePredicate("nomatch");

        FindCompanyCommand command = new FindCompanyCommand(predicate);
        expectedModel.updateFilteredCompanyList(predicate);

        CommandResult expectedCommandResult = new CommandResult(
                MESSAGE_NO_COMPANIES_FOUND, UiPaneVisibility.SHOW_COMPANY_LIST);

        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredCompanyList());
    }

    @Test
    public void execute_multipleKeywords_multipleCompaniesFound() {
        String expectedMessage = String.format(MESSAGE_COMPANY_LISTED_OVERVIEW, 3);
        CompanyMatchesFindPredicate predicate = preparePredicate("Goo et soft");

        FindCompanyCommand command = new FindCompanyCommand(predicate);
        expectedModel.updateFilteredCompanyList(predicate);

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage, UiPaneVisibility.SHOW_COMPANY_LIST);

        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
        assertTrue(model.getFilteredCompanyList().containsAll(Arrays.asList(GOOGLE, META, MICROSOFT)));
    }

    @Test
    public void execute_singleKeyword_singleCompanyFound() {
        String expectedMessage = String.format(MESSAGE_COMPANY_LISTED_OVERVIEW, 1);
        CompanyMatchesFindPredicate predicate = preparePredicate("Google");

        FindCompanyCommand command = new FindCompanyCommand(predicate);
        expectedModel.updateFilteredCompanyList(predicate);

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage, UiPaneVisibility.SHOW_COMPANY_LIST);

        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
        assertEquals(Arrays.asList(GOOGLE), model.getFilteredCompanyList());
    }

    @Test
    public void execute_partialKeywordMatch_companyFound() {
        String expectedMessage = String.format(MESSAGE_COMPANY_LISTED_OVERVIEW, 1);
        CompanyMatchesFindPredicate predicate = preparePredicate("Goo");

        FindCompanyCommand command = new FindCompanyCommand(predicate);
        expectedModel.updateFilteredCompanyList(predicate);

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage, UiPaneVisibility.SHOW_COMPANY_LIST);

        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
        assertEquals(Arrays.asList(GOOGLE), model.getFilteredCompanyList());
    }

    @Test
    public void execute_keywordCaseInsensitive_companyFound() {
        String expectedMessage = String.format(MESSAGE_COMPANY_LISTED_OVERVIEW, 1);
        CompanyMatchesFindPredicate predicate = preparePredicate("google"); // lowercase

        FindCompanyCommand command = new FindCompanyCommand(predicate);
        expectedModel.updateFilteredCompanyList(predicate);

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage, UiPaneVisibility.SHOW_COMPANY_LIST);

        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
        assertEquals(Arrays.asList(GOOGLE), model.getFilteredCompanyList());
    }

    @Test
    public void toStringMethod() {
        CompanyMatchesFindPredicate predicate =
                new CompanyMatchesFindPredicate(Arrays.asList("keyword"));
        FindCompanyCommand findCompanyCommand = new FindCompanyCommand(predicate);

        String expected = FindCompanyCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCompanyCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code CompanyMatchesFindPredicate}.
     */
    private CompanyMatchesFindPredicate preparePredicate(String userInput) {
        return new CompanyMatchesFindPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
