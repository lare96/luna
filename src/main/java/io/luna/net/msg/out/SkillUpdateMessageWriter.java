package io.luna.net.msg.out;

import io.luna.game.model.mob.Player;
import io.luna.net.codec.ByteMessage;
import io.luna.net.codec.ByteOrder;
import io.luna.net.msg.GameMessageWriter;

/**
 * A {@link GameMessageWriter} implementation that displays the skill level and experience.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class SkillUpdateMessageWriter extends GameMessageWriter {

    /**
     * The skill identifier.
     */
    private final int id;

    /**
     * Creates a new {@link SkillUpdateMessageWriter}.
     *
     * @param id The skill identifier.
     */
    public SkillUpdateMessageWriter(int id) {
        this.id = id;
    }

    @Override
    public ByteMessage write(Player player) {
        var skill = player.skill(id);
        var msg = ByteMessage.message(134);
        msg.put(id);
        msg.putInt((int) skill.getExperience(), ByteOrder.MIDDLE);
        msg.put(skill.getLevel());
        return msg;
    }
}
