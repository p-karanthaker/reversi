package uk.ac.aston.dc2060.group5.reversi.model;

/**
 * Piece models a Reversi playing piece. A {@link Piece} has a {@link PieceColour} and can be
 * flipped in order to change it's colour.
 *
 * <p>
 * Created by Dean Sohn.
 * Reviewed by Karan Thaker.
 * </p>
 */
public class Piece {

  /**
   * The current colour of the piece.
   */
  private PieceColour pieceColour;

  /**
   * Constructor to create a piece.
   *
   * @param colour the colour that the piece should be.
   */
  public Piece(PieceColour colour) {
    this.pieceColour = colour;
  }

  /**
   * Returns the current colour of the piece.
   *
   * @return the current colour of the piece.
   */
  public PieceColour getPieceColour() {
    return pieceColour;
  }

  /**
   * Flips the playing piece so that the opposite colour is now the current colour. If the piece is
   * black then the {@link PieceColour} will be white when flipped and vice-versa.
   */
  public void flipPiece() {
    if (pieceColour.equals(PieceColour.BLACK)) {
      this.pieceColour = PieceColour.WHITE;
    } else {
      this.pieceColour = PieceColour.BLACK;
    }
  }

  /**
   * Returns a string representation of the Piece's colour.
   *
   * @return the short name of {@link PieceColour}.
   */
  @Override
  public String toString() {
    return this.pieceColour.shortName;
  }

  /**
   * Defines what colours a piece can be.
   */
  public enum PieceColour {

    /**
     * Colour Black - with short name of "b".
     */
    BLACK("b", "Black"),

    /**
     * Colour White - with short name of "w".
     */
    WHITE("w", "White");

    /**
     * The short name of the piece colour.
     */
    private String shortName;

    private String longName;

    /**
     * Constructor for {@link PieceColour} with a short name.
     */
    PieceColour(String shortName, String longName) {
      this.shortName = shortName;
      this.longName = longName;
    }

    /**
     * Returns the short name of the piece colour.
     *
     * @return the short name of the piece colour.
     */
    public String getShortName() {
      return this.shortName;
    }

    public String getLongName() {
      return this.longName;
    }
  }

}
