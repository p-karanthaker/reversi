package uk.ac.aston.dc2060.group5.reversi.players;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;

/**
 * Created by Sam on 09/10/2016.
 */
public abstract class AbstractPlayer {

  private PieceColour playerColour;

  public AbstractPlayer(PieceColour playerColour) {
    this.playerColour = playerColour;
  }

  public PieceColour getPlayerColour() {
    return this.playerColour;
  }

  public abstract void takeTurn();

}
