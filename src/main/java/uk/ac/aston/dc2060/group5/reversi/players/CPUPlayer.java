package uk.ac.aston.dc2060.group5.reversi.players;

import uk.ac.aston.dc2060.group5.reversi.model.Board;
import uk.ac.aston.dc2060.group5.reversi.model.Move;
import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Models a CPU Player.
 *
 * <p>
 * Created by Sam on 09/10/2016.
 * Reviewed by Karan Thaker.
 * </p>
 */
public class CPUPlayer extends AbstractPlayer {

  /**
   * True if the CPU is set to make smarter moves.
   */
  private boolean hardPlayer;

  /**
   * Constructs a CPU player.
   *
   * @param playerColour the colour the CPU will play as.
   * @param hard         true if the CPU should make smarter moves.
   */
  public CPUPlayer(PieceColour playerColour, boolean hard) {
    super(playerColour);
    hardPlayer = hard;
  }

  /**
   * The CPU will attempt to make a move.
   *
   * @param game the game in which the CPU is making the move.
   */
  public void takeTurn(AbstractGame game) {
    boolean moveTaken = false;

    // Get a list of the possible moves
    List<Point> moves =
        Move.allPossibleMoves(game.getBoard(), game.getCurrentPlayer().getPlayerColour());

    // If hard difficulty, take a corner if there is one
    if (hardPlayer) {
      moveTaken = tryTakeCorner(moves, game);
      if (!moveTaken) {
        // Attempt to remove moves that would result in giving up a corner.
        moves = avoidCornerOpportunity(moves, game);
      }
    }

    // Pick random valid move if move has not yet been made
    if (!moveTaken) {
      int upperBound = moves.size();
      Random random = new Random();
      int index = random.nextInt(upperBound);

      Point moveToMake = moves.get(index);
      takeTurn(game, Board.translatePointToIndex(moveToMake));
    }
  }

  /**
   * CPU will make a move.
   *
   * @param game       the game in which the player is playing.
   * @param coordinate the coordinate to add the piece to.
   * @return true if the move was successful.
   */
  public boolean takeTurn(AbstractGame game, int coordinate) {
    return game.playerTurn(coordinate);
  }


  private boolean tryTakeCorner(List<Point> moves, AbstractGame game) {
    for (Point p : moves) {
      if (p.equals(new Point(0, 0))
          || p.equals(new Point(7, 0))
          || p.equals(new Point(0, 7))
          || p.equals(new Point(7, 7))) {
        takeTurn(game, Board.translatePointToIndex(p));
        return true;
      }
    }
    return false;
  }

  /**
   * The CPU will attempt to remove moves from it's list which would enable the opponent to place a
   * piece in a corner.
   *
   * @param moves the list of possible moves that the CPU can make.
   * @param game  the game to check moves on.
   * @return a new list of moves the CPU can make.
   */
  protected List<Point> avoidCornerOpportunity(List<Point> moves, AbstractGame game) {
    List<Point> oldList = moves.stream().collect(Collectors.toList());
    List<Point> toRemove = new ArrayList<>();

    // Try out a move from the list
    for (Point moveToTry : oldList) {
      List<Point> opponentsMoves =
          Move.lookAheadOneMove(moveToTry.y, moveToTry.x, game.getBoard(), this.getPlayerColour());

      for (Point opponentsMove : opponentsMoves) {
        // If any of the opponent's moves are on a corner, then remove the moveToTry.
        if (opponentsMove.equals(new Point(0, 0))
            || opponentsMove.equals(new Point(0, 7))
            || opponentsMove.equals(new Point(7, 0))
            || opponentsMove.equals(new Point(7, 7))) {
          toRemove.add(moveToTry);
        }
      }
    }

    // Remove all of the moves from the list
    moves.removeAll(toRemove);

    // If removing all of the moves found would result in an empty move list,
    // return the original list as no other moves are possible.
    if (moves.isEmpty()) {
      return oldList;
    } else {
      // Return new list with corner opportunities removed.
      return moves;
    }
  }
}
