package world.player.skill.herblore.makeUnfPotion

import api.predef.*
import io.luna.game.action.Action
import io.luna.game.action.InventoryAction
import io.luna.game.event.impl.ItemOnItemEvent
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Animation
import io.luna.game.model.mob.Player
import io.luna.game.model.mob.dialogue.MakeItemDialogueInterface

/**
 * An [InventoryAction] that will make unfinished potions.
 */
class MakeUnfAction(plr: Player,
                    val unfPotion: UnfPotion,
                    makeTimes: Int) : InventoryAction(plr, true, 2, makeTimes) {

    companion object {

        /**
         * The unfinished potion making animation.
         */
        val ANIMATION = Animation(363)
    }

    override fun executeIf(start: Boolean) =
        when {
            mob.herblore.level < unfPotion.level -> {
                mob.sendMessage("You need a Herblore level of ${unfPotion.level} to make this potion.")
                false
            }
            else -> true
        }

    override fun execute() {
        mob.sendMessage("You put the ${unfPotion.herbName} into the vial of water.")
        mob.animation(ANIMATION)
    }

    override fun add() = listOf(unfPotion.idItem)

    override fun remove() = listOf(unfPotion.herbItem, Item(UnfPotion.VIAL_OF_WATER))

    override fun ignoreIf(other: Action<*>) =
        when (other) {
            is MakeUnfAction -> unfPotion == other.unfPotion
            else -> false
        }
}

/**
 * Opens a [MakeItemDialogueInterface] to make unfinished potions.
 */
fun makeUnf(msg: ItemOnItemEvent, herb: Int) {
    val plr = msg.plr
    val unfPotion = UnfPotion.HERB_TO_UNF[herb]
    if (unfPotion != null) {
        plr.interfaces.open(object : MakeItemDialogueInterface(unfPotion.id) {
            override fun makeItem(player: Player, id: Int, index: Int, forAmount: Int) =
                plr.submitAction(MakeUnfAction(plr, unfPotion, forAmount))
        })
    }
}

/**
 * Intercept event to make unf. potions if the required items are present.
 */
on(ItemOnItemEvent::class) {
    when (UnfPotion.VIAL_OF_WATER) {
        targetId -> makeUnf(this, usedId)
        usedId -> makeUnf(this, targetId)
    }
}
