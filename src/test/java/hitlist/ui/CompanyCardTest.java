package hitlist.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hitlist.model.company.Company;
import hitlist.testutil.CompanyBuilder;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class CompanyCardTest {

    @BeforeAll
    public static void setupFxGuard() {
        assumeTrue(JavaFxTestSupport.isFxAvailable(),
                "Skipping CompanyCardTest: JavaFX unavailable in this environment.");
    }

    @Test
    public void constructor_validCompany_displaysCorrectDetails() throws Exception {
        Company company = new CompanyBuilder()
                .withName("Google Inc.")
                .withDescription("A multinational technology company")
                .build();

        AtomicReference<CompanyCard> cardRef = new AtomicReference<>();
        AtomicReference<String> idText = new AtomicReference<>();
        AtomicReference<String> nameText = new AtomicReference<>();
        AtomicReference<String> descText = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            CompanyCard card = new CompanyCard(company, 1);
            cardRef.set(card);

            Label id = (Label) card.getRoot().lookup("#id");
            Label name = (Label) card.getRoot().lookup("#name");
            Label description = (Label) card.getRoot().lookup("#description");

            idText.set(id == null ? null : id.getText());
            nameText.set(name == null ? null : name.getText());
            descText.set(description == null ? null : description.getText());
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertNotNull(cardRef.get());
        assertEquals("1. ", idText.get());
        assertEquals("Google Inc.", nameText.get());
        assertEquals("A multinational technology company", descText.get());
    }

    @Test
    public void constructor_differentIndex_displaysCorrectIndex() throws Exception {
        Company company = new CompanyBuilder()
                .withName("Meta Platforms, Inc.")
                .withDescription("A technology conglomerate")
                .build();

        AtomicReference<String> idText = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            CompanyCard card = new CompanyCard(company, 7);
            Label id = (Label) card.getRoot().lookup("#id");
            idText.set(id == null ? null : id.getText());
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertEquals("7. ", idText.get());
    }
}
