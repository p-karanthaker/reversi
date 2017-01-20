package uk.ac.aston.dc2060.group5.reversi.rulesets;

import uk.ac.aston.dc2060.group5.reversi.model.Board;
import uk.ac.aston.dc2060.group5.reversi.model.Move;
import uk.ac.aston.dc2060.group5.reversi.model.Piece;
import uk.ac.aston.dc2060.group5.reversi.players.AbstractPlayer;
import uk.ac.aston.dc2060.group5.reversi.players.CPUPlayer;
import uk.ac.aston.dc2060.group5.reversi.players.HumanPlayer;

import java.awt.Point;
import java.util.Observable;

/**
 * Models the blueprint of a game. Extends the Observable class so it can be used in an MVC pattern.
 *
 * <p>Created by Karan Thaker.</p>
 */
public abstract class AbstractGame extends Observable {

  /**
   * The state of the game.
   */
  private GameState gameState;

  /**
   * The board used in the game.
   */
  private Board playingBoard;

  /**
   * An array of players in the game.
   */
  private AbstractPlayer[] players;

  /**
   * The type of game.
   */
  private GameType gameType;

  /**
   * If the game is set to hard mode.
   */
  private boolean hardDifficulty;

  /**
   * If the game is a timed game.
   */
  private boolean timedGame;

  /**
   * The index of the current player in the players array.
   */
  private int currentPlayer;

  /**
   * The total time per player in seconds if the game is timed.
   */
  private int totalTimePerPlayerInSeconds = 1;

  /**
   * Super constructor for a game.
   *
   * @param gameType       the type of game to be played.
   * @param hardDifficulty whether the game will be hard mode when against AI.
   */
  public AbstractGame(GameType gameType, boolean hardDifficulty) {
    this.players = new AbstractPlayer[2];
    this.gameType = gameType;
    this.playingBoard = new Board();
    this.currentPlayer = 0;
    this.timedGame = false;
    this.hardDifficulty = hardDifficulty;
    this.gameState = GameState.IN_PROGRESS;

    if (this.gameType.equals(GameType.PVP)) {
      this.players[0] = new HumanPlayer(Piece.PieceColour.BLACK);
      this.players[1] = new HumanPlayer(Piece.PieceColour.WHITE);
    } else if (this.gameType.equals(GameType.PVC)) {
      this.players[0] = new HumanPlayer(Piece.PieceColour.BLACK);
      this.players[1] = new CPUPlayer(Piece.PieceColour.WHITE, hardDifficulty);
    } else if (this.gameType.equals(GameType.DEMO)) {
      this.players[0] = new CPUPlayer(Piece.PieceColour.BLACK, false);
      this.players[1] = new CPUPlayer(Piece.PieceColour.WHITE, false);
    }

    if (players.length != 2) {
      throw new IllegalStateException("Not enough players!");
    } else if (players[0].getPlayerColour().equals(players[1].getPlayerColour())) {
      throw new IllegalStateException("Players cannot be of same colour!");
    }
  }

  /**
   * Overloaded super constructor for a timed game.
   *
   * @param gameType                    the type of game to be played.
   * @param hardDifficulty              whether the game is hard against AI.
   * @param totalTimePerPlayerInSeconds the total time each player will have to play in seconds.
   */
  public AbstractGame(GameType gameType, boolean hardDifficulty, int totalTimePerPlayerInSeconds) {
    this(gameType, hardDifficulty);
    this.timedGame = true;
    this.totalTimePerPlayerInSeconds = totalTimePerPlayerInSeconds;
    this.players[0].setTimeLeftToPlayInSeconds(totalTimePerPlayerInSeconds);
    this.players[1].setTimeLeftToPlayInSeconds(totalTimePerPlayerInSeconds);
  }

  /**
   * Switches the current player.
   */
  public void switchPlayer() {
    this.currentPlayer = this.currentPlayer == 0 ? 1 : 0;
  }

  /**
   * Retrieves the current player from the player array.
   *
   * @return the current player.
   */
  public AbstractPlayer getCurrentPlayer() {
    return this.players[this.currentPlayer];
  }

  /**
   * Returns an array of the players in the game.
   *
   * @return an array of the players in the game.
   */
  public AbstractPlayer[] getPlayers() {
    return this.players;
  }

  /**
   * Get the board being played on.
   *
   * @return the playing board.
   */
  public Board getBoard() {
    return this.playingBoard;
  }

  /**
   * Get the type of game being played.
   *
   * @return the game type.
   */
  public GameType getGameType() {
    return this.gameType;
  }

  /**
   * Return if the game is being played on hard mode vs AI.
   *
   * @return whether the game is set to hard mode.
   */
  public boolean getDifficulty() {
    return this.hardDifficulty;
  }

  /**
   * Get the state of the game.
   *
   * @return wheter the game is in progress, aborted, or over.
   */
  public GameState getGameState() {
    return this.gameState;
  }

  /**
   * Set the game state.
   *
   * @param newState the new state to set the game to.
   */
  public void setGameState(GameState newState) {
    this.gameState = newState;
  }

  /**
   * Is the game a timed game?
   *
   * @return whether or not the game is timed.
   */
  public boolean isTimedGame() {
    return this.timedGame;
  }

  /**
   * Get the total time each player has to play in seconds.
   *
   * @return the total time per player in seconds.
   */
  public int getTotalTimePerPlayerInSeconds() {
    return this.totalTimePerPlayerInSeconds;
  }

  /**
   * Deterimes the winner of the game.
   *
   * @return the piece colour which won the game.
   */
  public abstract Piece.PieceColour determineWinner();

  /**
   * Allows the player to take a turn.
   *
   * @param coordinate the coordinate to add a piece to.
   * @return true if the move was successful.
   */
  public boolean playerTurn(int coordinate) {
    Point point = Board.translateIndexToPoint(coordinate);
    //if the array calculated is vacant
    if (Move.makeMove(this, point.y, point.x)) {
      this.playingBoard.addPiece(coordinate, this.getCurrentPlayer().getPlayerColour());
      return true;
    }
    return false;
  }

  /**
   * Notify the observer of changes to the game.
   */
  public void update() {
    setChanged();
    notifyObservers(this.getGameState());
  }

  /**
   * The state of the game.
   */
  public enum GameState {
    /**
     * The game is in progress.
     */
    IN_PROGRESS,

    /**
     * The game is finished.
     */
    GAVE_OVER,

    /**
     * The game was aborted.
     */
    ABORTED
  }

}
