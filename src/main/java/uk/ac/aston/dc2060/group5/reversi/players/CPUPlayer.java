package uk.ac.aston.dc2060.group5.reversi.players;

import uk.ac.aston.dc2060.group5.reversi.model.Board;
import uk.ac.aston.dc2060.group5.reversi.model.Move;
import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;

import java.awt.Point;
import java.util.List;
import java.util.Random;

/**
 * Created by Sam on 09/10/2016.
 */
public class CPUPlayer extends AbstractPlayer {

  public CPUPlayer(PieceColour playerColour) {
    super(playerColour);
  }

  public void takeTurn(AbstractGame game) {
    // Pick random valid move
    List<Point> moves = Move.allPossibleMoves(game.getBoard(), game.getCurrentPlayer().getPlayerColour());
    int upperBound = moves.size();
    Random random = new Random();
    int index = random.nextInt(upperBound);

    Point moveToMake = moves.get(index);
    takeTurn(game, Board.translatePointToIndex(moveToMake));
  }

  public boolean takeTurn(AbstractGame game, int coordinate) {
      return game.playerTurn(coordinate);
  }

}
