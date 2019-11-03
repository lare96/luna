import io.luna.game.event.Event
import io.luna.game.event.EventListener
import io.luna.game.event.impl.ButtonClickEvent
import io.luna.game.event.impl.CommandEvent
import io.luna.game.event.impl.ItemClickEvent.*
import io.luna.game.event.impl.ItemOnItemEvent
import io.luna.game.event.impl.ItemOnObjectEvent
import io.luna.game.event.impl.NpcClickEvent.*
import io.luna.game.event.impl.ObjectClickEvent.*
import io.luna.game.model.mob.PlayerRights
import java.util.*
import kotlin.reflect.KClass

/**
 * The player dedicated event listener consumer alias.
 */
private typealias Action<E> = E.() -> Unit

/**
 * The command key, used to match [CommandEvent]s.
 */
class CommandKey(val name: String, val rights: PlayerRights) {
    override fun hashCode() = Objects.hash(name)
    override fun equals(other: Any?) =
        when (other) {
            is CommandKey -> name == other.name
            else -> false
        }
}

/**
 * The main event interception function. Forwards to [InterceptBy].
 */
fun <E : Event> on(eventClass: KClass<E>) = InterceptBy(eventClass)

/**
 * The main event interception function. Runs the action without any forwarding.
 */
fun <E : Event> on(eventClass: KClass<E>, action: Action<E>) {
    scriptListeners += EventListener(eventClass.java, action)
}

/**
 * The [ItemOnItemEvent] and [ItemOnObjectEvent] matcher function. Forwards to [InterceptUseItem].
 */
fun useItem(id: Int) = InterceptUseItem(id)

/**
 * The [CommandEvent] matcher function.
 */
fun cmd(name: String, rights: PlayerRights, action: Action<CommandEvent>) {
    val matcher = Matcher.get<CommandEvent, CommandKey>()
    matcher[CommandKey(name, rights)] = {
        if (plr.rights >= rights) {
            action(this)
        }
    }
}

/**
 * The [ButtonClickEvent] matcher function.
 */
fun button(id: Int, action: Action<ButtonClickEvent>) =
    Matcher.get<ButtonClickEvent, Int>().set(id, action)


/** The [NpcFirstClickEvent] matcher function.*/
fun npc1(id: Int, action: Action<NpcFirstClickEvent>) =
    Matcher.get<NpcFirstClickEvent, Int>().set(id, action)

/** The [NpcSecondClickEvent] matcher function.*/
fun npc2(id: Int, action: Action<NpcSecondClickEvent>) =
    Matcher.get<NpcSecondClickEvent, Int>().set(id, action)

/** The [NpcThirdClickEvent] matcher function.*/
fun npc3(id: Int, action: Action<NpcThirdClickEvent>) =
    Matcher.get<NpcThirdClickEvent, Int>().set(id, action)

/** The [NpcFourthClickEvent] matcher function.*/
fun npc4(id: Int, action: Action<NpcFourthClickEvent>) =
    Matcher.get<NpcFourthClickEvent, Int>().set(id, action)

/** The [NpcFifthClickEvent] matcher function.*/
fun npc5(id: Int, action: Action<NpcFifthClickEvent>) =
    Matcher.get<NpcFifthClickEvent, Int>().set(id, action)


/** The [ItemFirstClickEvent] matcher function.*/
fun item1(id: Int, action: Action<ItemFirstClickEvent>) =
    Matcher.get<ItemFirstClickEvent, Int>().set(id, action)

/** The [ItemSecondClickEvent] matcher function.*/
fun item2(id: Int, action: Action<ItemSecondClickEvent>) =
    Matcher.get<ItemSecondClickEvent, Int>().set(id, action)

/** The [ItemThirdClickEvent] matcher function.*/
fun item3(id: Int, action: Action<ItemThirdClickEvent>) =
    Matcher.get<ItemThirdClickEvent, Int>().set(id, action)

/** The [ItemFourthClickEvent] matcher function.*/
fun item4(id: Int, action: Action<ItemFourthClickEvent>) =
    Matcher.get<ItemFourthClickEvent, Int>().set(id, action)

/** The [ItemFifthClickEvent] matcher function.*/
fun item5(id: Int, action: Action<ItemFifthClickEvent>) =
    Matcher.get<ItemFifthClickEvent, Int>().set(id, action)


/** The [ObjectFirstClickEvent] matcher function.*/
fun object1(id: Int, action: Action<ObjectFirstClickEvent>) =
    Matcher.get<ObjectFirstClickEvent, Int>().set(id, action)

/** The [ObjectSecondClickEvent] matcher function.*/
fun object2(id: Int, action: Action<ObjectSecondClickEvent>) =
    Matcher.get<ObjectSecondClickEvent, Int>().set(id, action)

/** The [ObjectThirdClickEvent] matcher function.*/
fun object3(id: Int, action: Action<ObjectThirdClickEvent>) =
    Matcher.get<ObjectThirdClickEvent, Int>().set(id, action)