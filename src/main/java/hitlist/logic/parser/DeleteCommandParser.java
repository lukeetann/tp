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
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        String preamble = argMultimap.getPreamble().trim();
        Optional<String> nameValue = argMultimap.getValue(PREFIX_NAME);

        try {
            if (nameValue.isPresent() && preamble.isEmpty()) {
                Name name = ParserUtil.parseName(nameValue.get());
                return new DeleteCommand(name);
            }

            if (nameValue.isEmpty() && !preamble.isEmpty()) {
                Index index = ParserUtil.parseIndex(preamble);
                return new DeleteCommand(index);
            }

            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }
}
