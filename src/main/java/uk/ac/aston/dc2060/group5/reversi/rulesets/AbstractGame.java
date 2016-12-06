package uk.ac.aston.dc2060.group5.reversi.rulesets;

import uk.ac.aston.dc2060.group5.reversi.model.Board;
import uk.ac.aston.dc2060.group5.reversi.model.Piece;
import uk.ac.aston.dc2060.group5.reversi.players.AbstractPlayer;
import uk.ac.aston.dc2060.group5.reversi.players.CPUPlayer;
import uk.ac.aston.dc2060.group5.reversi.players.HumanPlayer;

import java.util.Observable;

/**
 * Created by Sam on 09/10/2016.
 */
public abstract class AbstractGame extends Observable {

  protected Board playingBoard;
  private AbstractPlayer[] players;
  protected GameType gameType;
  private int currentPlayer;

  protected boolean gameOver = false;

  public AbstractGame(GameType gameType) {
    this.players = new AbstractPlayer[2];
    this.gameType = gameType;
    this.playingBoard = new Board();
    this.currentPlayer = 0;

    if (this.gameType.equals(GameType.PVP)) {
      this.players[0] = new HumanPlayer(Piece.PieceColour.BLACK);
      this.players[1] = new HumanPlayer(Piece.PieceColour.WHITE);
    } else {
      this.players[0] = new HumanPlayer(Piece.PieceColour.BLACK);
      this.players[1] = new CPUPlayer(Piece.PieceColour.WHITE);
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
  protected void switchPlayer() {
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

  public boolean isGameOver() {
    return this.gameOver;
  }

  public abstract boolean playerTurn(int coordinate);

}
