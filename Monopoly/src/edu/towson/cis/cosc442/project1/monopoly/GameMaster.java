package edu.towson.cis.cosc442.project1.monopoly;

import java.util.ArrayList;
import java.util.Iterator;


public class GameMaster {

	private static GameMaster gameMaster;
	static final public int MAX_PLAYER = 8;	
	private Die[] dice;
	private GameBoard gameBoard;
	private MonopolyGUI gui;
	private int initAmountOfMoney;
	private ArrayList<Player> players = new ArrayList<Player>();
	private int turn = 0;
	private int utilDiceRoll;
	private boolean testMode;

	/**
	 * Returns the singleton instance of the GameMaster class, creating it if it does not exist.
	 * @return The singleton GameMaster instance.
	 */
	/**
	 * Returns the singleton instance of the GameMaster class, creating it if it does not exist.
	 * @return The singleton GameMaster instance.
	 */
	public static GameMaster instance() {
		if(gameMaster == null) {
			gameMaster = new GameMaster();
		}
		return gameMaster;
	}

	/**
	 * Constructs a new GameMaster with default initial amount of money and two dice.
	 */
	/**
	 * Constructs a new GameMaster with default initial amount of money and two dice.
	 */
	public GameMaster() {
		initAmountOfMoney = 1500;
		dice = new Die[]{new Die(), new Die()};
	}

    /**
     * Handles the event when the buy house button is clicked by showing the buy house dialog for the current player.
     */
    /**
     * Handles the event when the buy house button is clicked by showing the buy house dialog for the current player.
     */
    public void btnBuyHouseClicked() {
        gui.showBuyHouseDialog(getCurrentPlayer());
    }

    /**
     * Processes the draw card button click by drawing a Community Chest or Chance card, applying its action, and enabling end turn.
     * @return The drawn Card object.
     */
    /**
     * Processes the draw card button click by drawing a Community Chest or Chance card, applying its action, and enabling end turn.
     * @return The drawn Card object.
     */
    public Card btnDrawCardClicked() {
        gui.setDrawCardEnabled(false);
        CardCell cell = (CardCell)getCurrentPlayer().getPosition();
        Card card = null;
        if(cell.getType() == Card.TYPE_CC) {
            card = getGameBoard().drawCCCard();
            card.applyAction();
        } else {
            card = getGameBoard().drawChanceCard();
            card.applyAction();
        }
        gui.setEndTurnEnabled(true);
        return card;
    }

    /**
     * Handles the end turn button click by disabling buttons, executing current player's position action, and switching turn if not bankrupt.
     */
    /**
     * Handles the end turn button click by disabling buttons, executing current player's position action, and switching turn if not bankrupt.
     */
    public void btnEndTurnClicked() {
		setAllButtonEnabled(false);
		getCurrentPlayer().getPosition().playAction();
		if(getCurrentPlayer().isBankrupt()) {
			gui.setBuyHouseEnabled(false);
			gui.setDrawCardEnabled(false);
			gui.setEndTurnEnabled(false);
			gui.setGetOutOfJailEnabled(false);
			gui.setPurchasePropertyEnabled(false);
			gui.setRollDiceEnabled(false);
			gui.setTradeEnabled(getCurrentPlayerIndex(),false);
			updateGUI();
		}
		else {
			switchTurn();
			updateGUI();
		}
    }

    /**
     * Processes the get out of jail button click, updates player's jail status, and adjusts button availability accordingly.
     */
    /**
     * Processes the get out of jail button click, updates player's jail status, and adjusts button availability accordingly.
     */
    public void btnGetOutOfJailClicked() {
		getCurrentPlayer().getOutOfJail();
		if(getCurrentPlayer().isBankrupt()) {
			gui.setBuyHouseEnabled(false);
			gui.setDrawCardEnabled(false);
			gui.setEndTurnEnabled(false);
			gui.setGetOutOfJailEnabled(false);
			gui.setPurchasePropertyEnabled(false);
			gui.setRollDiceEnabled(false);
			gui.setTradeEnabled(getCurrentPlayerIndex(),false);
		}
		else {
			gui.setRollDiceEnabled(true);
			gui.setBuyHouseEnabled(getCurrentPlayer().canBuyHouse());
			gui.setGetOutOfJailEnabled(getCurrentPlayer().isInJail());
		}
    }

    /**
     * Handles the purchase property button click by making the current player purchase the property and updating the GUI.
     */
    /**
     * Handles the purchase property button click by making the current player purchase the property and updating the GUI.
     */
    public void btnPurchasePropertyClicked() {
        Player player = getCurrentPlayer();
		player.purchase();
		gui.setPurchasePropertyEnabled(false);
		updateGUI();
    }
    
    /**
     * Responds to the roll dice button click by rolling dice, moving the current player, and showing the dice results.
     */
    /**
     * Responds to the roll dice button click by rolling dice, moving the current player, and showing the dice results.
     */
    public void btnRollDiceClicked() {
		int[] rolls = rollDice();
		if((rolls[0]+rolls[1]) > 0) {
			Player player = getCurrentPlayer();
			gui.setRollDiceEnabled(false);
			StringBuffer msg = new StringBuffer();
			msg.append(player.getName())
					.append(", you rolled ")
					.append(rolls[0])
					.append(" and ")
					.append(rolls[1]);
			gui.showMessage(msg.toString());
			movePlayer(player, rolls[0] + rolls[1]);
			gui.setBuyHouseEnabled(false);
		}
    }

    /**
     * Manages the trade button click by opening trade dialogs and completing the trade if accepted.
     */
    /**
     * Manages the trade button click by opening trade dialogs and completing the trade if accepted.
     */
    public void btnTradeClicked() {
        TradeDialog dialog = gui.openTradeDialog();
        TradeDeal deal = dialog.getTradeDeal();
        if(deal != null) {
            RespondDialog rDialog = gui.openRespondDialog(deal);
            if(rDialog.getResponse()) {
                completeTrade(deal);
                updateGUI();
            }
        }
    }

    /**
     * Completes a trade between the current player and another player based on the given trade deal by transferring property and money.
     * @param deal The trade deal detailing players, property, and amount involved in the trade.
     */
    /**
     * Completes a trade between the current player and another player based on the given trade deal by transferring property and money.
     * @param deal The trade deal detailing players, property, and amount involved in the trade.
     */
    public void completeTrade(TradeDeal deal) {
        Player seller = getPlayer(deal.getPlayerIndex());
        Cell property = gameBoard.queryCell(deal.getPropertyName());
        seller.sellProperty(property, deal.getAmount());
        getCurrentPlayer().buyProperty(property, deal.getAmount());
    }

    /**
     * Draws and returns a Community Chest card from the game board.
     * @return The drawn Community Chest Card.
     */
    /**
     * Draws and returns a Community Chest card from the game board.
     * @return The drawn Community Chest Card.
     */
    public Card drawCCCard() {
        return gameBoard.drawCCCard();
    }

    /**
     * Draws and returns a Chance card from the game board.
     * @return The drawn Chance Card.
     */
    /**
     * Draws and returns a Chance card from the game board.
     * @return The drawn Chance Card.
     */
    public Card drawChanceCard() {
        return gameBoard.drawChanceCard();
    }

	
	/**
	 * Gets the player whose turn it currently is in the game.
	 * @return The current Player object.
	 */
	/**
	 * Gets the player whose turn it currently is in the game.
	 * @return The current Player object.
	 */
	public Player getCurrentPlayer() {
		return getPlayer(turn);
	}
    
    /**
     * Returns the index of the current player whose turn it is.
     * @return The current player's index.
     */
    /**
     * Returns the index of the current player whose turn it is.
     * @return The current player's index.
     */
    public int getCurrentPlayerIndex() {
        return turn;
    }

	/**
	 * Retrieves the current game board being used for the game.
	 * @return The GameBoard instance.
	 */
	/**
	 * Retrieves the current game board being used for the game.
	 * @return The GameBoard instance.
	 */
	public GameBoard getGameBoard() {
		return gameBoard;
	}

    /**
     * Returns the GUI associated with the Monopoly game master.
     * @return The MonopolyGUI instance.
     */
    /**
     * Returns the GUI associated with the Monopoly game master.
     * @return The MonopolyGUI instance.
     */
    public MonopolyGUI getGUI() {
        return gui;
    }

	/**
	 * Gets the initial amount of money assigned to each player at the start of the game.
	 * @return The initial amount of money.
	 */
	/**
	 * Gets the initial amount of money assigned to each player at the start of the game.
	 * @return The initial amount of money.
	 */
	public int getInitAmountOfMoney() {
		return initAmountOfMoney;
	}
	
	/**
	 * Returns the total number of players currently in the game.
	 * @return The number of players.
	 */
	/**
	 * Returns the total number of players currently in the game.
	 * @return The number of players.
	 */
	public int getNumberOfPlayers() {
		return players.size();
	}

    /**
     * Returns the number of players who can potentially sell properties, excluding the current player.
     * @return The number of possible sellers (players excluding current).
     */
    /**
     * Returns the number of players who can potentially sell properties, excluding the current player.
     * @return The number of possible sellers (players excluding current).
     */
    public int getNumberOfSellers() {
        return players.size() - 1;
    }

	/**
	 * Retrieves the player at the specified index in the player list.
	 * @param index The index of the player to retrieve.
	 * @return The Player object at the given index.
	 */
	/**
	 * Retrieves the player at the specified index in the player list.
	 * @param index The index of the player to retrieve.
	 * @return The Player object at the given index.
	 */
	public Player getPlayer(int index) {
		return (Player)players.get(index);
	}
	
	/**
	 * Returns the index of the specified player in the player list.
	 * @param player The Player object whose index is to be found.
	 * @return The index of the player, or -1 if not found.
	 */
	/**
	 * Returns the index of the specified player in the player list.
	 * @param player The Player object whose index is to be found.
	 * @return The index of the player, or -1 if not found.
	 */
	public int getPlayerIndex(Player player) {
		return players.indexOf(player);
	}

    /**
     * Provides a list of all players other than the current player, representing potential sellers in trades.
     * @return An ArrayList of Player objects excluding the current player.
     */
    /**
     * Provides a list of all players other than the current player, representing potential sellers in trades.
     * @return An ArrayList of Player objects excluding the current player.
     */
    public ArrayList<Player> getSellerList() {
        ArrayList<Player> sellers = new ArrayList<Player>();
        for (Iterator<Player> iter = players.iterator(); iter.hasNext();) {
            Player player = (Player) iter.next();
            if(player != getCurrentPlayer()) sellers.add(player);
        }
        return sellers;
    }

	/**
	 * Returns the current turn index indicating which player's turn it is.
	 * @return The current turn index.
	 */
	/**
	 * Returns the current turn index indicating which player's turn it is.
	 * @return The current turn index.
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * Retrieves the utility dice roll value used for special game events.
	 * @return The utility dice roll integer value.
	 */
	/**
	 * Retrieves the utility dice roll value used for special game events.
	 * @return The utility dice roll integer value.
	 */
	public int getUtilDiceRoll() {
		return this.utilDiceRoll;
	}

	/**
	 * Moves the player at the specified index forward by the given dice value, updating position and GUI accordingly.
	 * @param playerIndex The index of the player to move.
	 * @param diceValue The number of steps to move the player forward.
	 */
	/**
	 * Moves the player at the specified index forward by the given dice value, updating position and GUI accordingly.
	 * @param playerIndex The index of the player to move.
	 * @param diceValue The number of steps to move the player forward.
	 */
	public void movePlayer(int playerIndex, int diceValue) {
		Player player = (Player)players.get(playerIndex);
		movePlayer(player, diceValue);
	}
	
	/**
	 * Moves the specified player forward by the given dice value, handling passing Go and updating the GUI.
	 * @param player The Player object to move.
	 * @param diceValue The number of cells to advance the player.
	 */
	/**
	 * Moves the specified player forward by the given dice value, handling passing Go and updating the GUI.
	 * @param player The Player object to move.
	 * @param diceValue The number of cells to advance the player.
	 */
	public void movePlayer(Player player, int diceValue) {
		Cell currentPosition = player.getPosition();
		int positionIndex = gameBoard.queryCellIndex(currentPosition.getName());
		int newIndex = (positionIndex+diceValue)%gameBoard.getCellNumber();
		if(newIndex <= positionIndex || diceValue > gameBoard.getCellNumber()) {
			player.setMoney(player.getMoney() + 200);
		}
		player.setPosition(gameBoard.getCell(newIndex));
		gui.movePlayer(getPlayerIndex(player), positionIndex, newIndex);
		playerMoved(player);
		updateGUI();
	}

	/**
	 * Handles actions after a player has moved, enabling appropriate GUI buttons based on the player's new cell.
	 * @param player The Player who just moved.
	 */
	/**
	 * Handles actions after a player has moved, enabling appropriate GUI buttons based on the player's new cell.
	 * @param player The Player who just moved.
	 */
	public void playerMoved(Player player) {
		Cell cell = player.getPosition();
		int playerIndex = getPlayerIndex(player);
		if(cell instanceof CardCell) {
		    gui.setDrawCardEnabled(true);
		} else{
			if(cell.isAvailable()) {
				int price = cell.getPrice();
				if(price <= player.getMoney() && price > 0) {
					gui.enablePurchaseBtn(playerIndex);
				}
			}	
			gui.enableEndTurnBtn(playerIndex);
		}
        gui.setTradeEnabled(turn, false);
	}

	/**
	 * Resets the game state by returning all players to start, clearing cards, and resetting the turn counter.
	 */
	/**
	 * Resets the game state by returning all players to start, clearing cards, and resetting the turn counter.
	 */
	public void reset() {
		for(int i = 0; i < getNumberOfPlayers(); i++){
			Player player = (Player)players.get(i);
			player.setPosition(gameBoard.getCell(0));
		}
		if(gameBoard != null) gameBoard.removeCards();
		turn = 0;
	}
	
	/**
	 * Rolls the dice once, either through test mode inputs or randomization, and returns the results.
	 * @return An array of two integers representing the dice roll results.
	 */
	/**
	 * Rolls the dice once, either through test mode inputs or randomization, and returns the results.
	 * @return An array of two integers representing the dice roll results.
	 */
	public int[] rollDice() {
		if(testMode) {
			return gui.getDiceRoll();
		}
		else {
			return new int[]{
					dice[0].getRoll(),
					dice[1].getRoll()
			};
		}
	}
	
	/**
	 * Sends the specified player to jail, updating their position and status in the GUI.
	 * @param player The Player to send to jail.
	 */
	/**
	 * Sends the specified player to jail, updating their position and status in the GUI.
	 * @param player The Player to send to jail.
	 */
	public void sendToJail(Player player) {
	    int oldPosition = gameBoard.queryCellIndex(getCurrentPlayer().getPosition().getName());
		player.setPosition(gameBoard.queryCell("Jail"));
		player.setInJail(true);
		int jailIndex = gameBoard.queryCellIndex("Jail");
		gui.movePlayer(
		        getPlayerIndex(player),
		        oldPosition,
		        jailIndex);
	}
    
	/**
	 * Enables or disables all main game control buttons in the GUI for the current turn.
	 * @param enabled True to enable all buttons; false to disable.
	 */
	/**
	 * Enables or disables all main game control buttons in the GUI for the current turn.
	 * @param enabled True to enable all buttons; false to disable.
	 */
	private void setAllButtonEnabled(boolean enabled) {
		gui.setRollDiceEnabled(enabled);
		gui.setPurchasePropertyEnabled(enabled);
		gui.setEndTurnEnabled(enabled);
        gui.setTradeEnabled(turn, enabled);
        gui.setBuyHouseEnabled(enabled);
        gui.setDrawCardEnabled(enabled);
        gui.setGetOutOfJailEnabled(enabled);
	}

	/**
	 * Sets the game board reference used by the game master for game state and logic.
	 * @param board The GameBoard instance to set.
	 */
	/**
	 * Sets the game board reference used by the game master for game state and logic.
	 * @param board The GameBoard instance to set.
	 */
	public void setGameBoard(GameBoard board) {
		this.gameBoard = board;
	}
	
	/**
	 * Assigns the GUI interface object to be used for user interaction and display.
	 * @param gui The MonopolyGUI object to assign.
	 */
	/**
	 * Assigns the GUI interface object to be used for user interaction and display.
	 * @param gui The MonopolyGUI object to assign.
	 */
	public void setGUI(MonopolyGUI gui) {
		this.gui = gui;
	}

	/**
	 * Sets the initial amount of money that all players start with at the game beginning.
	 * @param money The amount of money to assign as initial for each player.
	 */
	/**
	 * Sets the initial amount of money that all players start with at the game beginning.
	 * @param money The amount of money to assign as initial for each player.
	 */
	public void setInitAmountOfMoney(int money) {
		this.initAmountOfMoney = money;
	}

	/**
	 * Initializes the player list with the given number of players, setting their starting money.
	 * @param number The total number of players to create.
	 */
	/**
	 * Initializes the player list with the given number of players, setting their starting money.
	 * @param number The total number of players to create.
	 */
	public void setNumberOfPlayers(int number) {
		players.clear();
		for(int i =0;i<number;i++) {
			Player player = new Player();
			player.setMoney(initAmountOfMoney);
			players.add(player);
		}
	}

	/**
	 * Updates the utility dice roll value used for special game mechanics or testing.
	 * @param diceRoll The new dice roll value to assign.
	 */
	/**
	 * Updates the utility dice roll value used for special game mechanics or testing.
	 * @param diceRoll The new dice roll value to assign.
	 */
	public void setUtilDiceRoll(int diceRoll) {
		this.utilDiceRoll = diceRoll;
	}
	
	/**
	 * Starts the Monopoly game by initializing the GUI for the first player's turn and enabling trade options.
	 */
	/**
	 * Starts the Monopoly game by initializing the GUI for the first player's turn and enabling trade options.
	 */
	public void startGame() {
		gui.startGame();
		gui.enablePlayerTurn(0);
        gui.setTradeEnabled(0, true);
	}

	/**
	 * Advances to the next player's turn, enabling appropriate GUI controls depending on jail status.
	 */
	/**
	 * Advances to the next player's turn, enabling appropriate GUI controls depending on jail status.
	 */
	public void switchTurn() {
		turn = (turn + 1) % getNumberOfPlayers();
		if(!getCurrentPlayer().isInJail()) {
			gui.enablePlayerTurn(turn);
			gui.setBuyHouseEnabled(getCurrentPlayer().canBuyHouse());
            gui.setTradeEnabled(turn, true);
		}
		else {
			gui.setGetOutOfJailEnabled(true);
		}
	}
	
	/**
	 * Requests the GUI to update its display to reflect the current game state.
	 */
	/**
	 * Requests the GUI to update its display to reflect the current game state.
	 */
	public void updateGUI() {
		gui.update();
	}

	/**
	 * Obtains a utility dice roll value by prompting the user through the GUI and stores the result.
	 */
	/**
	 * Obtains a utility dice roll value by prompting the user through the GUI and stores the result.
	 */
	public void utilRollDice() {
		this.utilDiceRoll = gui.showUtilDiceRoll();
	}

	/**
	 * Sets whether the game is in test mode, affecting dice roll generation.
	 * @param b True to enable test mode; false to disable.
	 */
	/**
	 * Sets whether the game is in test mode, affecting dice roll generation.
	 * @param b True to enable test mode; false to disable.
	 */
	public void setTestMode(boolean b) {
		testMode = b;
	}
}
