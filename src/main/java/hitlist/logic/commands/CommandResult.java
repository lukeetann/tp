package hitlist.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import hitlist.commons.util.ToStringBuilder;
import hitlist.ui.UiPaneVisibility;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** The company list pane should be shown to the user. */
    private final boolean showCompanyList;

    /** The group list pane should be shown to the user. */
    private final boolean showGroupList;

    /** The role list pane should be shown to the user. */
    private final boolean showRoleList;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    private CommandResult(String feedbackToUser,
                         boolean showHelp,
                         boolean exit,
                         boolean showCompanyList,
                         boolean showGroupList,
                         boolean showRoleList) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.showCompanyList = showCompanyList;
        this.showGroupList = showGroupList;
        this.showRoleList = showRoleList;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, UiPaneVisibility paneToShow) {
        this(feedbackToUser, paneToShow.isHelp(), paneToShow.isExit(), paneToShow.isCompanyList(),
                paneToShow.isGroupList(), paneToShow.isRoleList());
    }


    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, false, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isShowCompanyList() {
        return showCompanyList;
    }

    public boolean isShowGroupList() {
        return showGroupList;
    }

    public boolean isShowRoleList() {
        return showRoleList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && showCompanyList == otherCommandResult.showCompanyList
                && showGroupList == otherCommandResult.showGroupList
                && showRoleList == otherCommandResult.showRoleList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, showCompanyList, showGroupList, showRoleList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("showCompanyList", showCompanyList)
                .add("showGroupList", showGroupList)
                .add("showRoleList", showRoleList)
                .toString();
    }

}
