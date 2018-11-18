import io.luna.game.event.impl.ObjectClickEvent.ObjectFirstClickEvent
import io.luna.game.model.mob.{Animation, Player}


/* Recharge prayer animation. */
private val ANIMATION = new Animation(645)


/* A method that attempts to recharge the player's prayer. */
private def rechargePrayer(plr: Player) = {
  val skill = plr.skill(SKILL_PRAYER)

  if (skill.getLevel < skill.getStaticLevel) {
    skill.setLevel(skill.getStaticLevel)

    plr.animation(ANIMATION)
    plr.sendMessage("You recharge your Prayer points.")
  } else {
    plr.sendMessage("You already have full Prayer points.")
  }
}


/* If the object being clicked is an altar, recharge prayer. */
on[ObjectFirstClickEvent].
  args(409, 3243).
  run { msg =>
    rechargePrayer(msg.plr)
    msg.terminate
  }
