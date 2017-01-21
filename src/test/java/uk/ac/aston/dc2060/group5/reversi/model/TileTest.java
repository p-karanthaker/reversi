package uk.ac.aston.dc2060.group5.reversi.model;

import junit.framework.TestCase;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;

/**
 * Created by Karan Thaker.
 */
public class TileTest extends TestCase {

  /**
   * Tests that a VacantTile returns null for pieces.
   */
  public void testVacantTileHasNoPiece() {
    assertNull(AbstractTile.createTile(0, null).getPiece());
  }

  /**
   * Test that isVacant method returns true for a VacantTile.
   *
   * @throws Exception thrown if class cannot be found.
   */
  public void testVacantTileIsVacant() throws Exception {
    Class<?> vacantTile =
        Class.forName("uk.ac.aston.dc2060.group5.reversi.model.AbstractTile$VacantTile");

    /* First prove that passing 0 and null to the AbstractTile.createTile method will return a
     * VacantTile class.
     */
    assertEquals(vacantTile.getName(), AbstractTile.createTile(0, null).getClass().getName());

    assertTrue(AbstractTile.createTile(0, null).isVacant());
  }

  /**
   * Tests that a FullTile has a piece on it.
   */
  public void testFullTileHasPiece() {
    assertNotNull(AbstractTile.createTile(0, new Piece(PieceColour.WHITE)).getPiece());
  }


  /**
   * Tests that isVacant method returns false for a FullTile.
   *
   * @throws Exception thrown if class cannot be found.
   */
  public void testFullTileIsNotVacant() throws Exception {
    Class<?> fullTile =
        Class.forName("uk.ac.aston.dc2060.group5.reversi.model.AbstractTile$FullTile");

    /* First prove that passing 0 and a piece to the AbstractTile.createTile method will return a
     * FullTile class.
     */
    assertEquals(fullTile.getName(), AbstractTile.createTile(0, new Piece(PieceColour.WHITE)).getClass().getName());

    assertFalse(AbstractTile.createTile(0, new Piece(PieceColour.WHITE)).isVacant());
  }
}
