package hitlist.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hitlist.model.company.Company;
import hitlist.testutil.CompanyBuilder;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;

/**
 * Unit tests for {@link CompanyCard}.
 */
public class CompanyCardTest {

    @BeforeAll
    public static void initToolkit() {
        // Initializes JavaFX runtime for tests.
        new JFXPanel();
    }

    @Test
    public void constructor_validCompany_displaysCorrectDetails() throws Exception {
        Company company = new CompanyBuilder()
                .withName("Google Inc.")
                .withDescription("A multinational technology company")
                .build();

        AtomicReference<CompanyCard> cardRef = new AtomicReference<>();
        AtomicReference<String> idTextRef = new AtomicReference<>();
        AtomicReference<String> nameTextRef = new AtomicReference<>();
        AtomicReference<String> descriptionTextRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            CompanyCard card = new CompanyCard(company, 1);
            cardRef.set(card);

            Label idLabel = (Label) card.getRoot().lookup("#id");
            Label nameLabel = (Label) card.getRoot().lookup("#name");
            Label descriptionLabel = (Label) card.getRoot().lookup("#description");

            idTextRef.set(idLabel == null ? null : idLabel.getText());
            nameTextRef.set(nameLabel == null ? null : nameLabel.getText());
            descriptionTextRef.set(descriptionLabel == null ? null : descriptionLabel.getText());

            latch.countDown();
        });

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertEquals(true, completed);
        assertNotNull(cardRef.get());
        assertEquals("1. ", idTextRef.get());
        assertEquals("Google Inc.", nameTextRef.get());
        assertEquals("A multinational technology company", descriptionTextRef.get());
    }

    @Test
    public void constructor_validCompanyDifferentIndex_displaysCorrectIndex() throws Exception {
        Company company = new CompanyBuilder()
                .withName("Meta Platforms, Inc.")
                .withDescription("A technology conglomerate")
                .build();

        AtomicReference<String> idTextRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            CompanyCard card = new CompanyCard(company, 7);
            Label idLabel = (Label) card.getRoot().lookup("#id");
            idTextRef.set(idLabel == null ? null : idLabel.getText());
            latch.countDown();
        });

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertEquals(true, completed);
        assertEquals("7. ", idTextRef.get());
    }
}
