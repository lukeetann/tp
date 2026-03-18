package hitlist.ui;

import java.util.logging.Logger;

import hitlist.commons.core.LogsCenter;
import hitlist.model.company.Company;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of companies.
 */
public class CompanyListPanel extends UiPart<Region> {
    private static final String FXML = "CompanyListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(CompanyListPanel.class);
    private final ObservableList<Company> companyList;

    @FXML
    private ListView<Company> companyListView;

    /**
     * Creates a {@code CompanyListPanel} with the given {@code ObservableList}.
     */
    public CompanyListPanel(ObservableList<Company> companyList) {
        super(FXML);
        this.companyList = companyList;
        setupListView();
    }

    /**
     * Sets up list bindings after FXML has been loaded.
     */
    private void setupListView() {
        if (companyListView == null) {
            // If this ever happens, FXML failed to inject fx:id="companyListView".
            throw new IllegalStateException("companyListView is not injected. Check CompanyListPanel.fxml fx:id.");
        }
        companyListView.setItems(companyList);
        companyListView.setCellFactory(listView -> new CompanyListViewCell());
    }

    class CompanyListViewCell extends ListCell<Company> {
        @Override
        protected void updateItem(Company company, boolean empty) {
            super.updateItem(company, empty);
            if (empty || company == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new CompanyCard(company, getIndex() + 1).getRoot());
            }
        }
    }
}
