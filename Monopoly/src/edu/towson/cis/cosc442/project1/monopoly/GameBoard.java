package edu.towson.cis.cosc442.project1.monopoly;

import java.util.ArrayList;
import java.util.Hashtable;

public class GameBoard {

	private ArrayList<Cell> cells = new ArrayList<Cell>();
    private ArrayList<Card> chanceCards = new ArrayList<Card>();
	//the key of colorGroups is the name of the color group.
	private Hashtable<String, Integer> colorGroups = new Hashtable<String, Integer>();
	private ArrayList<Card> communityChestCards = new ArrayList<Card>();
	/**
	 * Initializes a new GameBoard instance with the first cell set as 'Go'.
	 */
	/**
	 * Initializes a new GameBoard instance with the first cell set as 'Go'.
	 */
	public GameBoard() {
		Cell go = new GoCell();
		addCell(go);
	}

    /**
     * Adds a card to the appropriate deck based on its type (community chest or chance).
     * @param card The card to add to the game board decks
     */
    /**
     * Adds a card to the appropriate deck based on its type (community chest or chance).
     * @param card The card to add to the game board decks
     */
    public void addCard(Card card) {
        if(card.getCardType() == Card.TYPE_CC) {
            communityChestCards.add(card);
        } else {
            chanceCards.add(card);
        }
    }
	
	/**
	 * Adds a generic cell to the game board's list of cells.
	 * @param cell The cell to add to the game board
	 */
	/**
	 * Adds a generic cell to the game board's list of cells.
	 * @param cell The cell to add to the game board
	 */
	public void addCell(Cell cell) {
		cells.add(cell);
	}
	
	/**
	 * Adds a property cell to the game board and updates the count for its color group.
	 * @param cell The property cell to add
	 */
	/**
	 * Adds a property cell to the game board and updates the count for its color group.
	 * @param cell The property cell to add
	 */
	public void addCell(PropertyCell cell) {
		String colorGroup = cell.getColorGroup();
		int propertyNumber = getPropertyNumberForColor(colorGroup);
		colorGroups.put(colorGroup, propertyNumber +1);
        cells.add(cell);
	}

    /**
     * Draws the next community chest card, moves it to the back of the deck, and returns it.
     * @return The community chest card drawn from the deck
     */
    /**
     * Draws the next community chest card, moves it to the back of the deck, and returns it.
     * @return The community chest card drawn from the deck
     */
    public Card drawCCCard() {
        Card card = (Card)communityChestCards.get(0);
        communityChestCards.remove(0);
        addCard(card);
        return card;
    }

    /**
     * Draws the next chance card, moves it to the back of the deck, and returns it.
     * @return The chance card drawn from the deck
     */
    /**
     * Draws the next chance card, moves it to the back of the deck, and returns it.
     * @return The chance card drawn from the deck
     */
    public Card drawChanceCard() {
        Card card = (Card)chanceCards.get(0);
        chanceCards.remove(0);
        addCard(card);
        return card;
    }

	/**
	 * Retrieves the cell at the specified index on the game board.
	 * @param newIndex The index of the cell to retrieve
	 * @return The cell at the given index
	 */
	/**
	 * Retrieves the cell at the specified index on the game board.
	 * @param newIndex The index of the cell to retrieve
	 * @return The cell at the given index
	 */
	public Cell getCell(int newIndex) {
		return (Cell)cells.get(newIndex);
	}
	
	/**
	 * Returns the total number of cells currently on the game board.
	 * @return The number of cells on the board
	 */
	/**
	 * Returns the total number of cells currently on the game board.
	 * @return The number of cells on the board
	 */
	public int getCellNumber() {
		return cells.size();
	}
	
	/**
	 * Returns all property cells of a given color group representing a monopoly.
	 * @param color The color group name to retrieve properties for
	 * @return An array of property cells belonging to the specified color group
	 */
	/**
	 * Returns all property cells of a given color group representing a monopoly.
	 * @param color The color group name to retrieve properties for
	 * @return An array of property cells belonging to the specified color group
	 */
	public PropertyCell[] getPropertiesInMonopoly(String color) {
		PropertyCell[] monopolyCells = 
			new PropertyCell[getPropertyNumberForColor(color)];
		int counter = 0;
		for (int i = 0; i < getCellNumber(); i++) {
			Cell c = getCell(i);
			if(c instanceof PropertyCell) {
				PropertyCell pc = (PropertyCell)c;
				if(pc.getColorGroup().equals(color)) {
					monopolyCells[counter] = pc;
					counter++;
				}
			}
		}
		return monopolyCells;
	}
	
	/**
	 * Returns the number of properties in the specified color group.
	 * @param name The name of the color group
	 * @return The count of properties in the specified color group
	 */
	/**
	 * Returns the number of properties in the specified color group.
	 * @param name The name of the color group
	 * @return The count of properties in the specified color group
	 */
	public int getPropertyNumberForColor(String name) {
		Integer number = (Integer)colorGroups.get(name);
		if(number != null) {
			return number.intValue();
		}
		return 0;
	}

	/**
	 * Finds and returns the cell with the specified name.
	 * @param string The name of the cell to find
	 * @return The cell matching the specified name, or null if not found
	 */
	/**
	 * Finds and returns the cell with the specified name.
	 * @param string The name of the cell to find
	 * @return The cell matching the specified name, or null if not found
	 */
	public Cell queryCell(String string) {
		for(int i = 0; i < cells.size(); i++){
			Cell temp = (Cell)cells.get(i); 
			if(temp.getName().equals(string)) {
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * Finds and returns the index of the cell with the specified name.
	 * @param string The name of the cell to find
	 * @return The index of the cell with the specified name, or -1 if not found
	 */
	/**
	 * Finds and returns the index of the cell with the specified name.
	 * @param string The name of the cell to find
	 * @return The index of the cell with the specified name, or -1 if not found
	 */
	public int queryCellIndex(String string){
		for(int i = 0; i < cells.size(); i++){
			Cell temp = (Cell)cells.get(i); 
			if(temp.getName().equals(string)) {
				return i;
			}
		}
		return -1;
	}

    /**
     * Clears all community chest cards from the game board.
     */
    /**
     * Clears all community chest cards from the game board.
     */
    public void removeCards() {
        communityChestCards.clear();
    }
}
