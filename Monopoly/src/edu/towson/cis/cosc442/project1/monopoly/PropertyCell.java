package edu.towson.cis.cosc442.project1.monopoly;

public class PropertyCell extends Cell {
	private String colorGroup;
	private int housePrice;
	private int numHouses;
	private int rent;
	private int sellPrice;

	/**
	 * Returns the color group to which this property belongs.
	 * @return The color group of this property as a String.
	 */
	/**
	 * Returns the color group to which this property belongs.
	 * @return The color group of this property as a String.
	 */
	public String getColorGroup() {
		return colorGroup;
	}

	/**
	 * Returns the price required to build a house on this property.
	 * @return The cost of building one house on the property.
	 */
	/**
	 * Returns the price required to build a house on this property.
	 * @return The cost of building one house on the property.
	 */
	public int getHousePrice() {
		return housePrice;
	}

	/**
	 * Returns the current number of houses built on this property.
	 * @return The number of houses on the property.
	 */
	/**
	 * Returns the current number of houses built on this property.
	 * @return The number of houses on the property.
	 */
	public int getNumHouses() {
		return numHouses;
	}
    @Override 
    /**
     * Returns the selling price of the property.
     * @return The sell price of the property.
     */
    /**
     * Returns the selling price of the property.
     * @return The sell price of the property.
     */
    public int getPrice() {
		return sellPrice;
	}

	/**
	 * Calculates and returns the rent amount owed when another player lands on this property.
	 * @return The current rent amount based on ownership and number of houses.
	 */
	/**
	 * Calculates and returns the rent amount owed when another player lands on this property.
	 * @return The current rent amount based on ownership and number of houses.
	 */
	public int getRent() {
		int rentToCharge = rent;
		String [] monopolies = theOwner.getMonopolies();
		rentToCharge = calculateMonopoliesRent(rentToCharge, monopolies);
		if(numHouses > 0) {
			rentToCharge = rent * (numHouses + 1);
		}
		return rentToCharge;
	}

	/**
	 * Calculates the rent considering any monopolies owned by the property owner, doubling rent if monopoly colors match.
	 * @param rentToCharge The base rent to charge before monopoly adjustment.
	 * @param monopolies An array of color groups representing monopolies owned by the property owner.
	 * @return The adjusted rent accounting for monopoly ownership.
	 */
	/**
	 * Calculates the rent considering any monopolies owned by the property owner, doubling rent if monopoly colors match.
	 * @param rentToCharge The base rent to charge before monopoly adjustment.
	 * @param monopolies An array of color groups representing monopolies owned by the property owner.
	 * @return The adjusted rent accounting for monopoly ownership.
	 */
	private int calculateMonopoliesRent(int rentToCharge, String[] monopolies) {
		for(int i = 0; i < monopolies.length; i++) {
			if(monopolies[i].equals(colorGroup)) {
				rentToCharge = rent * 2;
			}
		}
		return rentToCharge;
	}

	/**
	 * Performs the action required when a player lands on this property cell, including paying rent if applicable.
	 */
	/**
	 * Performs the action required when a player lands on this property cell, including paying rent if applicable.
	 */
	public void playAction() {
		Player currentPlayer = null;
		if(!isAvailable()) {
			currentPlayer = GameMaster.instance().getCurrentPlayer();
			if(theOwner != currentPlayer) {
				currentPlayer.payRentTo(theOwner, getRent());
			}
		}
	}

	/**
	 * Sets the color group classification for this property.
	 * @param colorGroup The color group to assign to this property.
	 */
	/**
	 * Sets the color group classification for this property.
	 * @param colorGroup The color group to assign to this property.
	 */
	public void setColorGroup(String colorGroup) {
		this.colorGroup = colorGroup;
	}

	/**
	 * Sets the cost required to build a house on this property.
	 * @param housePrice The house building price to set.
	 */
	/**
	 * Sets the cost required to build a house on this property.
	 * @param housePrice The house building price to set.
	 */
	public void setHousePrice(int housePrice) {
		this.housePrice = housePrice;
	}

	/**
	 * Sets the current number of houses built on this property.
	 * @param numHouses The number of houses to assign to this property.
	 */
	/**
	 * Sets the current number of houses built on this property.
	 * @param numHouses The number of houses to assign to this property.
	 */
	public void setNumHouses(int numHouses) {
		this.numHouses = numHouses;
	}

	/**
	 * Sets the selling price for this property.
	 * @param sellPrice The selling price to set for the property.
	 */
	/**
	 * Sets the selling price for this property.
	 * @param sellPrice The selling price to set for the property.
	 */
	public void setPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * Sets the base rent amount for this property.
	 * @param rent The rent amount to assign.
	 */
	/**
	 * Sets the base rent amount for this property.
	 * @param rent The rent amount to assign.
	 */
	public void setRent(int rent) {
		this.rent = rent;
	}
}
