package hitlist.ui;

import java.util.logging.Logger;

import hitlist.commons.core.LogsCenter;
import hitlist.model.group.Group;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of groups.
 */
public class GroupListPanel extends UiPart<Region> {
    private static final String FXML = "GroupListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(GroupListPanel.class);
    private final ObservableList<Group> groupList;

    @FXML
    private ListView<Group> groupListView;

    /**
     * Creates a {@code GroupListPanel} with the given {@code ObservableList}.
     */
    public GroupListPanel(ObservableList<Group> groupList) {
        super(FXML);
        this.groupList = groupList;
        setupListView();
    }

    /**
     * Sets up list bindings after FXML has been loaded.
     */
    private void setupListView() {
        if (groupListView == null) {
            // If this ever happens, FXML failed to inject fx:id="groupListView".
            throw new IllegalStateException("groupListView is not injected. Check GroupListPanel.fxml fx:id.");
        }
        groupListView.setItems(groupList);
        groupListView.setCellFactory(listView -> new GroupListViewCell());
    }

    class GroupListViewCell extends ListCell<Group> {
        @Override
        protected void updateItem(Group group, boolean empty) {
            super.updateItem(group, empty);
            if (empty || group == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new GroupCard(group, getIndex() + 1).getRoot());
            }
        }
    }
}
