package hitlist.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hitlist.model.company.Company;
import hitlist.testutil.CompanyBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ListView;

/**
 * Unit tests for {@link CompanyListPanel}.
 */
public class CompanyListPanelTest {

    @BeforeAll
    public static void initJfxRuntime() {
        // Bootstraps JavaFX toolkit in test environment.
        new JFXPanel();
    }

    @Test
    public void constructor_nonEmptyList_bindsItemsToListView() throws Exception {
        ObservableList<Company> companies = FXCollections.observableArrayList(
                new CompanyBuilder().withName("Google Inc.").withDescription("Tech company").build(),
                new CompanyBuilder().withName("Meta Platforms, Inc.").withDescription("Social company").build()
        );

        AtomicReference<CompanyListPanel> panelRef = new AtomicReference<>();
        AtomicReference<Integer> sizeRef = new AtomicReference<>(-1);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            CompanyListPanel panel = new CompanyListPanel(companies);
            panelRef.set(panel);

            @SuppressWarnings("unchecked")
            ListView<Company> listView = (ListView<Company>) panel.getRoot().lookup("#companyListView");
            if (listView != null) {
                sizeRef.set(listView.getItems().size());
            }
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(panelRef.get());
        assertEquals(2, sizeRef.get());
    }

    @Test
    public void constructor_emptyList_bindsEmptyItemsToListView() throws Exception {
        ObservableList<Company> companies = FXCollections.observableArrayList();

        AtomicReference<Integer> sizeRef = new AtomicReference<>(-1);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            CompanyListPanel panel = new CompanyListPanel(companies);

            @SuppressWarnings("unchecked")
            ListView<Company> listView = (ListView<Company>) panel.getRoot().lookup("#companyListView");
            if (listView != null) {
                sizeRef.set(listView.getItems().size());
            }
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(0, sizeRef.get());
    }

    @Test
    public void constructor_nullList_doesNotThrow() throws Exception {
        AtomicReference<CompanyListPanel> panelRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            panelRef.set(new CompanyListPanel(null));
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(panelRef.get());
    }
}
