package uk.ac.aston.dc2060.group5.reversi.model;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;

import java.awt.Point;
import java.util.Stack;

/**
 * Board represents the logical model of a Reversi playing board.
 *
 * <p>Created by Karan Thaker</p>
 */
public class Board {

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
   * Constructor which sets up the board and initialises the grid list and pieces stack.
   */
  public Board() {
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
   * Returns the number of specified colour pieces currently on the board.
   *
   * @param pieceColour the colour of the pieces to count.
   * @return numPieces the number of specified colour pieces currently on the board.
   */
  public int getPieceCount(PieceColour pieceColour) {
    int numPieces = 0;
    for (AbstractTile[] row : grid) {
      for (AbstractTile t : row) {
        if (!t.isVacant()) {
          if (t.getPiece().getPieceColour() == pieceColour) {
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
   * Adds a piece to the board.
   *
   * @param coordinate  the coordinate to add a piece to.
   * @param pieceColour the colour of the piece being added to the board.
   * @return true if a piece was added to the board.
   */
  public boolean addPiece(int coordinate, PieceColour pieceColour) {
    // Translate the coordinate to grid array.
    Point point = translateIndexToPoint(coordinate);

    // Create new piece
    Piece piece = new Piece(pieceColour);

    if (this.getTile(coordinate).isVacant()) {
      // Place the piece on the grid
      this.grid[point.y][point.x] = AbstractTile.createTile(coordinate, piece);
      return true;
    }
    return false;
  }

  /**
   * Make any given tile vacant.
   *
   * @param coordinate the coordinate of the tile to make vacant.
   */
  protected void removePiece(int coordinate) {
    // Translate the coordinate to grid array.
    Point point = translateIndexToPoint(coordinate);

    this.grid[point.y][point.x] = AbstractTile.createTile(coordinate, null);
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
