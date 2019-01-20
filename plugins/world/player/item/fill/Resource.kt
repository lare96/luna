package world.player.item.fill

import api.predef.*
import io.luna.game.event.item.ItemOnObjectEvent
import io.luna.game.model.def.ObjectDefinition
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Player
import io.luna.game.model.mob.dialogue.MakeItemDialogueInterface

/**
 * An abstraction model representing a resource within the Runescape world that fills items.
 */
abstract class Resource {

    /**
     * Determines if [obj] matches this resource.
     */
    abstract fun matches(obj: ObjectDefinition): Boolean

    /**
     * Retrieves the id of [empty] filled with this resource.
     */
    abstract fun getFilled(empty: Int): Int?

    /**
     * Invoked when the item is filled with the resource.
     */
    open fun onFill(plr: Player) {

    }

    /**
     * Open an interface that allows for filling [emptyId] with this resource, for [plr].
     */
    private fun fill(plr: Player, emptyId: Int, objectId: Int? = null, msg: ItemOnObjectEvent? = null) {
        val filled = getFilled(emptyId)
        if (filled != null) {
            plr.interfaces.open(object : MakeItemDialogueInterface(filled) {
                override fun makeItem(player: Player, id: Int, index: Int, forAmount: Int) {
                    val emptyItem = Item(emptyId)
                    val filledItem = Item(filled)
                    plr.submitAction(FillAction(plr, emptyItem, objectId, filledItem, this@Resource, forAmount))
                }
            })
            msg?.terminate()
        }
    }

    /**
     * Register
     */
    fun register() {
        for (obj in ObjectDefinition.ALL) {
            if (obj != null && matches(obj)) {
                // Dynamically cached, so we don't have to worry about 'matches' performance.
                on(ItemOnObjectEvent::class)
                    .condition { objectId == obj.id }
                    .then { fill(plr, itemId, objectId, this) }
            }
        }
    }
}