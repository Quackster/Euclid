package euclid.storage.database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class DbExecutor {

    private static final int CORE_POOL_SIZE = 4;
    private static final ExecutorService executor = Executors.newFixedThreadPool(CORE_POOL_SIZE, r -> {
        Thread t = new Thread(r, "db-executor");
        t.setDaemon(true);
        return t;
    });

    private DbExecutor() {
    }

    public static void execute(Runnable task) {
        executor.execute(task);
    }

    public static void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
