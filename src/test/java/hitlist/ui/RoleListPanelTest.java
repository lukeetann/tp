package hitlist.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hitlist.model.company.role.Role;
import hitlist.model.company.role.RoleDescription;
import hitlist.model.company.role.RoleName;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * Unit tests for {@link RoleListPanel}.
 */
public class RoleListPanelTest {

    @BeforeAll
    public static void setupFxGuard() {
        assumeTrue(JavaFxTestSupport.isFxAvailable(),
                "Skipping RoleListPanelTest: JavaFX unavailable in this environment.");
    }

    @Test
    public void constructor_validRoleList_setsItemsCorrectly() throws Exception {
        ObservableList<Role> roleList = FXCollections.observableArrayList();
        roleList.add(new Role(new RoleName("Manager"), new RoleDescription("Manages the team")));
        roleList.add(new Role(new RoleName("Developer"), new RoleDescription("Develops software")));

        AtomicReference<RoleListPanel> panelRef = new AtomicReference<>();
        AtomicReference<Integer> listSizeRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            RoleListPanel panel = new RoleListPanel(roleList);
            panelRef.set(panel);

            ListView<Role> listView =
                    (ListView<Role>) panel.getRoot().lookup("#roleListView");

            listSizeRef.set(listView == null ? null : listView.getItems().size());
            latch.countDown();
        });

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertTrue(completed);
        assertNotNull(panelRef.get());
        assertEquals(2, listSizeRef.get());
    }

    @Test
    public void roleListViewCell_emptyItem_setsNullGraphicAndText() throws Exception {
        AtomicReference<Object> graphicRef = new AtomicReference<>();
        AtomicReference<String> textRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            RoleListPanel panel = new RoleListPanel(FXCollections.observableArrayList());
            RoleListPanel.RoleListViewCell cell = panel.new RoleListViewCell();

            cell.updateItem(null, true);

            graphicRef.set(cell.getGraphic());
            textRef.set(cell.getText());
            latch.countDown();
        });

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertEquals(true, completed);
        assertNull(graphicRef.get());
        assertNull(textRef.get());
    }

    @Test
    public void constructor_emptyList_bindsEmptyItemsToListView() throws Exception {
        ObservableList<Role> roles = FXCollections.observableArrayList();

        AtomicReference<Integer> sizeRef = new AtomicReference<>(-1);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            RoleListPanel panel = new RoleListPanel(roles);

            @SuppressWarnings("unchecked")
            ListView<Role> listView =
                    (ListView<Role>) panel.getRoot().lookup("#roleListView");

            if (listView != null) {
                sizeRef.set(listView.getItems().size());
            }
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(0, sizeRef.get());
    }
}
