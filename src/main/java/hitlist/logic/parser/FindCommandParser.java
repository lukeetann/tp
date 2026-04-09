package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import hitlist.logic.commands.FindCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.person.Name;
import hitlist.model.person.PersonMatchesFindPredicate;

/**
 * Parses input arguments and creates a new {@code FindCommand} object.
 */
public class FindCommandParser implements Parser<FindCommand> {

    @Override
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmedArgs.split("\\s+");
        List<String> nameKeywords = new ArrayList<>();

        for (String token : tokens) {
            // Validate each keyword follows the same rules as a Name
            if (!Name.isValidName(token)) {
                throw new ParseException(
                        String.format(FindCommand.MESSAGE_INVALID_KEYWORD, token, Name.MESSAGE_CONSTRAINTS));
            }
            nameKeywords.add(token);
        }

        if (nameKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new PersonMatchesFindPredicate(nameKeywords));
    }
}
