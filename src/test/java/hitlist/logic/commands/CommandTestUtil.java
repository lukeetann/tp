package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY_DESC;
import static hitlist.logic.parser.CliSyntax.PREFIX_EMAIL;
import static hitlist.logic.parser.CliSyntax.PREFIX_GROUP;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static hitlist.logic.parser.CliSyntax.PREFIX_PHONE;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE;
import static hitlist.logic.parser.CliSyntax.PREFIX_ROLE_DESC;
import static hitlist.logic.parser.CliSyntax.PREFIX_TAG;
import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hitlist.commons.core.index.Index;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.HitList;
import hitlist.model.Model;
import hitlist.model.person.NameContainsKeywordsPredicate;
import hitlist.model.person.Person;
import hitlist.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {
    public static final String VALID_GROUP_NAME_STUDENTS = "Students";
    public static final String VALID_GROUP_NAME_UNEMPLOYED = "Unemployed";

    public static final String GROUP_NAME_DESC_STUDENTS = " " + PREFIX_GROUP + VALID_GROUP_NAME_STUDENTS;
    public static final String GROUP_NAME_DESC_UNEMPLOYED = " " + PREFIX_GROUP + VALID_GROUP_NAME_UNEMPLOYED;

    public static final String INVALID_GROUP_NAME_DESC = " " + PREFIX_GROUP + "This&That"; // '&' not allowed in names

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String VALID_COMPANY_NAME_GOOGLE = "Google Inc.";
    public static final String VALID_COMPANY_NAME_META = "Meta Platforms, Inc.";
    public static final String VALID_COMPANY_DESCRIPTION_GOOGLE =
            "A multinational technology company that specializes in Internet-related services and products";
    public static final String VALID_COMPANY_DESCRIPTION_META =
            "An American multinational technology conglomerate based in Menlo Park, California. "
                    + "It is the parent company of Facebook, Instagram, and WhatsApp, among other subsidiaries";

    public static final String INVALID_COMPANY_NAME =
            " A/B Testing Ltd"; // ' ' not allowed as start character and '/' not allowed in company names
    public static final String INVALID_COMPANY_DESCRIPTION =
            "We specialize in B2B/B2C marketing."; // '/' not allowed in company descriptions

    public static final String COMPANY_NAME_DESC_GOOGLE = " " + PREFIX_COMPANY + VALID_COMPANY_NAME_GOOGLE;
    public static final String COMPANY_NAME_DESC_META = " " + PREFIX_COMPANY + VALID_COMPANY_NAME_META;
    public static final String COMPANY_DESC_GOOGLE = " " + PREFIX_COMPANY_DESC + VALID_COMPANY_DESCRIPTION_GOOGLE;
    public static final String COMPANY_DESC_META = " " + PREFIX_COMPANY_DESC + VALID_COMPANY_DESCRIPTION_META;
    public static final String INVALID_COMPANY_NAME_DESC = " " + PREFIX_COMPANY + INVALID_COMPANY_NAME;
    public static final String INVALID_COMPANY_DESC = " " + PREFIX_COMPANY_DESC + INVALID_COMPANY_DESCRIPTION;

    public static final String VALID_ROLE_NAME_PRODUCT_MANAGER = "Product Manager";
    public static final String VALID_ROLE_NAME_SOFTWARE_ENGINEER = "Software Engineer";
    public static final String VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER =
            "Responsible for overseeing the development and delivery of a product, "
                    + "ensuring it meets customer needs and business goals.";
    public static final String VALID_ROLE_DESCRIPTION_SOFTWARE_ENGINEER =
            "Responsible for designing, developing, and maintaining software applications or systems, "
                    + "ensuring they meet functional and technical requirements.";

    public static final String INVALID_ROLE_NAME =
            "A/B Testing"; // '/' not allowed in role names
    public static final String INVALID_ROLE_DESCRIPTION =
            "We specialize in B2B/B2C marketing."; // '/' not allowed in role descriptions

    public static final String ROLE_NAME_DESC_PRODUCT_MANAGER = " " + PREFIX_ROLE
            + VALID_ROLE_NAME_PRODUCT_MANAGER;
    public static final String ROLE_NAME_DESC_SOFTWARE_ENGINEER = " " + PREFIX_ROLE
            + VALID_ROLE_NAME_SOFTWARE_ENGINEER;
    public static final String ROLE_DESC_PRODUCT_MANAGER = " " + PREFIX_ROLE_DESC
            + VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER;
    public static final String ROLE_DESC_SOFTWARE_ENGINEER = " " + PREFIX_ROLE_DESC
            + VALID_ROLE_DESCRIPTION_SOFTWARE_ENGINEER;
    public static final String INVALID_ROLE_NAME_DESC = " " + PREFIX_ROLE + INVALID_ROLE_NAME;
    public static final String INVALID_ROLE_DESC = " " + PREFIX_ROLE_DESC + INVALID_ROLE_DESCRIPTION;

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the HitList, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        HitList expectedHitList = new HitList(actualModel.getHitList());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedHitList, actualModel.getHitList());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s HitList.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the company at the given {@code targetIndex} in the
     * {@code model}'s HitList.
     */
    public static void showCompanyAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredCompanyList().size());

        assertEquals(1, model.getFilteredCompanyList().size());
    }
}
