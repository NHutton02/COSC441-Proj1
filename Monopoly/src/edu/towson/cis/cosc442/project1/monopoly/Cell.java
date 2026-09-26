package edu.towson.cis.cosc442.project1.monopoly;

public abstract class Cell {
	private boolean available = true;
	private String name;
	protected Player theOwner;

	/**
	 * Returns the name of this cell.
	 * @return The name of the cell.
	 */
	/**
	 * Returns the name of this cell.
	 * @return The name of the cell.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the player who owns this cell.
	 * @return The owner player of the cell, or null if unowned.
	 */
	/**
	 * Returns the player who owns this cell.
	 * @return The owner player of the cell, or null if unowned.
	 */
	public Player getTheOwner() {
		return theOwner;
	}
	
	/**
	 * Returns the price of this cell, defaulting to 0.
	 * @return The price of the cell, which is 0 by default.
	 */
	/**
	 * Returns the price of this cell, defaulting to 0.
	 * @return The price of the cell, which is 0 by default.
	 */
	public int getPrice() {
		return 0;
	}

	/**
	 * Checks if this cell is currently available.
	 * @return True if the cell is available; false otherwise.
	 */
	/**
	 * Checks if this cell is currently available.
	 * @return True if the cell is available; false otherwise.
	 */
	public boolean isAvailable() {
		return available;
	}
	
	/**
	 * Performs the action associated with landing on this cell.
	 */
	/**
	 * Performs the action associated with landing on this cell.
	 */
	public abstract void playAction();

	/**
	 * Sets the availability status of this cell.
	 * @param available The new availability status to set.
	 */
	/**
	 * Sets the availability status of this cell.
	 * @param available The new availability status to set.
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	/**
	 * Sets the name of this cell.
	 * @param name The new name to assign to the cell.
	 */
	/**
	 * Sets the name of this cell.
	 * @param name The new name to assign to the cell.
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Assigns ownership of this cell to a player.
	 * @param owner The player to set as the owner.
	 */
	/**
	 * Assigns ownership of this cell to a player.
	 * @param owner The player to set as the owner.
	 */
	public void setTheOwner(Player owner) {
		this.theOwner = owner;
	}
    
    /**
     * Returns a string representation of this cell.
     * @return The name of the cell as a string.
     */
    /**
     * Returns a string representation of this cell.
     * @return The name of the cell as a string.
     */
    public String toString() {
        return name;
    }
}
