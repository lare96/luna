package io.luna.game.model.def;

import com.google.common.collect.ImmutableList;
import io.luna.game.model.def.DefinitionRepository.ArrayDefinitionRepository;

import java.util.OptionalInt;

/**
 * A definition model describing an item.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class ItemDefinition implements Definition {

    /**
     * Determines if {@code id} is valid.
     *
     * @param id The identifier.
     * @return {@code true} if the identifier is valid.
     */
    public static boolean isIdValid(int id) {
        return id > 0 && id < SIZE;
    }

    /**
     * The definition count.
     */
    public static final int SIZE = 7956;

    /**
     * The item definition repository.
     */
    public static final DefinitionRepository<ItemDefinition> ALL = new ArrayDefinitionRepository<>(SIZE);

    /**
     * The identifier.
     */
    private final int id;

    /**
     * The name.
     */
    private final String name;

    /**
     * The examine text.
     */
    private final String examine;

    /**
     * If the item is stackable.
     */
    private final boolean stackable;

    /**
     * The base value.
     */
    private final int value;

    /**
     * The noted identifier.
     */
    private final OptionalInt notedId;

    /**
     * The unnoted identifier.
     */
    private final OptionalInt unnotedId;

    /**
     * If this item is members only.
     */
    private final boolean membersOnly;

    /**
     * The weight.
     */
    private final double weight;

    /**
     * If this item can be traded.
     */
    private final boolean tradeable;

    /**
     * A set of inventory actions.
     */
    private final ImmutableList<String> inventoryActions;

    /**
     * A set of ground actions.
     */
    private final ImmutableList<String> groundActions;

    /**
     * Creates a new {@link ItemDefinition}.
     *
     * @param id The identifier.
     * @param name The name.
     * @param examine The examine text.
     * @param stackable If the item is stackable.
     * @param value The base value.
     * @param notedId The noted identifier.
     * @param unnotedId The unnoted identifier.
     * @param membersOnly If this item is members only.
     * @param weight The weight.
     * @param tradeable If this item can be traded.
     * @param inventoryActions A list of inventory actions.
     * @param groundActions A list of ground actions.
     */
    public ItemDefinition(int id, String name, String examine, boolean stackable, int value, int notedId,
                          int unnotedId, boolean membersOnly, double weight, boolean tradeable,
                          String[] inventoryActions, String[] groundActions) {
        this.id = id;
        this.name = name;
        this.examine = examine;
        this.stackable = stackable;
        this.value = value;
        this.notedId = notedId == -1 ? OptionalInt.empty() : OptionalInt.of(notedId);
        this.unnotedId = unnotedId == -1 ? OptionalInt.empty() : OptionalInt.of(unnotedId);
        this.membersOnly = membersOnly;
        this.weight = weight;
        this.tradeable = tradeable;
        this.inventoryActions = ImmutableList.copyOf(inventoryActions);
        this.groundActions = ImmutableList.copyOf(groundActions);
    }

    /**
     * Returns {@code true} if the item can be noted.
     */
    public boolean isNoteable() {
        return notedId.isPresent();
    }

    /**
     * Returns {@code true} if the item is noted.
     */
    public boolean isNoted() {
        return unnotedId.isPresent();
    }

    /**
     * @return The item identifier.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The examine text.
     */
    public String getExamine() {
        return examine;
    }

    /**
     * @return If the item is stackable.
     */
    public boolean isStackable() {
        return stackable;
    }

    /**
     * @return The base value.
     */
    public int getValue() {
        return value;
    }

    /**
     * @return The noted identifier.
     */
    public OptionalInt getNotedId() {
        return notedId;
    }

    /**
     * @return The unnoted identifier.
     */
    public OptionalInt getUnnotedId() {
        return unnotedId;
    }

    /**
     * @return If this item is members only.
     */
    public boolean isMembersOnly() {
        return membersOnly;
    }

    /**
     * @return The weight.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @return If this item can be traded.
     */
    public boolean isTradeable() {
        return tradeable;
    }

    /**
     * @return A set of inventory actions.
     */
    public ImmutableList<String> getInventoryActions() {
        return inventoryActions;
    }

    /**
     * @return A set of ground actions.
     */
    public ImmutableList<String> getGroundActions() {
        return groundActions;
    }
}