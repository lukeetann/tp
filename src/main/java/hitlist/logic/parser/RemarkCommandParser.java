package hitlist.logic.parser;

import hitlist.commons.core.index.Index;
import hitlist.commons.exceptions.IllegalValueException;
import hitlist.logic.commands.RemarkCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.person.Remark;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_REMARK;
import static java.util.Objects.requireNonNull;

public class RemarkCommandParser implements Parser {
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_REMARK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemarkCommand.MESSAGE_USAGE), ive);
        }

        Remark remark = new Remark(argMultimap.getValue(PREFIX_REMARK).orElse(""));

        return new RemarkCommand(index, remark);
    }
}
