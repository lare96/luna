package io.luna.game.event.item;

import io.luna.game.event.entity.player.PlayerEvent;
import io.luna.game.model.mob.Player;

/**
 * An event sent when a Player drops an item.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class DropItemEvent extends PlayerEvent {

    /**
     * The item identifier.
     */
    private final int itemId;

    /**
     * The widget identifier.
     */
    private final int widgetId;

    /**
     * The index of the item.
     */
    private final int index;

    /**
     * Creates a new {@link DropItemEvent}.
     *
     * @param player The player.
     * @param itemId The item identifier.
     * @param widgetId The widget identifier.
     * @param index The index of the item.
     */
    public DropItemEvent(Player player, int itemId, int widgetId, int index) {
        super(player);
        this.itemId = itemId;
        this.widgetId = widgetId;
        this.index = index;
    }

    @Override
    public boolean terminate() {
        throw new IllegalStateException("This event type (DropItemEvent) cannot be terminated.");
    }

    /**
     * @return The item identifier.
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * @return The widget identifier.
     */
    public int getWidgetId() {
        return widgetId;
    }

    /**
     * @return The index of the item.
     */
    public int getIndex() {
        return index;
    }
}