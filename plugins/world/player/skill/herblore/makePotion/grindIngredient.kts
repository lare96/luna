import api.predef.itemDef
import api.predef.on
import io.luna.game.action.Action
import io.luna.game.action.InventoryAction
import io.luna.game.event.impl.ItemOnItemEvent
import io.luna.game.model.mob.Animation
import io.luna.game.model.mob.Player
import io.luna.game.model.mob.dialogue.MakeItemDialogueInterface
import world.player.skill.herblore.makePotion.Ingredient

/**
 * An [InventoryAction] that will grind all ingredients in an inventory.
 */
class GrindAction(plr: Player,
                  val ingredient: Ingredient,
                  makeTimes: Int) : InventoryAction(plr, true, 2, makeTimes) {

    companion object {

        /**
         * The grinding animation.
         */
        val ANIMATION = Animation(364)
    }

    override fun executeIf(start: Boolean) =
        when {
            !mob.inventory.contains(Ingredient.PESTLE_AND_MORTAR) -> false
            else -> true
        }


    override fun execute() {
        val oldName = itemDef(ingredient.id).name
        val newName = itemDef(ingredient.newId).name
        val nextWord = if (ingredient == Ingredient.CRUSHED_NEST) "a" else "some"
        mob.sendMessage("You grind the $oldName into $nextWord $newName.")

        mob.animation(ANIMATION)
    }

    override fun remove() = listOf(ingredient.oldItem)

    override fun add() = listOf(ingredient.newItem)

    override fun ignoreIf(other: Action<*>) =
        when (other) {
            is GrindAction -> ingredient == other.ingredient
            else -> false
        }
}

/**
 * Opens a [MakeItemDialogueInterface] for grinding ingredients.
 */
fun grind(msg: ItemOnItemEvent, id: Int) {
    val ingredient = Ingredient.OLD_TO_INGREDIENT[id]
    if (ingredient != null) {
        val interfaces = msg.plr.interfaces
        interfaces.open(object : MakeItemDialogueInterface(ingredient.newId) {
            override fun makeItem(plr: Player, id: Int, index: Int, forAmount: Int) =
                plr.submitAction(GrindAction(plr, ingredient, forAmount))
        })
    }
}

/**
 * Intercept event for using ingredient with pestle and mortar.
 */
on(ItemOnItemEvent::class) {
    when (Ingredient.PESTLE_AND_MORTAR) {
        usedId -> grind(this, targetId)
        targetId -> grind(this, usedId)
    }
}
