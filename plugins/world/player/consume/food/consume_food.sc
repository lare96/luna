import io.luna.game.event.impl.ItemClickEvent.ItemFirstClickEvent
import io.luna.game.model.item.Item
import io.luna.game.model.mob.{Animation, Player}


/* Class representing food in the 'FOOD_TABLE'. */
case class Food(heal: Int, delay: Long, ids: Int*) {
  def effect(plr: Player) = {}

  def firstMessage(name: String) = s"You eat the $name."

  def secondMessage(name: String) = "It heals some health."
}

/* An extension of 'Food' specific to purple sweets. */
final class PurpleSweets extends Food(3, 1800, 4561) {
  override def effect(plr: Player) = plr.changeRunEnergy(10.0)

  override def firstMessage(name: String) = "You eat the sweets."

  override def secondMessage(name: String) = "The sugary goodness heals some energy."
}


/* Consume food animation. */
private val ANIMATION = new Animation(829)

/*
 A table of all the foods that can be eaten.

 food_symbol -> Food
*/
private val FOOD_TABLE = Map(
  'cooked_meat -> Food(heal = 2,
    delay = 1800,
    ids = 2142),

  'cooked_chicken -> Food(heal = 2,
    delay = 1800,
    ids = 2140),

  'herring -> Food(heal = 2,
    delay = 1800,
    ids = 347),

  'anchovies -> Food(heal = 2,
    delay = 1800,
    ids = 319),

  'redberry_pie -> Food(heal = 2,
    delay = 600,
    ids = 2325, 2333),

  'shrimp -> Food(heal = 3,
    delay = 1800,
    ids = 315),

  'cake -> Food(heal = 4,
    delay = 1800,
    ids = 1891, 1893, 1895),

  'cod -> Food(heal = 4,
    delay = 1800,
    ids = 339),

  'pike -> Food(heal = 4,
    delay = 1800,
    ids = 351),

  'chocolate_cake -> Food(heal = 5,
    delay = 1800,
    ids = 1897, 1899, 1901),

  'mackerel -> Food(heal = 6,
    delay = 1800,
    ids = 355),

  'meat_pie -> Food(heal = 6,
    delay = 600,
    ids = 2327, 2331),

  'plain_pizza -> Food(heal = 7,
    delay = 1800,
    ids = 2289, 2291),

  'apple_pie -> Food(heal = 7,
    delay = 600,
    ids = 2323, 2335),

  'trout -> Food(heal = 7,
    delay = 1800,
    ids = 333),

  'meat_pizza -> Food(heal = 8,
    delay = 1800,
    ids = 2293, 2295),

  'anchovy_pizza -> Food(heal = 9,
    delay = 1800,
    ids = 2297, 2299),

  'salmon -> Food(heal = 9,
    delay = 1800,
    ids = 329),

  'bass -> Food(heal = 9,
    delay = 1800,
    ids = 365),

  'tuna -> Food(heal = 10,
    delay = 1800,
    ids = 361),

  'pineapple_pizza -> Food(heal = 11,
    delay = 1800,
    ids = 2301, 2303),

  'lobster -> Food(heal = 12,
    delay = 1800,
    ids = 379),

  'swordfish -> Food(heal = 14,
    delay = 1800,
    ids = 373),

  'monkfish -> Food(heal = 16,
    delay = 1800,
    ids = 7946),

  'karambwan -> Food(heal = 18,
    delay = 600,
    ids = 3144),

  'shark -> Food(heal = 20,
    delay = 1800,
    ids = 385),

  'manta_ray -> Food(heal = 22,
    delay = 1800,
    ids = 391),

  'sea_turtle -> Food(heal = 22,
    delay = 1800,
    ids = 397),

  'tuna_potato -> Food(heal = 22,
    delay = 1800,
    ids = 7060),

  'purple_sweets -> new PurpleSweets
)

/*
 A different mapping of the 'FOOD_TABLE' that maps food ids to their data.

 food_id -> Food
*/
private val ID_TO_FOOD = {
  for {
    (symbol, food) <- FOOD_TABLE
    foodId <- food.ids
  } yield foodId -> food
}


// TODO Stop eating process if player has just died.
// TODO Confirm duel rule for no food.
/* Attempt to consume food, if we haven't just recently consumed any. */
private def consume(plr: Player, food: Food, index: Int): Unit = {
  val inventory = plr.inventory
  val skill = plr.skill(SKILL_HITPOINTS)
  val ids = food.ids

  if (!plr.elapsedTime("last_food_consume", food.delay)) {
    return
  }

  plr.interruptAction()

  val toConsume = inventory.computeIdForIndex(index).orElse(-1)
  if (toConsume != -1 && inventory.remove(index, new Item(toConsume))) {
    val consumeName = nameOfItem(toConsume)

    val nextIndex = ids.indexOf(toConsume) + 1
    if (ids.isDefinedAt(nextIndex)) {
      /* Add unfinished portion to inventory, if there is one. */
      inventory.add(index, new Item(ids(nextIndex)))
    }

    plr.sendMessage(food.firstMessage(consumeName))
    plr.animation(ANIMATION)
    food.effect(plr)

    if (skill.getLevel < skill.getStaticLevel) {
      skill.addLevels(food.heal, false)
      plr.sendMessage(food.secondMessage(consumeName))
    }
  }

  plr.resetTime("last_food_consume")
}


/* Intercept first click item event, attempt to consume food if applicable. */
on[ItemFirstClickEvent].run { msg =>
  ID_TO_FOOD.get(msg.id).foreach { food =>
    consume(msg.plr, food, msg.index)
    msg.terminate
  }
}
