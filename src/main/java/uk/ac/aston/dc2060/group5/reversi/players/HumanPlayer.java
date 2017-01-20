package uk.ac.aston.dc2060.group5.reversi.players;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;

/**
 * Models a Human player.
 *
 * <p>Created by Sam on 09/10/2016.</p>
 */
public class HumanPlayer extends AbstractPlayer {

  /**
   * Constructs a Human player.
   *
   * @param playerColour the colour assigned to the player.
   */
  public HumanPlayer(PieceColour playerColour) {
    super(playerColour);
  }

  /**
   * Allows the player to take a turn.
   *
   * @param game       the game in which the player is playing.
   * @param coordinate the coordinate to add the piece to.
   * @return true if the move was successful.
   */
  public boolean takeTurn(AbstractGame game, int coordinate) {
    return game.playerTurn(coordinate);
  }

}
