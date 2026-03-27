package hitlist.ui;

/**
 * Enum representing the visibility of different UI panes in the application.
 */
public enum UiPaneVisibility {
    EXIT, SHOW_HELP_MESSAGE, SHOW_COMPANY_LIST, SHOW_GROUP_LIST, SHOW_ROLE_LIST;

    // Helper methods to check which pane should be shown
    public boolean isExit() {
        return this == EXIT;
    }

    public boolean isHelp() {
        return this == SHOW_HELP_MESSAGE;
    }

    public boolean isCompanyList() {
        return this == SHOW_COMPANY_LIST;
    }

    public boolean isGroupList() {
        return this == SHOW_GROUP_LIST;
    }

    public boolean isRoleList() {
        return this == SHOW_ROLE_LIST;
    }
}
