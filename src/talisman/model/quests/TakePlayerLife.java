package talisman.model.quests;

/**
 * Take 1 life from another player's character to complete this quest
 *
 * @author Enrico Maria Montanari
 */
public class TakePlayerLife extends TalismanQuest{

    /**
     * Gets a description of the objective of the current quest
     *
     * @return the description
     */
    @Override
    public String toString(){

        return "Take 1 life from another Character";
    }
}
