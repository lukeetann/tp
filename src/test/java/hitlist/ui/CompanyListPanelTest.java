package hitlist.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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

/**
 * Focused tests for CompanyListPanel.CompanyListViewCell#updateItem.
 */
public class CompanyListPanelTest {

    @BeforeAll
    public static void setupFxGuard() {
        assumeTrue(JavaFxTestSupport.isFxAvailable(),
                "Skipping CompanyListPanelTest: JavaFX unavailable in this environment.");
    }

    @Test
    public void companyListViewCell_updateItem_emptyOrNull_clearsGraphicAndText() throws Exception {
        ObservableList<Company> companies = FXCollections.observableArrayList();
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                CompanyListPanel panel = new CompanyListPanel(companies);
                CompanyListPanel.CompanyListViewCell cell = panel.new CompanyListViewCell();

                // Covers: if (empty || company == null) -> true (empty path)
                cell.updateItem(null, true);
                assertNull(cell.getGraphic());
                assertNull(cell.getText());

                // Covers: if (empty || company == null) -> true (null company path)
                cell.updateItem(null, false);
                assertNull(cell.getGraphic());
                assertNull(cell.getText());
            } catch (Throwable t) {
                thrown.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        if (thrown.get() != null) {
            throw new AssertionError(thrown.get());
        }
    }

    @Test
    public void companyListViewCell_updateItem_nonEmpty_setsGraphic() throws Exception {
        Company company = new CompanyBuilder()
                .withName("Google Inc.")
                .withDescription("A multinational technology company")
                .build();

        ObservableList<Company> companies = FXCollections.observableArrayList(company);
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                CompanyListPanel panel = new CompanyListPanel(companies);
                CompanyListPanel.CompanyListViewCell cell = panel.new CompanyListViewCell();

                // Covers: if (empty || company == null) -> false (else path)
                cell.updateItem(company, false);
                assertNotNull(cell.getGraphic());
            } catch (Throwable t) {
                thrown.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        if (thrown.get() != null) {
            throw new AssertionError(thrown.get());
        }
    }
}
