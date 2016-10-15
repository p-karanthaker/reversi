package uk.ac.aston.dc2060.group5.reversi.model;

import uk.ac.aston.dc2060.group5.reversi.players.AbstractPlayer;
import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

/**
 * Created by Sam on 09/10/2016.
 */
public class Board extends Observable {

  private List<AbstractTile> grid;
  private Stack<Piece> whitePieces;
  private Stack<Piece> blackPieces;
  private AbstractPlayer currentPlayer;

  private int counter;  //primitive, automatically initialised to 0

  public Board() {
    this.grid = new ArrayList<AbstractTile>();
    this.whitePieces = new Stack<Piece>();
    this.blackPieces = new Stack<Piece>();

    for (int i = 0; i < 32; i++)
      this.whitePieces.add(i, new Piece(PieceColor.WHITE));

    for (int i = 0; i < 32; i++)
      this.blackPieces.add(i, new Piece(PieceColor.BLACK));

    this.boardSetup();
  }

  public AbstractTile getTile(final int tileId) {
    return this.grid.get(tileId);
  }

  private void boardSetup() {
    for (int i = 0; i < 64; i++) {
      if (i == 27 || i == 36)
        this.grid.add(AbstractTile.abstractTile(i, this.whitePieces.pop()));
      else if (i == 28 || i == 35)
        this.grid.add(AbstractTile.abstractTile(i, this.blackPieces.pop()));
      else
        this.grid.add(AbstractTile.abstractTile(i, null));
    }
  }

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