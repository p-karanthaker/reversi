package uk.ac.aston.dc2060.group5.reversi.model;

import junit.framework.TestCase;

import org.junit.Before;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;

/**
 * Created by Karan Thaker.
 */
public class PieceTest extends TestCase {

  private Piece whitePiece;
  private Piece blackPiece;

  @Before
  public void setUp() {
    whitePiece = new Piece(PieceColour.WHITE);
    blackPiece = new Piece(PieceColour.BLACK);
  }

  /**
   * Tests that getting a piece's colour return the correct colour.
   */
  public void testGettingPieceColour() {
    assertEquals(PieceColour.WHITE, whitePiece.getPieceColour());
    assertEquals(PieceColour.BLACK, blackPiece.getPieceColour());
  }

  /**
   * Test that flipping a piece will change it's colour.
   */
  public void testFlipPiece() {
    // Assert the piece is originally white.
    assertEquals(PieceColour.WHITE, whitePiece.getPieceColour());

    // Flip the piece and assert that it has changed to be black.
    whitePiece.flipPiece();
    assertEquals(PieceColour.BLACK, whitePiece.getPieceColour());

    // Flip the piece again and assert that it is white again.
    whitePiece.flipPiece();
    assertEquals(PieceColour.WHITE, whitePiece.getPieceColour());
  }

  /**
   * Test that piece colours short and long names are returned correctly.
   */
  public void testPieceColourNames() {
    String blackPieceShortName = "b";
    String blackPieceLongName = "Black";
    String whitePieceShortName = "w";
    String whitePieceLongName = "White";

    assertEquals(blackPieceShortName, PieceColour.BLACK.getShortName());
    assertEquals(blackPieceLongName, PieceColour.BLACK.getLongName());
    assertEquals(whitePieceShortName, PieceColour.WHITE.getShortName());
    assertEquals(whitePieceLongName, PieceColour.WHITE.getLongName());

  }
}
