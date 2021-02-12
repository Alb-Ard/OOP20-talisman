package talisman.model.action;

import java.io.Serializable;

import talisman.model.board.BoardPawn;

/**
 * Interface for actions that will be applied to a player when it reaches a
 * cell.
 * 
 * @author Alberto Arduini
 *
 */
public interface TalismanAction extends Serializable {
    /**
     * Gets what to show if no action is present.
     */
    String NO_ACTION_DESCRIPTION = "Do nothing";
    /**
     * The flag value in case an optional field is serialized.
     */
    int SERIALIZED_PRESENT = 1;
    /**
     * The flag value in case an optional field is not serialized.
     */
    int SERIALIZED_MISSING = 0;

    /**
     * Obtains the action's description.
     * 
     * @return the description
     */
    String getDescription();

    /**
     * Applies the action to the specified player.
     * 
     * @param player the player on which the action will execute
     */
    void applyTo(int player);

    /**
     * Checks if the action can be applied to a player.
     * 
     * @param player the player on which the action should be execute
     * @return if the action can be applied
     */
    default boolean canBeApplied(final int player) {
        return true;
    }
}