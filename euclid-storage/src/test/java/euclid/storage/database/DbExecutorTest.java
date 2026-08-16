package euclid.storage.database;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DbExecutorTest {

    @Test
    void executeRunsTask() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        DbExecutor.execute(latch::countDown);
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }
}
