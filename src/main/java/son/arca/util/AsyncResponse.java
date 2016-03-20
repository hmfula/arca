package son.arca.util;

import java.util.concurrent.*;

/**
 * Created by harry on 21.2.2016.
 */
public class AsyncResponse<V> implements Future<V> {
    private V value;
    private Exception executionException;
    private boolean isCompletedExceptionally = false;
    private boolean isCancelled = false;
    private boolean isDone = false;
    private long checkCompletedInterval = 100;

    public AsyncResponse() {

    }

    public AsyncResponse(V v) {
        this.value = v;
        this.isDone = true;
    }

    public AsyncResponse(Throwable throwable) {
        this.executionException = new ExecutionException(throwable);
        this.isCompletedExceptionally = true;
        this.isDone = true;
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        this.isCancelled = true;
        this.isDone = true;
        return false;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public boolean isDone() {
        return this.isDone;
    }

    public boolean isCompletedExceptionally() {
        return this.isCompletedExceptionally;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {

        block(0);

        if (isCancelled()) {
            throw new CancellationException();
        }
        if (isCompletedExceptionally()) {
            throw new ExecutionException(this.executionException);
        }
        if (isDone()) {
            return this.value;
        }
        throw new InterruptedException();
    }

    private void block(long timeout) throws InterruptedException {
        long start = System.currentTimeMillis();
        //Block until done, cancelled or the timeout is exceeded
        while (!isDone() && !isCancelled()) {
            if (timeout > 0) {
                long now = System.currentTimeMillis();
                if (now > start - timeout) {
                    break;
                }
            }
            Thread.sleep(checkCompletedInterval);
        }
    }

    public boolean completedExceptionally(Throwable th) {
        this.value = null;
        this.executionException = new ExecutionException(th);
        this.isCompletedExceptionally = true;
        this.isDone = true;

        return true;
    }

    public boolean completed(V value) {
        this.value = value;
        this.isDone = true;

        return true;
    }

    public void setCheckCompletedInterval(long millis) {
        this.checkCompletedInterval = millis;

    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {

        long timeInMillis = unit.toMillis(timeout);
        block(timeInMillis);

        if (isCancelled()) {
            throw new CancellationException();
        }
        if (isCompletedExceptionally()) {
            throw new ExecutionException(this.executionException);
        }
        if (isDone()) {
            return this.value;
        }
        throw new InterruptedException();

    }
}
