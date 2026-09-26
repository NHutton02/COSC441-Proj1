package edu.towson.cis.cosc442.project1.monopoly;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


public class Player {
	//the key of colorGroups is the name of the color group.
	private Hashtable<String, Integer> colorGroups = new Hashtable<String, Integer>();
	private boolean inJail;
	private int money;
	private String name;

	private Cell position;
	private ArrayList<PropertyCell> properties = new ArrayList<PropertyCell>();
	private ArrayList<Cell> railroads = new ArrayList<Cell>();
	private ArrayList<Cell> utilities = new ArrayList<Cell>();
	
	/**
	 * Initializes a Player object starting at the 'Go' cell with default settings.
	 */
	/**
	 * Initializes a Player object starting at the 'Go' cell with default settings.
	 */
	public Player() {
		GameBoard gb = GameMaster.instance().getGameBoard();
		inJail = false;
		if(gb != null) {
			position = gb.queryCell("Go");
		}
	}

    /**
     * Assigns ownership of a property to the player, adds it to their property lists, and deducts the purchase amount from their money.
     * @param property The property cell to be purchased.
     * @param amount The price paid for the property.
     */
    /**
     * Assigns ownership of a property to the player, adds it to their property lists, and deducts the purchase amount from their money.
     * @param property The property cell to be purchased.
     * @param amount The price paid for the property.
     */
    public void buyProperty(Cell property, int amount) {
        property.setTheOwner(this);
        if(property instanceof PropertyCell) {
            PropertyCell cell = (PropertyCell)property;
            properties.add(cell);
            colorGroups.put(
                    cell.getColorGroup(), 
                    new Integer(getPropertyNumberForColor(cell.getColorGroup())+1));
        }
        if(property instanceof RailRoadCell) {
            railroads.add(property);
            colorGroups.put(
                    RailRoadCell.COLOR_GROUP, 
                    new Integer(getPropertyNumberForColor(RailRoadCell.COLOR_GROUP)+1));
        }
        if(property instanceof UtilityCell) {
            utilities.add(property);
            colorGroups.put(
                    UtilityCell.COLOR_GROUP, 
                    new Integer(getPropertyNumberForColor(UtilityCell.COLOR_GROUP)+1));
        }
        setMoney(getMoney() - amount);
    }
	
	/**
	 * Determines if the player currently owns any monopolies allowing house purchases.
	 * @return true if the player has at least one monopoly, false otherwise.
	 */
	/**
	 * Determines if the player currently owns any monopolies allowing house purchases.
	 * @return true if the player has at least one monopoly, false otherwise.
	 */
	public boolean canBuyHouse() {
		return (getMonopolies().length != 0);
	}

	/**
	 * Checks whether the player owns a property with the specified name.
	 * @param property The name of the property to check ownership for.
	 * @return true if player owns the property, false otherwise.
	 */
	/**
	 * Checks whether the player owns a property with the specified name.
	 * @param property The name of the property to check ownership for.
	 * @return true if player owns the property, false otherwise.
	 */
	public boolean checkProperty(String property) {
		for(int i=0;i<properties.size();i++) {
			Cell cell = (Cell)properties.get(i);
			if(cell.getName().equals(property)) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Transfers all properties from this player to another player or releases them if null.
	 * @param player The player to receive the properties, or null to release them.
	 */
	/**
	 * Transfers all properties from this player to another player or releases them if null.
	 * @param player The player to receive the properties, or null to release them.
	 */
	public void exchangeProperty(Player player) {
		for(int i = 0; i < getPropertyNumber(); i++ ) {
			PropertyCell cell = getProperty(i);
			cell.setTheOwner(player);
			if(player == null) {
				cell.setAvailable(true);
				cell.setNumHouses(0);
			}
			else {
				player.properties.add(cell);
				colorGroups.put(
						cell.getColorGroup(), 
						new Integer(getPropertyNumberForColor(cell.getColorGroup())+1));
			}
		}
		properties.clear();
	}
    
    /**
     * Retrieves an array of all cells owned by the player including properties, railroads, and utilities.
     * @return An array of all owned property cells.
     */
    /**
     * Retrieves an array of all cells owned by the player including properties, railroads, and utilities.
     * @return An array of all owned property cells.
     */
    public Cell[] getAllProperties() {
        ArrayList<Cell> list = new ArrayList<Cell>();
        list.addAll(properties);
        list.addAll(utilities);
        list.addAll(railroads);
        return (Cell[])list.toArray(new Cell[list.size()]);
    }

	/**
	 * Gets the current amount of money the player has.
	 * @return The player's current money balance.
	 */
	/**
	 * Gets the current amount of money the player has.
	 * @return The player's current money balance.
	 */
	public int getMoney() {
		return this.money;
	}
	
	/**
	 * Retrieves the color groups for which the player owns all properties, constituting monopolies.
	 * @return An array of monopoly color group names owned by the player.
	 */
	/**
	 * Retrieves the color groups for which the player owns all properties, constituting monopolies.
	 * @return An array of monopoly color group names owned by the player.
	 */
	public String[] getMonopolies() {
		ArrayList<String> monopolies = new ArrayList<String>();
		Enumeration<String> colors = colorGroups.keys();
		while(colors.hasMoreElements()) {
			String color = (String)colors.nextElement();
            if(!(color.equals(RailRoadCell.COLOR_GROUP)) && !(color.equals(UtilityCell.COLOR_GROUP))) {
    			Integer num = (Integer)colorGroups.get(color);
    			GameBoard gameBoard = GameMaster.instance().getGameBoard();
    			if(num.intValue() == gameBoard.getPropertyNumberForColor(color)) {
    				monopolies.add(color);
    			}
            }
		}
		return (String[])monopolies.toArray(new String[monopolies.size()]);
	}

	/**
	 * Returns the name of the player.
	 * @return The player's name.
	 */
	/**
	 * Returns the name of the player.
	 * @return The player's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Processes the player's bail payment to get out of jail and updates their status and properties if bankrupt.
	 */
	/**
	 * Processes the player's bail payment to get out of jail and updates their status and properties if bankrupt.
	 */
	public void getOutOfJail() {
		money -= JailCell.BAIL;
		if(isBankrupt()) {
			money = 0;
			exchangeProperty(null);
		}
		inJail = false;
		GameMaster.instance().updateGUI();
	}

	/**
	 * Returns the current position of the player on the game board.
	 * @return The cell object representing the player's position.
	 */
	/**
	 * Returns the current position of the player on the game board.
	 * @return The cell object representing the player's position.
	 */
	public Cell getPosition() {
		return this.position;
	}
	
	/**
	 * Retrieves a property owned by the player at a specific index.
	 * @param index The index of the property to retrieve.
	 * @return The property cell at the specified index.
	 */
	/**
	 * Retrieves a property owned by the player at a specific index.
	 * @param index The index of the property to retrieve.
	 * @return The property cell at the specified index.
	 */
	public PropertyCell getProperty(int index) {
		return (PropertyCell)properties.get(index);
	}
	
	/**
	 * Returns the total number of properties the player owns.
	 * @return The count of owned properties.
	 */
	/**
	 * Returns the total number of properties the player owns.
	 * @return The count of owned properties.
	 */
	public int getPropertyNumber() {
		return properties.size();
	}

	/**
	 * Returns the number of properties the player owns within a specific color group.
	 * @param name The color group name to count properties for.
	 * @return The count of properties owned in the specified color group.
	 */
	/**
	 * Returns the number of properties the player owns within a specific color group.
	 * @param name The color group name to count properties for.
	 * @return The count of properties owned in the specified color group.
	 */
	private int getPropertyNumberForColor(String name) {
		Integer number = (Integer)colorGroups.get(name);
		if(number != null) {
			return number.intValue();
		}
		return 0;
	}

	/**
	 * Checks whether the player is bankrupt based on their money balance.
	 * @return true if the player has zero or negative money, false otherwise.
	 */
	/**
	 * Checks whether the player is bankrupt based on their money balance.
	 * @return true if the player has zero or negative money, false otherwise.
	 */
	public boolean isBankrupt() {
		return money <= 0;
	}

	/**
	 * Determines if the player is currently in jail.
	 * @return true if the player is in jail, false otherwise.
	 */
	/**
	 * Determines if the player is currently in jail.
	 * @return true if the player is in jail, false otherwise.
	 */
	public boolean isInJail() {
		return inJail;
	}

	/**
	 * Returns the number of railroads owned by the player.
	 * @return The count of railroads owned.
	 */
	/**
	 * Returns the number of railroads owned by the player.
	 * @return The count of railroads owned.
	 */
	public int numberOfRR() {
		return getPropertyNumberForColor(RailRoadCell.COLOR_GROUP);
	}

	/**
	 * Returns the number of utilities owned by the player.
	 * @return The count of utilities owned.
	 */
	/**
	 * Returns the number of utilities owned by the player.
	 * @return The count of utilities owned.
	 */
	public int numberOfUtil() {
		return getPropertyNumberForColor(UtilityCell.COLOR_GROUP);
	}
	
	/**
	 * Pays rent to another player and updates ownership if this player goes bankrupt during payment.
	 * @param owner The player receiving the rent.
	 * @param rentValue The amount of rent to be paid.
	 */
	/**
	 * Pays rent to another player and updates ownership if this player goes bankrupt during payment.
	 * @param owner The player receiving the rent.
	 * @param rentValue The amount of rent to be paid.
	 */
	public void payRentTo(Player owner, int rentValue) {
		if(money < rentValue) {
			owner.money += money;
			money -= rentValue;
		}
		else {
			money -= rentValue;
			owner.money +=rentValue;
		}
		if(isBankrupt()) {
			money = 0;
			exchangeProperty(owner);
		}
	}
	
	/**
	 * Attempts to purchase the property at the player's current position if it is available.
	 */
	/**
	 * Attempts to purchase the property at the player's current position if it is available.
	 */
	public void purchase() {
		if(getPosition().isAvailable()) {
			Cell c = getPosition();
			c.setAvailable(false);
			if(c instanceof PropertyCell) {
				PropertyCell cell = (PropertyCell)c;
				purchaseProperty(cell);
			}
			if(c instanceof RailRoadCell) {
				RailRoadCell cell = (RailRoadCell)c;
				purchaseRailRoad(cell);
			}
			if(c instanceof UtilityCell) {
				UtilityCell cell = (UtilityCell)c;
				purchaseUtility(cell);
			}
		}
	}
	
	/**
	 * Purchases a specified number of houses for all properties in a selected monopoly if the player has sufficient funds.
	 * @param selectedMonopoly The color group of the monopoly to build houses on.
	 * @param houses The number of houses to purchase per property.
	 */
	/**
	 * Purchases a specified number of houses for all properties in a selected monopoly if the player has sufficient funds.
	 * @param selectedMonopoly The color group of the monopoly to build houses on.
	 * @param houses The number of houses to purchase per property.
	 */
	public void purchaseHouse(String selectedMonopoly, int houses) {
		GameBoard gb = GameMaster.instance().getGameBoard();
		PropertyCell[] cells = gb.getPropertiesInMonopoly(selectedMonopoly);
		if((money >= (cells.length * (cells[0].getHousePrice() * houses)))) {
			for(int i = 0; i < cells.length; i++) {
				int newNumber = cells[i].getNumHouses() + houses;
				if (newNumber <= 5) {
					cells[i].setNumHouses(newNumber);
					this.setMoney(money - (cells[i].getHousePrice() * houses));
					GameMaster.instance().updateGUI();
				}
			}
		}
	}
	
	/**
	 * Purchases a single property cell for the player using its listed price.
	 * @param cell The property cell to purchase.
	 */
	/**
	 * Purchases a single property cell for the player using its listed price.
	 * @param cell The property cell to purchase.
	 */
	private void purchaseProperty(PropertyCell cell) {
        buyProperty(cell, cell.getPrice());
	}

	/**
	 * Purchases a railroad property cell for the player at its price.
	 * @param cell The railroad cell to purchase.
	 */
	/**
	 * Purchases a railroad property cell for the player at its price.
	 * @param cell The railroad cell to purchase.
	 */
	private void purchaseRailRoad(RailRoadCell cell) {
	    buyProperty(cell, cell.getPrice());
	}

	/**
	 * Purchases a utility property cell for the player at its price.
	 * @param cell The utility cell to purchase.
	 */
	/**
	 * Purchases a utility property cell for the player at its price.
	 * @param cell The utility cell to purchase.
	 */
	private void purchaseUtility(UtilityCell cell) {
	    buyProperty(cell, cell.getPrice());
	}

    /**
     * Sells a property and removes ownership, adding the sale amount to the player's money.
     * @param property The property cell to sell.
     * @param amount The sale price received from the property.
     */
    /**
     * Sells a property and removes ownership, adding the sale amount to the player's money.
     * @param property The property cell to sell.
     * @param amount The sale price received from the property.
     */
    public void sellProperty(Cell property, int amount) {
        property.setTheOwner(null);
        if(property instanceof PropertyCell) {
            properties.remove(property);
        }
        if(property instanceof RailRoadCell) {
            railroads.remove(property);
        }
        if(property instanceof UtilityCell) {
            utilities.remove(property);
        }
        setMoney(getMoney() + amount);
    }

	/**
	 * Sets the player's jail status to in jail or not in jail.
	 * @param inJail True if the player is to be set as in jail, false otherwise.
	 */
	/**
	 * Sets the player's jail status to in jail or not in jail.
	 * @param inJail True if the player is to be set as in jail, false otherwise.
	 */
	public void setInJail(boolean inJail) {
		this.inJail = inJail;
	}

	/**
	 * Sets the player's money to the specified amount.
	 * @param money The new money amount for the player.
	 */
	/**
	 * Sets the player's money to the specified amount.
	 * @param money The new money amount for the player.
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * Sets the player's name to the specified string.
	 * @param name The new name for the player.
	 */
	/**
	 * Sets the player's name to the specified string.
	 * @param name The new name for the player.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Updates the player's position on the game board to the specified cell.
	 * @param newPosition The new position cell for the player.
	 */
	/**
	 * Updates the player's position on the game board to the specified cell.
	 * @param newPosition The new position cell for the player.
	 */
	public void setPosition(Cell newPosition) {
		this.position = newPosition;
	}

    /**
     * Returns the string representation of the player, which is their name.
     * @return The player's name as a string.
     */
    /**
     * Returns the string representation of the player, which is their name.
     * @return The player's name as a string.
     */
    public String toString() {
        return name;
    }
    
    /**
     * Clears all properties, railroads, and utilities currently owned by the player.
     */
    /**
     * Clears all properties, railroads, and utilities currently owned by the player.
     */
    public void resetProperty() {
    	properties = new ArrayList<PropertyCell>();
    	railroads = new ArrayList<Cell>();
    	utilities = new ArrayList<Cell>();
	}
}
