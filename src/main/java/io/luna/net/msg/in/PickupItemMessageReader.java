package io.luna.net.msg.in;

import io.luna.game.action.InteractionAction;
import io.luna.game.event.Event;
import io.luna.game.event.item.PickupItemEvent;
import io.luna.game.model.EntityType;
import io.luna.game.model.Position;
import io.luna.game.model.def.ItemDefinition;
import io.luna.game.model.item.GroundItem;
import io.luna.game.model.mob.Player;
import io.luna.net.codec.ByteOrder;
import io.luna.net.msg.GameMessage;
import io.luna.net.msg.GameMessageReader;

import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;

/**
 * A {@link GameMessageReader} implementation that intercepts data when a ground item is clicked on.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class PickupItemMessageReader extends GameMessageReader {

    @Override
    public Event read(Player player, GameMessage msg) {
        int y = msg.getPayload().getShort(false, ByteOrder.LITTLE);
        int id = msg.getPayload().getShort(false);
        int x = msg.getPayload().getShort(false, ByteOrder.LITTLE);
        var pos = new Position(x, y, player.getPosition().getZ());
        
        checkState(ItemDefinition.isIdValid(id), "invalid item id");

        if (!player.getPosition().isViewable(pos)) {
            return null;
        }

        // Try to pickup the item (verification).
        Stream<GroundItem> localItems = player.getChunks().getChunk(pos.getChunkPosition()).stream(EntityType.ITEM);

        localItems.filter(item -> item.getPosition().equals(pos))
                .filter(item -> item.getId() == id)
                .findFirst()
                .ifPresent(item -> {
                    var event = new PickupItemEvent(player, x, y, id);
                    player.submitAction(new InteractionAction(player, item, event));
                });
        return null;
    }
}
