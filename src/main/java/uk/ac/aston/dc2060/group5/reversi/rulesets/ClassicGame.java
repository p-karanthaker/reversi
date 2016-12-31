package uk.ac.aston.dc2060.group5.reversi.rulesets;

import uk.ac.aston.dc2060.group5.reversi.model.Board;
import uk.ac.aston.dc2060.group5.reversi.model.Move;

import java.awt.Point;

/**
 * Created by Sam on 09/10/2016.
 */
public class ClassicGame extends AbstractGame {

  public ClassicGame(GameType gameType) {
    super(gameType);
  }

  @Override
  public boolean playerTurn(int coordinate) {
    Point point = Board.translateIndexToPoint(coordinate);
    //if the array calculated is vacant
    if (Move.makeMove(this, point.y, point.x)) {
      this.playingBoard.addPiece(coordinate, this.getCurrentPlayer().getPlayerColour());
      this.switchPlayer();

      // Check if the new player can make a legal move, and pass back turn if they can't
      if (Move.allPossibleMoves(this.getBoard(), this.getCurrentPlayer().getPlayerColour()).size() == 0) {
        this.switchPlayer();
        if (Move.allPossibleMoves(this.getBoard(), this.getCurrentPlayer().getPlayerColour()).size() == 0) {
          // End game if both players cannot make a legal move.
          this.setGameState(AbstractGame.GameState.GAVE_OVER);
        }
      }

      //notify observer of the changes
      setChanged();
      notifyObservers(this.getGameState());
      return true;
    }
    return false;
  }
}
