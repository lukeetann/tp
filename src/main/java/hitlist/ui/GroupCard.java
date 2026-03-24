package hitlist.ui;

import java.util.Objects;

import hitlist.model.group.Group;
import hitlist.model.person.UniquePersonList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * A UI component that displays information of a {@code Group}.
 */
public class GroupCard extends UiPart<Region> {

    private static final String FXML = "GroupListCard.fxml";
    private final int displayedIndex;
    private final Group group;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label count;
    @FXML
    private FlowPane members;
    @FXML
    private HBox membersWrapper;

    /**
     * Creates a {@code GroupCard} with the given {@code Group} and index to display.
     */
    public GroupCard(Group group, int displayedIndex) {
        super(FXML);
        this.group = group;
        this.displayedIndex = displayedIndex;

        UniquePersonList memberList = group.getMembers();

        Integer memberCount = memberList.asUnmodifiableObservableList().size();

        id.setText(displayedIndex + ". ");
        name.setText(group.getName().toString());
        count.setText("(" + memberCount.toString() + ")");

        memberList.asUnmodifiableObservableList().stream()
            .forEach(member -> {
                Label memberLabel = new Label(member.getName().fullName);

                memberLabel.getStyleClass().add("tag-label");
                members.getChildren().add(memberLabel);
            });

        if (members.getChildren().isEmpty()) {
            membersWrapper.setVisible(false);
            membersWrapper.setManaged(false);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof GroupCard)) {
            return false;
        }

        GroupCard card = (GroupCard) other;
        return displayedIndex == card.displayedIndex
                && group.equals(card.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayedIndex, group);
    }
}
