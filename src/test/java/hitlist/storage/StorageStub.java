package hitlist.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import hitlist.commons.exceptions.DataLoadingException;
import hitlist.model.ReadOnlyHitList;
import hitlist.model.ReadOnlyUserPrefs;
import hitlist.model.UserPrefs;

/**
 * A default storage stub that have all methods failing.
 */
public abstract class StorageStub implements Storage {

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getHitListFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<ReadOnlyHitList> readHitList() throws DataLoadingException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void saveHitList(ReadOnlyHitList hitList) throws IOException {
        throw new AssertionError("This method should not be called.");
    }

}
