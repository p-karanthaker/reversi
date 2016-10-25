package uk.ac.aston.dc2060.group5.reversi.model;

/**
 * AbstractTile models a basic tile design.
 *
 * <p>
 * Created by Maryam Ali
 * Reviewed by Karan Thaker
 * </p>
 */
public abstract class AbstractTile {

  /**
   * The coordinate of the tile on a grid.
   */
  private int tileCoordinate;

  /**
   * Abstract method which returns the Piece on the tile when implemented.
   *
   * @return the Piece on the tile.
   */
  public abstract Piece getPiece();

  /**
   * Abstract method which returns the vacancy of the tile.
   *
   * @return true if the tile is a Vacant tile, false if it is a FullTile.
   */
  public abstract boolean isVacant();

  /**
   * Private constructor which takes a tileCoordinate since all tiles must have a coordinate.
   *
   * @param tileCoordinate the coordinate of the tile on the board.
   */
  private AbstractTile(int tileCoordinate) {
    this.tileCoordinate = tileCoordinate;
  }

  /**
   * Method to return the coordinate which the tile is on.
   *
   * @return the tile's coordinate on the board.
   */
  public int getTileCoordinate() {
    return tileCoordinate;
  }

  /**
   * Creates a VacantTile or a FullTile depending on whether or not a Piece object is passed to it.
   * If null is passed into the piece parameter then a VacantTile is created. Otherwise a FullTile
   * will be created.
   *
   * @param tileCoordinate the coordinate at which to create the tile.
   * @param piece          the piece to place on the tile.
   * @return the created tile.
   */
  public static AbstractTile createTile(int tileCoordinate, Piece piece) {
    return piece == null ? new VacantTile(tileCoordinate) : new FullTile(tileCoordinate, piece);
  }

  /**
   * VacantTile models a tile which does not contain a piece. It only has a coordinate on the board.
   */
  private static class VacantTile extends AbstractTile {

    /**
     * Private constructor to prevent instantiation. The only way a tile can be created is through
     * the {@link AbstractTile} createTile method.
     *
     * @param tileCoordinate the coordinate of the tile on the board.
     */
    private VacantTile(int tileCoordinate) {
      super(tileCoordinate);
    }

    /**
     * Implementation of abstract method getPiece(). VacantTile returns null because it cannot
     * contain a Piece.
     *
     * @return null since a VacantTile contains no piece.
     */
    @Override
    public Piece getPiece() {
      return null;
    }

    /**
     * Implementation of abstract method isVacant(). VacantTile returns true.
     *
     * @return true.
     */
    @Override
    public boolean isVacant() {
      return true;
    }

    /**
     * Returns the string "-" to signify a vacant tile.
     *
     * @return the string "-".
     */
    @Override
    public String toString() {
      return "-";
    }
  }

  /**
   * FullTile models a tile which contains a Piece.
   */
  private static class FullTile extends AbstractTile {

    /**
     * The piece on the tile.
     */
    private Piece pieceOnTile;

    /**
     * Private constructor to prevent instantiation. The only way a tile can be created is through
     * the {@link AbstractTile} createTile method.
     *
     * @param tileCoordinate the coordinate of the tile on the board.
     * @param pieceOnTile    the piece on the tile.
     */
    private FullTile(int tileCoordinate, Piece pieceOnTile) {
      super(tileCoordinate);
      this.pieceOnTile = pieceOnTile;
    }

    /**
     * Implementation of abstract method getPiece(). FullTile returns the Piece which it contains.
     *
     * @return the Piece which is on the tile.
     */
    @Override
    public Piece getPiece() {
      return this.pieceOnTile;
    }

    /**
     * Implementation of abstract method isVacant(). FullTile returns false.
     *
     * @return false.
     */
    @Override
    public boolean isVacant() {
      return false;
    }

    /**
     * Returns the string representation of the enum
     * {@link uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour}.
     *
     * @return the string representation of the Piece's colour.
     */
    @Override
    public String toString() {
      return this.pieceOnTile.toString();
    }
  }

}
