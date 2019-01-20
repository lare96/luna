package io.luna.game.event.item;

import io.luna.game.event.entity.player.PlayerEvent;
import io.luna.game.model.mob.Player;

/**
 * An event sent when a player picks up an item.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class PickupItemEvent extends PlayerEvent {

    /**
     * The item's x coordinate.
     */
    private final int x;

    /**
     * The item's y coordinate.
     */
    private final int y;

    /**
     * The item identifier.
     */
    private final int id;

    /**
     * Creates a new {@link PickupItemEvent}.
     *
     * @param x The item's x coordinate.
     * @param y The item's y coordinate.
     * @param id The item identifier.
     */
    public PickupItemEvent(Player player, int x, int y, int id) {
        super(player);
        this.x = x;
        this.y = y;
        this.id = id;
    }

    /**
     * @return The item's x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * @return The item's y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * @return The item identifier.
     */
    public int getId() {
        return id;
    }
}
