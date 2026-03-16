package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import hitlist.logic.commands.FindCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.person.PersonMatchesFindPredicate;
import hitlist.model.tag.Tag;

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
        List<String> tagKeywords = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (token.equals(PREFIX_TAG.getPrefix())) {
                if (i + 1 >= tokens.length || !Tag.isValidTagName(tokens[i + 1])) {
                    throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
                }
                tagKeywords.add(tokens[++i]);
            } else if (token.startsWith(PREFIX_TAG.getPrefix())) {
                String tagName = token.substring(PREFIX_TAG.getPrefix().length());
                if (tagName.isEmpty() || !Tag.isValidTagName(tagName)) {
                    throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
                }
                tagKeywords.add(tagName);
            } else {
                nameKeywords.add(token);
            }
        }

        if (nameKeywords.isEmpty() && tagKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new PersonMatchesFindPredicate(nameKeywords, tagKeywords));
    }
}
