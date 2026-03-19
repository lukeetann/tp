package hitlist.logic.commands;

import static java.util.Objects.requireNonNull;

import hitlist.model.HitList;
import hitlist.model.Model;

/**
 * Clears the HitList.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "HitList has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setHitList(new HitList());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
