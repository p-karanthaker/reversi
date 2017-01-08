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
  private int currentPlayer;

  public AbstractGame(GameType gameType, boolean hardDifficulty) {
    this.players = new AbstractPlayer[2];
    this.gameType = gameType;
    this.playingBoard = new Board();
    this.currentPlayer = 0;
    this.gameState = GameState.IN_PROGRESS;

    if (this.gameType.equals(GameType.PVP)) {
      this.players[0] = new HumanPlayer(Piece.PieceColour.BLACK);
      this.players[1] = new HumanPlayer(Piece.PieceColour.WHITE);
    } else if (this.gameType.equals(GameType.PVC)){
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

  /**
   * Switches the current player.
   */
  public void switchPlayer() {
    this.currentPlayer = this.currentPlayer == 0 ? 1 : 0;
  }

  /**
   * Retrieves the current player from the player array.
   * @return the current player.
   */
  public AbstractPlayer getCurrentPlayer() {
    return this.players[this.currentPlayer];
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

  public boolean isGameOver() {
    if (this.gameState.equals(GameState.GAVE_OVER)) {
      return true;
    }
    return false;
  }

  public abstract Piece.PieceColour determineWinner();

  public boolean playerTurn(int coordinate) {
    Point point = Board.translateIndexToPoint(coordinate);
    //if the array calculated is vacant
    if (Move.makeMove(this, point.y, point.x)) {
      this.playingBoard.addPiece(coordinate, this.getCurrentPlayer().getPlayerColour());
      this.switchPlayer();

      // Check if the new player can make a legal move, and pass back turn if they can't
      if (Move.allPossibleMoves(this.getBoard(), this.getCurrentPlayer().getPlayerColour()).size() == 0) {
        this.switchPlayer();
        if (Move.allPossibleMoves(this.getBoard(), this.getCurrentPlayer().getPlayerColour()).size() == 0) {
          // End game if both players cannot make a legal move.
          this.setGameState(AbstractGame.GameState.GAVE_OVER);
        }
      }

      //notify observer of the changes
      setChanged();
      notifyObservers(this.getGameState());
      return true;
    }
    return false;
  }

  public enum GameState {
    IN_PROGRESS, GAVE_OVER, ABORTED;
  }

}
