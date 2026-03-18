package hitlist.ui;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;

/**
 * Shared JavaFX bootstrap helper for UI tests.
 * Tests can skip gracefully when JavaFX is unavailable in CI/headless environments.
 */
public final class JavaFxTestSupport {

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
    private static volatile boolean fxAvailable = true;

    private JavaFxTestSupport() {}

    /**
     * Returns true if JavaFX is available for UI tests.
     */
    public static boolean isFxAvailable() {
        if (!INITIALIZED.get()) {
            init();
        }
        return fxAvailable;
    }

    private static void init() {
        if (!INITIALIZED.compareAndSet(false, true)) {
            return;
        }

        try {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            boolean started = latch.await(5, TimeUnit.SECONDS);
            if (!started) {
                fxAvailable = false;
            }
        } catch (UnsupportedOperationException | NoClassDefFoundError e) {
            fxAvailable = false;
        } catch (IllegalStateException e) {
            // Toolkit already initialized by another test class.
            fxAvailable = true;
        } catch (Exception e) {
            fxAvailable = false;
        }
    }
}
