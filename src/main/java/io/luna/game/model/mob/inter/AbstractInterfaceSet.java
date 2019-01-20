package io.luna.game.model.mob.inter;

import io.luna.game.model.mob.Player;
import io.luna.net.msg.out.CloseWindowsMessageWriter;
import io.luna.net.msg.out.WalkableInterfaceMessageWriter;

import java.util.Optional;

/**
 * A collection of {@link AbstractInterface}s that are displayed on the Player's game screen.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class AbstractInterfaceSet {

    /**
     * The player instance.
     */
    private final Player player;

    /**
     * The current standard interface.
     */
    private StandardInterface currentStandard;

    /**
     * The current input interface.
     */
    private InputInterface currentInput;

    /**
     * The current walkable interface.
     */
    private WalkableInterface currentWalkable;

    /**
     * Creates a new {@link AbstractInterfaceSet}.
     *
     * @param player The player instance.
     */
    public AbstractInterfaceSet(Player player) {
        this.player = player;
    }

    /**
     * Opens a new interface.
     *
     * @param inter The interface to open.
     */
    public void open(AbstractInterface inter) {
        if (inter.isStandard()) {
            setCurrentStandard((StandardInterface) inter);
        } else if (inter.isInput()) {
            setCurrentInput((InputInterface) inter);
        } else if (inter.isWalkable()) {
            setCurrentWalkable((WalkableInterface) inter);
        }
        
        inter.setOpened(player);
    }

    /**
     * Closes all windows except {@link WalkableInterface} interface types.
     */
    public void close() {
        if (isStandardOpen() || isInputOpen()) {
            player.queue(new CloseWindowsMessageWriter());
            player.resetDialogues();
            setCurrentStandard(null);
            setCurrentInput(null);
        }
    }

    /**
     * Closes the current {@link WalkableInterface}.
     */
    public void closeWalkable() {
        if (isWalkableOpen()) {
            player.queue(new WalkableInterfaceMessageWriter(-1));
            setCurrentWalkable(null);
        }
    }

    /**
     * Closes all interfaces on the game screen.
     */
    public void closeAll() {
        close();
        closeWalkable();
    }

    /**
     * Closes all necessary interfaces on movement or action initialization.
     */
    public void applyActionClose() {
        // Close standard and input interfaces if needed.
        if (isAutoClose(currentStandard) || isAutoClose(currentInput)) {
            close();
        }

        // Close walkable interfaces if needed.
        if (isAutoClose(currentWalkable)) {
            closeWalkable();
        }

        // Reset dialogues.
        player.resetDialogues();
    }

    /**
     * Determines if an interface needs to close on movement or action initialization.
     *
     * @param abstractInterface The interface.
     * @return {@code true} if the interface needs to close.
     */
    private <I extends AbstractInterface> boolean isAutoClose(I abstractInterface) {
        return abstractInterface != null && abstractInterface.isAutoClose(player);
    }

    /**
     * Determines if a walkable interface is open.
     *
     * @return {@code true} if a walkable interface is open.
     */
    public boolean isWalkableOpen() {
        return currentWalkable != null;
    }

    /**
     * Determines if an input interface is open.
     *
     * @return {@code true} if an input interface is open.
     */
    public boolean isInputOpen() {
        return currentInput != null;
    }

    /**
     * Determines if a standard interface is open.
     *
     * @return {@code true} if a standard interface is open.
     */
    public boolean isStandardOpen() {
        return currentStandard != null;
    }

    /**
     * Sets the current standard interface.
     *
     * @param inter The new interface.
     */
    private void setCurrentStandard(StandardInterface inter) {
        if (currentStandard != null) {
            currentStandard.setClosed(player, inter);
        }
        
        currentStandard = inter;
    }

    /**
     * @return The current standard interface.
     */
    public Optional<StandardInterface> getCurrentStandard() {
        return Optional.ofNullable(currentStandard);
    }

    /**
     * Casts the current standard interface to {@code type}.
     *
     * @param type The type to cast to.
     * @param <I> The type.
     * @return The casted interface.
     */
    public <I extends StandardInterface> Optional<I> standardTo(Class<I> type) {
        if (currentStandard == null) {
            return Optional.empty();
        }

        if (type.isInstance(currentStandard)) {
            return Optional.of(type.cast(currentStandard));
        }
    
        return Optional.empty();
    }

    /**
     * Resets the current input interface.
     */
    public void resetCurrentInput() {
        setCurrentInput(null);
    }

    /**
     * Sets the current input interface.
     *
     * @param inter The new interface.
     */
    private void setCurrentInput(InputInterface inter) {
        if (currentInput != null) {
            currentInput.setClosed(player, inter);
        }
        
        currentInput = inter;
    }

    /**
     * @return The current input interface.
     */
    public Optional<InputInterface> getCurrentInput() {
        return Optional.ofNullable(currentInput);
    }

    /**
     * Casts the current input interface to {@code type}.
     *
     * @param type The type to cast to.
     * @param <I> The type.
     * @return The casted interface.
     */
    public <I extends InputInterface> Optional<I> inputTo(Class<I> type) {
        if (currentInput == null) {
            return Optional.empty();
        }

        if (type.isInstance(currentInput)) {
            return Optional.of(type.cast(currentInput));
        }
    
        return Optional.empty();
    }

    /**
     * Sets the current walkable interface.
     *
     * @param inter The new interface.
     */
    private void setCurrentWalkable(WalkableInterface inter) {
        if (currentWalkable != null) {
            currentWalkable.setClosed(player, inter);
        }
        
        currentWalkable = inter;
    }

    /**
     * @return The current walkable interface.
     */
    public Optional<WalkableInterface> getCurrentWalkable() {
        return Optional.ofNullable(currentWalkable);
    }

    /**
     * Casts the current walkable interface to {@code type}.
     *
     * @param type The type to cast to.
     * @param <I> The type.
     * @return The casted interface.
     */
    public <I extends WalkableInterface> Optional<I> walkableTo(Class<I> type) {
        if (currentWalkable == null) {
            return Optional.empty();
        }
        
        if (type.isInstance(currentWalkable)) {
            return Optional.of(type.cast(currentWalkable));
        }
    
        return Optional.empty();
    }
}
