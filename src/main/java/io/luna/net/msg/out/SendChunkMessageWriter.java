package io.luna.net.msg.out;

import io.luna.game.model.Position;
import io.luna.game.model.mob.Player;
import io.luna.net.codec.ByteMessage;
import io.luna.net.codec.ValueType;
import io.luna.net.msg.GameMessageWriter;

/**
 * A {@link GameMessageWriter} implementation that marks a chunk.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class SendChunkMessageWriter extends GameMessageWriter {

    /**
     * The position's chunk to mark.
     */
    private final Position position;

    /**
     * Creates a new {@link SendChunkMessageWriter}.
     *
     * @param position The position's chunk to mark.
     */
    public SendChunkMessageWriter(Position position) {
        this.position = position;
    }

    @Override
    public ByteMessage write(Player player) {
        var position = player.getPosition();
        var msg = ByteMessage.message(85);
        msg.put(this.position.getLocalY(position), ValueType.NEGATE);
        msg.put(this.position.getLocalX(position), ValueType.NEGATE);
        return msg;
    }
}