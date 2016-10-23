package uk.ac.aston.dc2060.group5.reversi.model;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.players.AbstractPlayer;

import java.awt.*;
import java.util.Observable;
import java.util.Stack;

/**
 * Board represents the logical model of a Reversi playing board.
 *
 * Created by Karan Thaker
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
  private AbstractPlayer currentPlayer;

  /**
   * Constructor which sets up the board and initialises the grid list and pieces stack.
   */
  public Board() {
    this.grid = new AbstractTile[8][8];
    this.whitePieces = new Stack<Piece>();
    this.blackPieces = new Stack<Piece>();

    for (int i = 0; i < 32; i++)
      this.whitePieces.add(i, new Piece(PieceColour.WHITE));

    for (int i = 0; i < 32; i++)
      this.blackPieces.add(i, new Piece(PieceColour.BLACK));

    this.boardSetup();
  }

  /**
   * Returns the tile with the given tile id.
   * @param tileId the id of the tile to return.
   * @return return the tile with the given tile id.
   */
  public AbstractTile getTile(final int tileId) {
    final Point point = this.translateIndexToPoint(tileId);
    return this.grid[point.y][point.x];
  }

  /**
   * Returns the tile with the given coordinates.
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
   * @param tileId
   * @return a Point object with Point.x as the column, and Point.y as the row of the array.
   */
  public Point translateIndexToPoint(final int tileId) {
    // Translate tileId into a coordinate for 8x8 array.
    int row = (int) (tileId / 8);
    int col = tileId % 8;
    return new Point(col, row);
  }

  /**

   * Translates a Point into an index value.
   * @param point
   * @return
   */
  private int translatePointToIndex(final Point point) {
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
        int coordinate = this.translatePointToIndex(new Point(col, row));
        if (row == col && (row == 3 || row == 4))
          this.grid[row][col] = AbstractTile.createTile(coordinate, this.whitePieces.pop());
        else if ((row == 3 || row == 4) && (col == 3 || col == 4))
          this.grid[row][col] = AbstractTile.createTile(coordinate, this.blackPieces.pop());
        else
          this.grid[row][col] = AbstractTile.createTile(coordinate, null);
      }
    }
  }


  /**
   * Adds a piece to the board.
   * @param
   * @return
   */
  public Boolean addPiece(PieceColour pieceColour, int coordinate){

    //translate the coordinate to grid array.
    Point point = translateIndexToPoint(coordinate);

    //create new piece
    Piece piece = new Piece(pieceColour);

    //if the array calculated is vacant
    if(Move.makeMove(this, point.y, point.x, piece)) {
      //place the piece on the grid
      grid[point.y][point.x] =  AbstractTile.createTile(coordinate, piece);

      //notify observer of the changes
      setChanged();
      notifyObservers(pieceColour);
    }
    return grid[point.y][point.x].isVacant()== false ? true : false;

    }

  /**
   * Produces a nice string representation of the board with it's pieces.
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
