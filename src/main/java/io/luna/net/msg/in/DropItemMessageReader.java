package io.luna.net.msg.in;

import io.luna.game.event.Event;
import io.luna.game.event.item.DropItemEvent;
import io.luna.game.model.def.ItemDefinition;
import io.luna.game.model.item.GroundItem;
import io.luna.game.model.mob.Player;
import io.luna.game.model.mob.dialogue.DestroyItemDialogueInterface;
import io.luna.net.codec.ValueType;
import io.luna.net.msg.GameMessage;
import io.luna.net.msg.GameMessageReader;

/**
 * A {@link GameMessageReader} implementation that intercepts data for when an item is dropped.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class DropItemMessageReader extends GameMessageReader {

    @Override
    public Event read(Player player, GameMessage msg) {
        var world = player.getWorld();
        int itemId = msg.getPayload().getShort(false, ValueType.ADD);
        int widgetId = msg.getPayload().getShort(false);
        int index = msg.getPayload().getShort(false, ValueType.ADD);
        var isTradeable = ItemDefinition.ALL.retrieve(itemId).isTradeable();

        // Make sure item exists in inventory.
        var inventoryItem = player.getInventory().get(index);
        
        if (inventoryItem == null || inventoryItem.getId() != itemId) {
            return null;
        }

        if (!isTradeable) {
            // Open destroy interface.
            player.getInterfaces().open(new DestroyItemDialogueInterface(index, itemId));
        } else {
            // Drop item.
            GroundItem dropItem = new GroundItem(player.getContext(), itemId, inventoryItem.getAmount(),
                    player.getPosition(), player);
            world.getItems().add(dropItem);
            player.getInventory().set(index, null);
        }
        
        return new DropItemEvent(player, itemId, widgetId, index);
    }
}