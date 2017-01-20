package uk.ac.aston.dc2060.group5.reversi.players;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;

/**
 * Models a generic player.
 *
 * <p>
 * Created by Sam on 09/10/2016.
 * Reviewed by Karan Thaker.
 * </p>
 */
public abstract class AbstractPlayer {

  /**
   * The piece colour that the player is playing as.
   */
  private PieceColour playerColour;

  /**
   * If amount of time a player has left to play. Set to 1 for non time-limited games.
   */
  private int timeLeftToPlayInSeconds = 1;

  /**
   * Super constructor for children of this class.
   *
   * @param playerColour the colour to assign to the player.
   */
  public AbstractPlayer(PieceColour playerColour) {
    this.playerColour = playerColour;
  }

  /**
   * Returns the player's colour.
   *
   * @return the piece colour that the player is assigned to.
   */
  public PieceColour getPlayerColour() {
    return this.playerColour;
  }

  /**
   * Models the way in which the player takes a turn to add a piece to the board.
   *
   * @param game       the game in which the player is playing.
   * @param coordinate the coordinate to add the piece to.
   * @return true if the move was successful.
   */
  public abstract boolean takeTurn(AbstractGame game, int coordinate);

  /**
   * Returns the amount of time a player has left to play in seconds.
   *
   * @return the amount of time the player has left to play in seconds.
   */
  public int getTimeLeftToPlayInSeconds() {
    return timeLeftToPlayInSeconds;
  }

  /**
   * Set the amount of time the player has left to play in seconds.
   *
   * @param timeLeftToPlayInSeconds the value to set the time left to play in seconds to.
   */
  public void setTimeLeftToPlayInSeconds(int timeLeftToPlayInSeconds) {
    this.timeLeftToPlayInSeconds = timeLeftToPlayInSeconds;
  }

}
