package uk.ac.aston.dc2060.group5.reversi.model;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.players.AbstractPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

/**
 * Board represents the logical model of a Reversi playing board.
 *
 * Created by Karan Thaker
 */
public class Board extends Observable {

  /**
   * A list of every tile on the playing grid.
   */
  private List<AbstractTile> grid;

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
    this.grid = new ArrayList<AbstractTile>();
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
    return this.grid.get(tileId);
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
    for (int i = 0; i < 64; i++) {
      if (i == 27 || i == 36)
        this.grid.add(AbstractTile.createTile(i, this.whitePieces.pop()));
      else if (i == 28 || i == 35)
        this.grid.add(AbstractTile.createTile(i, this.blackPieces.pop()));
      else
        this.grid.add(AbstractTile.createTile(i, null));
    }
  }

  /**
   * Produces a nice string representation of the board with it's pieces.
   * @return a string representation of the board with it's pieces.
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    int count = 1;
    for (AbstractTile tile : this.grid) {
      output.append(tile.toString() + " ");
      if (count % 8 == 0)
        output.append("\n");
      count++;
    }
    return output.toString();
  }

}