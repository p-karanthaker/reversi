package uk.ac.aston.dc2060.group5.reversi.players;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;

/**
 * Created by Sam on 09/10/2016.
 */
public class HumanPlayer extends AbstractPlayer {

  public HumanPlayer(PieceColour playerColour) {
    super(playerColour);
  }

  public boolean takeTurn(AbstractGame game, int coordinate) {
    return game.playerTurn(coordinate);
  }

}
