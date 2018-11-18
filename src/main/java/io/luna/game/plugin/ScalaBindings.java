package io.luna.game.plugin;

import io.luna.LunaContext;
import io.luna.game.event.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * A model representing values that will be reflectively injected into Scala scripts.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ScalaBindings {

    /**
     * The context instance.
     */
    private final LunaContext ctx;

    /**
     * The asynchronous logger.
     */
    private final Logger logger = LogManager.getLogger();

    /**
     * A list of event listeners.
     */
    private final List<EventListener<?>> listeners = new ArrayList<>();

    /**
     * Creates a new {@link ScalaBindings}.
     *
     * @param ctx The context instance.
     */
    ScalaBindings(LunaContext ctx) {
        this.ctx = ctx;
    }

    /**
     * @return The context instance.
     */
    public LunaContext getCtx() {
        return ctx;
    }

    /**
     * @return The asynchronous logger.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * @return A list of event listeners.
     */
    public List<EventListener<?>> getListeners() {
        return listeners;
    }
}