/**
 * An enum representing tools used to catch [Fish].
 */
enum class Tool(val id: Int,
                val level: Int,
                val bait: Int? = null,
                val catchAmount: IntRange = 1..1,
                val catchRate: Int,
                val animation: Int,
                val fish: List<Fish>) {

    SMALL_NET(id = 303,
              level = 1,
              catchRate = 15,
              animation = 621,
              fish = listOf(Fish.SHRIMP, Fish.ANCHOVY)),
    KARAMBWANJI_SMALL_NET(id = 303,
                          level = 5,
                          catchRate = 15,
                          animation = 621,
                          fish = listOf(Fish.SHRIMP, Fish.KARAMBWANJI)),
    FISHING_ROD(id = 307,
                level = 5,
                bait = 313,
                catchRate = 10,
                animation = 622,
                fish = listOf(Fish.SARDINE, Fish.HERRING, Fish.PIKE)),
    BIG_NET(id = 305,
            level = 16,
            catchAmount = 1..3,
            catchRate = 17,
            animation = 620,
            fish = listOf(Fish.MACKEREL, Fish.OYSTER, Fish.COD, Fish.BASS, Fish.CASKET, Fish.LEATHER_BOOTS,
                    Fish.LEATHER_GLOVES, Fish.SEAWEED)),
    FLY_FISHING_ROD(id = 309,
                    level = 20,
                    bait = 314,
                    catchRate = 10,
                    animation = 622,
                    fish = listOf(Fish.TROUT, Fish.SALMON)),
    HARPOON(id = 311,
            level = 35,
            catchRate = 18,
            animation = 618,
            fish = listOf(Fish.TUNA, Fish.SWORDFISH)),
    LOBSTER_POT(id = 301,
                level = 40,
                catchRate = 16,
                animation = 619,
                fish = listOf(Fish.LOBSTER)),
    MONKFISH_NET(id = 303,
                 level = 62,
                 catchRate = 13,
                 animation = 621,
                 fish = listOf(Fish.MONKFISH)),
    KARAMBWAN_VESSEL(id = 3157,
                     level = 65,
                     bait = 3150,
                     catchRate = 16,
                     animation = 519,
                     fish = listOf(Fish.KARAMBWAN)),
    SHARK_HARPOON(id = 311,
                  level = 76,
                  catchRate = 20,
                  animation = 618,
                  fish = listOf(Fish.SHARK))
}