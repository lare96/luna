package io.luna.game.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A model representing a cyclic unit of work carried out strictly on the game thread.
 *
 * @author lare96 <http://github.org/lare96>
 */
public abstract class Task {

    /**
     * The asynchronous logger.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * If execution happens instantly.
     */
    private final boolean instant;

    /**
     * The cyclic delay.
     */
    private int delay;

    /**
     * The state.
     */
    private TaskState state = TaskState.IDLE;

    /**
     * The execution counter. This task is ran when it reaches the delay.
     */
    private int executionCounter;

    /**
     * The attachment.
     */
    private Object key;

    /**
     * Creates a new {@link Task}.
     *
     * @param instant If execution happens instantly.
     * @param delay The cyclic delay.
     */
    public Task(boolean instant, int delay) {
        checkArgument(delay > 0);
        
        this.instant = instant;
        this.delay = delay;
    }

    /**
     * Creates a new {@link Task} that doesn't execute instantly.
     *
     * @param delay The cyclic delay.
     */
    public Task(int delay) {
        this(false, delay);
    }

    /**
     * A function representing the unit of work that will be carried out.
     */
    protected abstract void execute();

    /**
     * Increments the timer and determines if this task is ready to be ran.
     *
     * @return {@code true} if this task is ready to be ran.
     */
    final boolean isReady() {
        if (++executionCounter >= delay && state == TaskState.RUNNING) {
            executionCounter = 0;
            return true;
        }
        
        return false;
    }

    /**
     * Runs this task once. Forwards any errors to {@link #onException(Exception)}.
     */
    final void runTask() {
        try {
            execute();
        } catch (Exception e) {
            onException(e);
        }
    }

    /**
     * Cancels all subsequent executions, and fires {@link #onCancel()}. Does nothing if already
     * cancelled.
     */
    public final void cancel() {
        if (state != TaskState.CANCELLED) {
            onCancel();
            state = TaskState.CANCELLED;
        }
    }

    /**
     * A function executed when this task is scheduled.
     *
     * @return {@code true} if this task should still be scheduled.
     */
    protected boolean onSchedule() {
        return true;
    }

    /**
     * A function executed when this task is cancelled.
     */
    protected void onCancel() {

    }

    /**
     * A function executed on thrown exceptions. The default behaviour is to log the exception.
     *
     * @param failure The exception that was thrown.
     */
    protected void onException(Exception failure) {
        LOGGER.error(failure);
    }

    /**
     * Attaches a new key.
     *
     * @param newKey The key to attach.
     * @return This task instance, for chaining.
     */
    public Task attach(Object newKey) {
        checkState(key == null, "Task already has an attachment.");
        key = newKey;
        return this;
    }

    /**
     * @return {@code true} if execution happens instantly.
     */
    public boolean isInstant() {
        return instant;
    }

    /**
     * @return The cyclic delay.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Sets the cyclic delay.
     *
     * @param delay The new value to set.
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * @return The state.
     */
    public TaskState getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state The new value to set.
     */
    void setState(TaskState state) {
        this.state = state;
    }

    /**
     * @return The attachment.
     */
    public Object getAttachment() {
        return key;
    }
}
