package uk.ac.aston.dc2060.group5.reversi.players;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;

/**
 * Created by Sam on 09/10/2016.
 */
public abstract class AbstractPlayer {

  private PieceColour playerColour;
  private int timeLeftToPlayInSeconds = 1;

  public AbstractPlayer(PieceColour playerColour) {
    this.playerColour = playerColour;
  }

  public PieceColour getPlayerColour() {
    return this.playerColour;
  }

  public abstract boolean takeTurn(AbstractGame game, int coordinate);

  public int getTimeLeftToPlayInSeconds() {
    return timeLeftToPlayInSeconds;
  }

  public void setTimeLeftToPlayInSeconds(int timeLeftToPlayInSeconds) {
    this.timeLeftToPlayInSeconds = timeLeftToPlayInSeconds;
  }

}
