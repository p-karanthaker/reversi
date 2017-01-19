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
 * Created by Sam on 09/10/2016.
 */
public abstract class AbstractGame extends Observable {

  private GameState gameState;
  protected Board playingBoard;
  private AbstractPlayer[] players;
  protected GameType gameType;
  private boolean hardDifficulty;
  private boolean timedGame;
  private int currentPlayer;
  private int totalTimePerPlayerInSeconds = 1;

  public AbstractGame(GameType gameType, boolean hardDifficulty) {
    this.players = new AbstractPlayer[2];
    this.gameType = gameType;
    this.playingBoard = new Board();
    this.currentPlayer = 0;
    this.timedGame = false;
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
      new IllegalStateException("Not enough players!");
    } else if (players[0].getPlayerColour().equals(players[1].getPlayerColour())) {
      new IllegalStateException("Players cannot be of same colour!");
    }
  }

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

  public AbstractPlayer[] getPlayers() {
    return this.players;
  }

  public Board getBoard() {
    return this.playingBoard;
  }

  public GameType getGameType() {
    return this.gameType;
  }

  public boolean getDifficulty() {
    return this.hardDifficulty;
  }

  public GameState getGameState() {
    return this.gameState;
  }

  public void setGameState(GameState newState) {
    this.gameState = newState;
  }

  public boolean isTimedGame() {
    return this.timedGame;
  }

  public int getTotalTimePerPlayerInSeconds() {
    return this.totalTimePerPlayerInSeconds;
  }

  public boolean isGameOver() {
    return this.gameState.equals(GameState.GAVE_OVER);
  }

  public abstract Piece.PieceColour determineWinner();

  public boolean playerTurn(int coordinate) {
    Point point = Board.translateIndexToPoint(coordinate);
    //if the array calculated is vacant
    if (Move.makeMove(this, point.y, point.x)) {
      this.playingBoard.addPiece(coordinate, this.getCurrentPlayer().getPlayerColour());
      return true;
    }
    return false;
  }

  public void update() {
    //notify observer of the changes
    setChanged();
    notifyObservers(this.getGameState());
  }

  public enum GameState {
    IN_PROGRESS, GAVE_OVER, ABORTED
  }

}
