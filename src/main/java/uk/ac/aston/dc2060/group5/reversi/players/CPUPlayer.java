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
 * Created by Sam on 09/10/2016.
 */
public class CPUPlayer extends AbstractPlayer {

  private boolean hardPlayer;

  public CPUPlayer(PieceColour playerColour, boolean hard) {
    super(playerColour);
    hardPlayer = hard;
  }


  public void takeTurn(AbstractGame game) {
    boolean moveTaken = false;

    // Get a list of the possible moves
    List<Point> moves = Move.allPossibleMoves(game.getBoard(), game.getCurrentPlayer().getPlayerColour());

    // If hard difficulty, take a corner if there is one
    if (hardPlayer) {
      moveTaken = tryTakeCorner(moves, game);
    }

    // If hard difficulty, attempt to avoid moving anywhere that would result in a corner
    // being available
    if (hardPlayer) {
      moves = avoidCornerOpportunity(moves);
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

  public boolean takeTurn(AbstractGame game, int coordinate) {
    return game.playerTurn(coordinate);
  }


  private boolean tryTakeCorner(List<Point> moves, AbstractGame game) {
    for (Point p : moves) {
      if (p.getX() == 0 && p.getY() == 0) {
        takeTurn(game, Board.translatePointToIndex(p));
        return true;
      } else if (p.getX() == 0 && p.getY() == 7) {
        takeTurn(game, Board.translatePointToIndex(p));
        return true;
      } else if (p.getX() == 7 && p.getY() == 0) {
        takeTurn(game, Board.translatePointToIndex(p));
        return true;
      } else if (p.getX() == 7 && p.getY() == 7) {
        takeTurn(game, Board.translatePointToIndex(p));
        return true;
      }
    }
    return false;
  }

  private List<Point> avoidCornerOpportunity(List<Point> moves) {
    List<Point> oldList = moves.stream().collect(Collectors.toList());
    List<Point> toRemove = new ArrayList<>();

    // Loop through the possible moves, adding all moves that could potentially
    // allow opponent to take a corner to the toRemove list.
    for (Point p : moves) {
      if (p.getX() == 1 || p.getX() == 6) {
        if (p.getY() == 1 || p.getY() == 0 || p.getY() == 6 || p.getY() == 7) {
          toRemove.add(p);
        }
      } else if (p.getY() == 1 || p.getY() == 6) {
        if (p.getX() == 1 || p.getX() == 0 || p.getX() == 6 || p.getX() == 7) {
          toRemove.add(p);
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
