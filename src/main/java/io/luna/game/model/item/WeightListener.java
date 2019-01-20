package io.luna.game.model.item;

import io.luna.game.model.mob.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A listener that updates a Player's weight.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class WeightListener implements ItemContainerListener {

    /**
     * The player.
     */
    private final Player player;

    /**
     * A list of weight changes.
     */
    private final List<Double> weightChanges = new ArrayList<>();

    /**
     * Creates a new {@link WeightListener}.
     *
     * @param player The player.
     */
    public WeightListener(Player player) {
        this.player = player;
    }

    @Override
    public void onSingleUpdate(int index, ItemContainer items, Item oldItem, Item newItem) {
        updateWeight(oldItem, newItem);
    }

    @Override
    public void onBulkUpdate(int index, ItemContainer items, Item oldItem, Item newItem) {
        weightChanges.add(computeWeightDifference(oldItem, newItem));
    }

    @Override
    public void onBulkUpdateCompleted(ItemContainer items) {
        Iterator<Double> iterator = weightChanges.iterator();
        double currentWeight = player.getWeight();

        while (iterator.hasNext()) {
            currentWeight += iterator.next();
            iterator.remove();
        }
        
        player.setWeight(currentWeight);
    }

    /**
     * Updates the weight for a single item set.
     *
     * @param oldItem The old item.
     * @param newItem The new item.
     */
    private void updateWeight(Item oldItem, Item newItem) {
        player.setWeight(player.getWeight() + computeWeightDifference(oldItem, newItem));
    }

    /**
     * Computes the weight difference for a single item set.
     *
     * @param oldItem The old item.
     * @param newItem The new item.
     * @return The difference in weight between {@code oldItem} and {@code newItem}.
     */
    private double computeWeightDifference(Item oldItem, Item newItem) {
        double subtract = computeWeight(oldItem);
        double add = computeWeight(newItem);
        return add - subtract;
    }

    /**
     * Computes the weight of {@code item}.
     *
     * @param item The item to compute for.
     * @return The weight.
     */
    private double computeWeight(Item item) {
        return item == null ? 0D : item.getItemDef().getWeight();
    }
}
