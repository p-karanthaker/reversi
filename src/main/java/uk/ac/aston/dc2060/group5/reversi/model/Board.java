package uk.ac.aston.dc2060.group5.reversi.model;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.players.AbstractPlayer;
import uk.ac.aston.dc2060.group5.reversi.players.CPUPlayer;

import java.awt.Point;
import java.util.Observable;
import java.util.Stack;

/**
 * Board represents the logical model of a Reversi playing board.
 *
 * <p>Created by Karan Thaker</p>
 */
public class Board extends Observable {

  /**
   * An 8x8 2D array of every tile on the playing grid.
   */
  private AbstractTile[][] grid;

  /**
   * A stack of white pieces available for play.
   */
  private Stack<Piece> whitePieces;

  /**
   * A stack of black pieces available for play.
   */
  private Stack<Piece> blackPieces;

  /**
   * The player who's turn it currently is.
   */
  private int currentPlayer;

  /**
   * Stores the players playing the game.
   */
  private AbstractPlayer[] players;

  public boolean gameOver = false;

  /**
   * Constructor which sets up the board and initialises the grid list and pieces stack.
   *
   * @param players the 2 players who are playing reversi.
   */
  public Board(AbstractPlayer[] players) {
    if (players.length != 2) {
      new IllegalStateException("Not enough players!");
    } else if (players[0].getPlayerColour().equals(players[1].getPlayerColour())) {
      new IllegalStateException("Players cannot be of same colour!");
    }
    this.players = players;
    this.currentPlayer = 0;

    this.grid = new AbstractTile[8][8];
    this.whitePieces = new Stack<Piece>();
    this.blackPieces = new Stack<Piece>();

    for (int i = 0; i < 32; i++) {
      this.whitePieces.add(i, new Piece(PieceColour.WHITE));
      this.blackPieces.add(i, new Piece(PieceColour.BLACK));
    }

    this.boardSetup();
  }

  /**
   * Returns the number of black pieces currently on the board.
   *
   * @return numPieces the number of black pieces currently on the board
   */
  public int getBlackPieceCount() {
    int numPieces = 0;
    for (AbstractTile[] row : grid) {
      for (AbstractTile t : row) {
        if (!t.isVacant()) {
          if (t.getPiece().getPieceColour() == PieceColour.BLACK) {
            numPieces++;
          }
        }
      }
    }
    return numPieces;
  }

  /**
   * Returns the number of white pieces currently on the board.
   *
   * @return numPieces the number of white pieces currently on the board
   */
  public int getWhitePieceCount() {
    int numPieces = 0;
    for (AbstractTile[] row : grid) {
      for (AbstractTile t : row) {
        if (!t.isVacant()) {
          if (t.getPiece().getPieceColour() == PieceColour.WHITE) {
            numPieces++;
          }
        }
      }
    }
    return numPieces;
  }

  /**
   * Returns the tile with the given tile id.
   *
   * @param tileId the id of the tile to return.
   * @return return the tile with the given tile id.
   */
  public AbstractTile getTile(final int tileId) {
    final Point point = translateIndexToPoint(tileId);
    return this.grid[point.y][point.x];
  }

  /**
   * Returns the tile with the given coordinates.
   *
   * @param row the row coordinate.
   * @param col the column coordinate.
   * @return return the tile with the given coordinates.
   */
  public AbstractTile getTile(final int row, final int col) {
    return this.grid[row][col];
  }

  /**
   * Translates an index value into a Point. This point can then be used to
   * access the {@link #grid} using Point.x as the column, and Point.y as the row.
   *
   * @param tileId the tileId which we want a Point for.
   * @return a Point object with Point.x as the column, and Point.y as the row of the array.
   */
  public static Point translateIndexToPoint(final int tileId) {
    // Translate tileId into a coordinate for 8x8 array.
    int row = tileId / 8;
    int col = tileId % 8;
    return new Point(col, row);
  }

  /**
   * Translates a Point into an index value.
   *
   * @param point the Point to translate into an integer value for an 8x8 grid.
   * @return an integer representation of a Point on an 8x8 grid.
   */
  public static int translatePointToIndex(final Point point) {
    return point.y * 8 + point.x;
  }

  /**
   * Sets up a standard 8x8 board with 4 pieces in the center squares. Arranged like so:
   * - - - - - - - -
   * - - - - - - - -
   * - - - - - - - -
   * - - - w b - - -
   * - - - b w - - -
   * - - - - - - - -
   * - - - - - - - -
   * - - - - - - - -
   */
  private void boardSetup() {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        int coordinate = translatePointToIndex(new Point(col, row));
        if (row == col && (row == 3 || row == 4)) {
          this.grid[row][col] = AbstractTile.createTile(coordinate, this.whitePieces.pop());
        } else if ((row == 3 || row == 4) && (col == 3 || col == 4)) {
          this.grid[row][col] = AbstractTile.createTile(coordinate, this.blackPieces.pop());
        } else {
          this.grid[row][col] = AbstractTile.createTile(coordinate, null);
        }
      }
    }
  }

  /**
   * Switches the current player.
   */
  private void switchPlayer() {
    this.currentPlayer = this.currentPlayer == 0 ? 1 : 0;
  }

  /**
   * Retrieves the current player from the player array.
   * @return the current player.
   */
  public AbstractPlayer getCurrentPlayer() {
    return this.players[this.currentPlayer];
  }

  /**
   * Adds a piece to the board.
   *
   * @param coordinate the coordinate to add a piece to.
   * @return true if a piece was added to the board.
   */
  public boolean addPiece(int coordinate) {

    //translate the coordinate to grid array.
    Point point = translateIndexToPoint(coordinate);

    //create new piece
    Piece piece = new Piece(this.getCurrentPlayer().getPlayerColour());

    //if the array calculated is vacant
    if (Move.makeMove(this, point.y, point.x)) {
      //place the piece on the grid
      this.grid[point.y][point.x] = AbstractTile.createTile(coordinate, piece);

      this.switchPlayer();

      // Check if the new player can make a legal move, and pass back turn if they can't
      if (Move.allPossibleMoves(this, getCurrentPlayer().getPlayerColour()).size() == 0) {
        this.switchPlayer();
        if (Move.allPossibleMoves(this, getCurrentPlayer().getPlayerColour()).size() == 0) {
          // End game.
          gameOver = true;
        }
      }

      //notify observer of the changes
      setChanged();
      notifyObservers(gameOver);


      return true;
    }

    return false;
  }

  /**
   * Produces a nice string representation of the board with it's pieces.
   *
   * @return a string representation of the board with it's pieces.
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    for (AbstractTile[] tileArray : this.grid) {
      for (AbstractTile tile : tileArray) {
        output.append(tile.toString() + " ");
      }
      output.append("\n");
    }
    return output.toString();
  }


}
