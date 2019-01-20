import api.predef.*
import io.luna.game.event.entity.player.EquipmentChangeEvent
import io.luna.game.event.entity.player.LoginEvent
import io.luna.game.model.item.Equipment
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Player
import world.player.skill.runecrafting.Tiara

/**
 * Sends tiara altar config on login.
 */
fun loginUpdate(plr: Player) {
    val headId = plr.equipment.get(Equipment.HEAD)?.id
    val value = Tiara.ID_TO_TIARA[headId]?.config
    plr.sendConfig(491, value ?: 0)
}

/**
 * Sends tiara altar config on equipment change.
 */
fun equipmentUpdate(plr: Player, newItem: Item?) {
    val new = Tiara.ID_TO_TIARA[newItem?.id]
    plr.sendConfig(491, new?.config ?: 0)
}

/**
 * Forward to [loginUpdate].
 */
on(LoginEvent::class) { loginUpdate(plr) }

/**
 * Forward to [equipmentUpdate] if the changed equipment was in the head slot.
 */
on(EquipmentChangeEvent::class)
    .condition { index == Equipment.HEAD }
    .then { equipmentUpdate(plr, newItem) }

