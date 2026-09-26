package edu.towson.cis.cosc442.project1.monopoly;

public abstract class Card {

    public static int TYPE_CHANCE = 1;
    public static int TYPE_CC = 2;

    /**
     * Returns the label or description of the card.
     * @return the label or description text of the card
     */
    /**
     * Returns the label or description of the card.
     * @return the label or description text of the card
     */
    public abstract String getLabel();
    /**
     * Executes the action associated with the card.
     */
    /**
     * Executes the action associated with the card.
     */
    public abstract void applyAction();
    /**
     * Returns the type identifier of the card.
     * @return an integer indicating the card type (e.g., TYPE_CHANCE or TYPE_CC)
     */
    /**
     * Returns the type identifier of the card.
     * @return an integer indicating the card type (e.g., TYPE_CHANCE or TYPE_CC)
     */
    public abstract int getCardType();
}
