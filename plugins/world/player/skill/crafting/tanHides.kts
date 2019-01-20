import api.predef.*
import io.luna.game.event.button.ButtonClickEvent
import io.luna.game.event.server.ServerLaunchEvent
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Player
import io.luna.game.model.mob.inter.AmountInputInterface
import world.player.skill.crafting.Hide
import world.player.skill.crafting.TanInterface

/**
 * A data class representing the amount of a hide to tan.
 */
data class TanButton(val hide: Hide, val amount: TanAmount) {
    fun forAmount(plr: Player, action: (Int) -> Unit) {
        when (amount) {
            TanAmount.TAN_1 -> action(1)
            TanAmount.TAN_5 -> action(5)
            TanAmount.TAN_X -> plr.interfaces.open(object : AmountInputInterface() {
                override fun onAmountInput(player: Player?, value: Int) = action(value)
            })
            TanAmount.TAN_ALL -> action(plr.inventory.computeAmountForId(hide.hide))
        }
    }
}

/**
 * Tanning amounts.
 */
enum class TanAmount {
    TAN_1, TAN_5, TAN_X, TAN_ALL
}

/**
 * Mappings of button identifiers to the amount of hides made.
 */
val buttonToTan = mutableMapOf<Int, TanButton>()

/**
 * Tans the specified amount of hides.
 */
fun tan(plr: Player, hide: Hide, amount: Int) {
    val inv = plr.inventory
    if (!inv.contains(hide.hide)) {
        plr.sendMessage("You do not have any of these hides to tan.")
        return
    }

    val totalCost = hide.cost * amount
    val totalMoney = inv.computeAmountForId(995)
    var newAmount = amount
    if (totalCost > totalMoney) {
        newAmount = hide.cost / totalMoney
        if (newAmount <= 0) {
            plr.sendMessage("You do not have enough coins to tan this.")
            return
        }
    }

    val moneyItem = Item(995, hide.cost * newAmount)
    if (inv.remove(moneyItem)) {
        inv.replace(hide.hide, hide.tan, newAmount)
        plr.sendMessage("The tanner tans $newAmount hides for you.")
    }
}

/**
 * Opens the tanning interface.
 */
fun open(plr: Player) = plr.interfaces.open(TanInterface())

/**
 * Prepare tanning buttons and spawn tanner NPC.
 */
on(ServerLaunchEvent::class) {
    // Spawn tanner at home.
    world.addNpc(804,
                 3093,
                 3250)

    // Build button map.
    var button = 14817
    for (it in TanInterface.HIDES) {
        val make1 = button++
        val make5 = make1 - 8
        val makeX = make5 - 8
        val makeAll = makeX - 8
        buttonToTan[make1] = TanButton(it, TanAmount.TAN_1)
        buttonToTan[make5] = TanButton(it, TanAmount.TAN_5)
        buttonToTan[makeX] = TanButton(it, TanAmount.TAN_X)
        buttonToTan[makeAll] = TanButton(it, TanAmount.TAN_ALL)
    }
}

/**
 * Tanning button actions (1, 5, 10, X).
 */
on(ButtonClickEvent::class)
    .filter { plr.interfaces.isOpen(TanInterface::class) }
    .then {
        val tan = buttonToTan[id]
        tan?.forAmount(plr) { tan(plr, tan.hide, it) }
    }

/**
 * "Talk" option for tanner NPC.
 */
npc1(804) {
    plr.newDialogue()
        .npc(npc.id, "Would you like me to tan some hides?")
        .options("Yes", { open(it) },
                 "No", { it.interfaces.close() })
        .open()
}

/**
 * "Trade" option for tanner NPC.
 */
npc2(804) { open(plr) }