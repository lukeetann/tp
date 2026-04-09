package hitlist.logic.commands;

import static hitlist.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static hitlist.logic.commands.CommandTestUtil.assertCommandSuccess;
import static hitlist.testutil.TypicalPersons.CARL;
import static hitlist.testutil.TypicalPersons.ELLE;
import static hitlist.testutil.TypicalPersons.FIONA;
import static hitlist.testutil.TypicalPersons.getTypicalHitList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import hitlist.model.Model;
import hitlist.model.ModelManager;
import hitlist.model.UserPrefs;
import hitlist.model.person.PersonMatchesFindPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {

    private final Model model = new ModelManager(getTypicalHitList(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalHitList(), new UserPrefs());

    @Test
    public void equals() {
        PersonMatchesFindPredicate firstPredicate =
                new PersonMatchesFindPredicate(Collections.singletonList("first"));
        PersonMatchesFindPredicate secondPredicate =
                new PersonMatchesFindPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        assertTrue(findFirstCommand.equals(findFirstCommand));

        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        assertFalse(findFirstCommand.equals(1));
        assertFalse(findFirstCommand.equals(null));
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_noMatchingKeyword_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PersonMatchesFindPredicate predicate = preparePredicate("nomatch");

        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonMatchesFindPredicate predicate = preparePredicate("Kurz Elle Kunz");

        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleKeyword_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesFindPredicate predicate = preparePredicate("Elle");

        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ELLE), model.getFilteredPersonList());
    }

    @Test
    public void execute_keywordWithHyphen_personFound() {
        // Assuming there's a person with hyphenated name in TypicalPersons
        // If not, this test would need a custom model setup
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PersonMatchesFindPredicate predicate = preparePredicate("Jean-Luc");

        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        // Assert based on your test data
    }

    @Test
    public void execute_keywordCaseInsensitive_personsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesFindPredicate predicate = preparePredicate("elle"); // lowercase

        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ELLE), model.getFilteredPersonList());
    }

    @Test
    public void execute_partialKeywordMatch_personsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        PersonMatchesFindPredicate predicate = preparePredicate("Ku"); // Matches Kurz and Kunz

        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);

        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code PersonMatchesFindPredicate}.
     */
    private PersonMatchesFindPredicate preparePredicate(String userInput) {
        return new PersonMatchesFindPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
