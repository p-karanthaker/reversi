package uk.ac.aston.dc2060.group5.reversi.model;

import junit.framework.TestCase;

import org.junit.Before;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.rulesets.ClassicGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.GameType;

import java.awt.Point;
import java.util.List;

/**
 * Created by Karan Thaker.
 */
public class MoveTest extends TestCase {

  private ClassicGame game;
  private Board board;

  @Before
  public void setUp() {
    game = new ClassicGame(GameType.PVP, false);
    board = game.getBoard();
  }

  /**
   * Test that a move cannot be made on an occupied square. The middle four squares are occupied at
   * the start of every game so in this test we will try to place a piece on the top-left-middle
   * square (coordinate = 3,3) where a white piece is already located.
   *
   * - - - - - - - -
   * - - - - - - - -
   * - - - - - - - -
   * - - - w b - - -
   * - - - b w - - -
   * - - - - - - - -
   * - - - - - - - -
   * - - - - - - - -
   */
  public void testMakingAMoveOnAnOccupiedSquare() {
    assertFalse(Move.makeMove(game, 3, 3));
  }

  /**
   * Tests that a black piece can be added to the coordinate 2,3. This is a valid start move in
   * Reversi considering the standard starting board setup. The diagram below shows how the board
   * should end up.
   * - - - - - - - - *=> - - - - - - - -
   * - - - - - - - - *=> - - - - - - - -
   * - - - - - - - - *=> - - - b - - - -
   * - - - w b - - - *=> - - - b b - - -
   * - - - b w - - - *=> - - - b w - - -
   * - - - - - - - - *=> - - - - - - - -
   * - - - - - - - - *=> - - - - - - - -
   * - - - - - - - - *=> - - - - - - - -
   */
  public void testMakingMoveOnValidEmptySquare() {
    // Assert that the piece is originally white.
    assertEquals(PieceColour.WHITE, board.getTile(3, 3).getPiece().getPieceColour());

    // Assert that valid move is made.
    assertTrue(Move.makeMove(game, 2, 3));

    // Assert that piece has now been flipped over to black.
    assertEquals(PieceColour.BLACK, board.getTile(3, 3).getPiece().getPieceColour());
  }

  /**
   * Tests that a black piece cannot be added to the coordinate 2,4. This is an ivalid start move in
   * Reversi considering the standard starting board setup.
   * - - - - - - - -
   * - - - - - - - -
   * - - - - - - - -
   * - - - w b - - -
   * - - - b w - - -
   * - - - - - - - -
   * - - - - - - - -
   * - - - - - - - -
   */
  public void testMakingMoveOnInvalidEmptySquare() {
    assertFalse(Move.makeMove(game, 2, 4));
  }

  /**
   * Test that for a given colour, all possible moves at the point in the game are found. All
   * possible moves at the start of a standard Reversi game for black are as follows:
   * (2,3) - (3,2) - (4,5) - (5,4)
   * - - - - - - - -
   * - - - - - - - -
   * - - - - - - - -
   * - - - w b - - -
   * - - - b w - - -
   * - - - - - - - -
   * - - - - - - - -
   * - - - - - - - -
   */
  public void testAllPossibleMovesFound() {
    List<Point> allPossibleMovesForBlack = Move.allPossibleMoves(board, PieceColour.BLACK);

    assertEquals(4, allPossibleMovesForBlack.size());
    assertEquals(new Point(3, 2), allPossibleMovesForBlack.get(0));
    assertEquals(new Point(2, 3), allPossibleMovesForBlack.get(1));
    assertEquals(new Point(5, 4), allPossibleMovesForBlack.get(2));
    assertEquals(new Point(4, 5), allPossibleMovesForBlack.get(3));
  }

}
