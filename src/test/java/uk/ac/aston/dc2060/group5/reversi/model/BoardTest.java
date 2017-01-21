package uk.ac.aston.dc2060.group5.reversi.model;

import junit.framework.TestCase;

import org.junit.Before;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;

import java.awt.Point;

/**
 * Created by Karan Thaker.
 */
public class BoardTest extends TestCase {

  private Board board;

  @Before
  public void setUp() {
    board = new Board();
  }

  /**
   * Tests the translateIndexToPoint() method to check that it converts a given index number to a
   * valid point on an 8x8 board (array).
   */
  public void testTranslationOfIndexToPoint() {
    Point originPoint = Board.translateIndexToPoint(0);
    assertNotNull(board.getTile(originPoint.y, originPoint.x));

    Point endPoint = Board.translateIndexToPoint(63);
    assertNotNull(board.getTile(endPoint.y, endPoint.x));

    Point invalidPoint = Board.translateIndexToPoint(64);
    try {
      board.getTile(invalidPoint.y, invalidPoint.x);
      fail("Method did not throw exception.");
    } catch (ArrayIndexOutOfBoundsException expectedException) {

    }
  }

  /**
   * Tests the translatePointToIndex() method to check that it converts a given Point to a valid
   * index on an 8x8 board (array).
   */
  public void testTranslationOfPointToIndex() {
    int originIndex = Board.translatePointToIndex(new Point(0, 0));
    assertNotNull(board.getTile(originIndex));

    int endIndex = Board.translatePointToIndex(new Point(7, 7));
    assertNotNull(board.getTile(endIndex));

    int invalidIndex = Board.translatePointToIndex(new Point(8, 8));
    try {
      board.getTile(invalidIndex);
      fail("Method did not throw exception");
    } catch (ArrayIndexOutOfBoundsException expectedException) {

    }
  }

  /**
   * Test that tile can be returned using index.
   */
  public void testGetTileUsingInteger() {
    assertNotNull(board.getTile(0));
    assertNotNull(board.getTile(63));

    try {
      board.getTile(64);
      fail("Method did not throw exception");
    } catch (ArrayIndexOutOfBoundsException expectedException) {

    }
  }

  /**
   * Test that tile can be returned using coordinates.
   */
  public void testGetTileUsingCoordinates() {
    assertNotNull(board.getTile(0, 0));
    assertNotNull(board.getTile(7, 7));

    try {
      board.getTile(7, 8);
      fail("Method did not throw exception.");
    } catch (ArrayIndexOutOfBoundsException expectedException) {

    }
  }

  /**
   * Tests that the board is set up with the correct arrangement of pieces like so:
   * - - - - - - - -
   * - - - - - - - -
   * - - - - - - - -
   * - - - w b - - -
   * - - - b w - - -
   * - - - - - - - -
   * - - - - - - - -
   * - - - - - - - -
   *
   * Indicies: 27,28,35,36 should contain pieces
   */
  public void testStandardBoard() {
    assertEquals(PieceColour.WHITE, board.getTile(27).getPiece().getPieceColour());
    assertEquals(PieceColour.BLACK, board.getTile(28).getPiece().getPieceColour());
    assertEquals(PieceColour.BLACK, board.getTile(35).getPiece().getPieceColour());
    assertEquals(PieceColour.WHITE, board.getTile(36).getPiece().getPieceColour());
    assertEquals(2, board.getPieceCount(PieceColour.WHITE));
    assertEquals(2, board.getPieceCount(PieceColour.BLACK));
  }

  /**
   * Tests adding a piece to an already occupied tile.
   */
  public void testTestAddingPieceToFullTile() {
    assertFalse(board.addPiece(27, PieceColour.WHITE));
  }

  /**
   * Tests adding a piece to a vacant tile.
   */
  public void testAddingPieceToEmptyTile() {
    assertTrue(board.addPiece(0, PieceColour.WHITE));
  }
}
