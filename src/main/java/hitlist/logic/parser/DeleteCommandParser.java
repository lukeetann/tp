package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Optional;

import hitlist.commons.core.index.Index;
import hitlist.logic.commands.DeleteCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.person.Name;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);
        String preamble = argMultimap.getPreamble().trim();
        Optional<String> nameValue = argMultimap.getValue(PREFIX_NAME);

        try {
            if (!preamble.isEmpty()) {
                Index index = ParserUtil.parseIndex(preamble);
                return new DeleteCommand(index);
            } else {
                // Preamble is not a valid index, treat as name
                Name name = ParserUtil.parseName(preamble);
                return new DeleteCommand(name);
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}
