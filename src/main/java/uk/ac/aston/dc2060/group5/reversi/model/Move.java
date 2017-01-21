package uk.ac.aston.dc2060.group5.reversi.model;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


/**
 * This class allows you to check if a certain move is valid on a given board. It also provides the
 * utility to get a listing of all valid moves for a given piece colour.
 *
 * <p>
 * Created by Sam on 19/10/2016.
 * Reviewed by Karan Thaker.
 * </p>
 */
public class Move {

  /**
   * List of tiles which have pieces to be flipped.
   **/
  private static List<AbstractTile> piecesToFlip = new ArrayList<AbstractTile>();

  /**
   * The playing board to check move validity on.
   **/
  private static Board board;

  /**
   * Attempts to make a move by checking if the tile is first of all free. Then checks in every
   * direction to see if the move is valid at all. If the move is valid, then the
   * {@link #piecesToFlip} array will have items in it and the pieces will be flipped over.
   *
   * @param game the current game.
   * @param row  the row of the piece we want to place.
   * @param col  the column of the piece we want to place.
   * @return true if the move has been made, false if not.
   */
  public static boolean makeMove(AbstractGame game, int row, int col) {
    Move.board = game.getBoard();
    // boolean variable to return at end of method, set to true if move is valid
    boolean moveMade = false;

    // Get the tile from the coordinates.
    AbstractTile moveTile = board.getTile(row, col);

    // Check if the tile is vacant, return false if not.
    if (!moveTile.isVacant()) {
      return false;
    }

    for (Direction direction : Direction.values()) {
      checkDirection(game.getCurrentPlayer().getPlayerColour(), row, col, direction);
    }

    if (piecesToFlip.size() > 0) {
      for (AbstractTile tile : piecesToFlip) {
        tile.getPiece().flipPiece();
      }
      moveMade = true;
      piecesToFlip.clear();
    }

    return moveMade;
  }

  /**
   * Checks pieces in a given direction to see if the move is valid in that direction. If it is
   * then it adds the necessary pieces to flip to the {@link #piecesToFlip} list.
   *
   * @param playerColour the current player colour so we know what pieces to flip.
   * @param row          the row of the piece we want to place.
   * @param col          the columns of the piece we want to place.
   * @param direction    the direction we are checking.
   */
  private static List<AbstractTile> checkDirection(PieceColour playerColour, int row, int col,
                                                   final Direction direction) {

    // List of pieces which can potentially be flipped.
    List<AbstractTile> potentialPiecesToFlip = new ArrayList<AbstractTile>();

    // All validation is false unless proven otherwise.
    boolean potentiallyValid = false;
    boolean valid = false;

    // Coordinates for the next tile in the direction we are going
    int nextRow = row + direction.getY();
    int nextCol = col + direction.getX();

    // Condition ensures that the rows and columns do not go out of bounds (8x8 board)
    while (nextCol >= 0 && nextRow >= 0 && nextCol < 8 && nextRow < 8) {
      // Check piece on this tile to determine validity of move.
      AbstractTile tile = board.getTile(nextRow, nextCol);

      // Check vacancy of the tile to determine if we should progress.
      if (tile.isVacant()) {
        // Invalid move. Do not continue checks.
        break;
      } else {
        /* If the piece is of opposite colour then the move is potentially valid. Add the tile to
         * the list of tiles which contain pieces that can potentially be flipped. */
        if (!tile.getPiece().getPieceColour().equals(playerColour)) {
          potentiallyValid = true;
          potentialPiecesToFlip.add(tile);

        /* If the move is potentially valid and the piece we are checking is the same colour as
         * as the piece placed then the move is valid. No further checks in that direction are
         * required. */
        } else if (potentiallyValid && tile.getPiece().getPieceColour().equals(playerColour)) {
          valid = true;
          break;

        /* Previous checks failed and the move is still not potentially valid. No further checks
         * are required and the move is invalid. */
        } else if (!potentiallyValid) {
          break;
        }
      }

      // Move to next tile.
      nextRow += direction.getY();
      nextCol += direction.getX();
    }

    if (valid) {
      piecesToFlip.addAll(potentialPiecesToFlip);
    } else {
      potentialPiecesToFlip.clear();
    }
    return potentialPiecesToFlip;
  }

  /**
   * This only works for AI as it relies on placing a piece in a known valid location. It does not
   * check if the first piece added is in a valid spot.
   *
   * Looks ahead to see what the opponent can play if a given move is performed. Returns a list of
   * moves that the opponent could play.
   *
   * @param row         the row of the tile to move to.
   * @param col         the column of the tile to move to.
   * @param board       the board being played on.
   * @param pieceColour the colour of the player who is looking ahead.
   * @return a list of moves that the opponent could play if the given move was taken.
   */
  public static List<Point> lookAheadOneMove(int row, int col, Board board, PieceColour pieceColour) {
    // Place the piece on the board.
    board.addPiece(Board.translatePointToIndex(new Point(col, row)), pieceColour);

    List<AbstractTile> flip = new ArrayList<>();
    // Check which pieces would be flipped.
    for (Direction direction : Direction.values()) {
      flip.addAll(checkDirection(pieceColour, row, col, direction));
    }
    Move.piecesToFlip.clear();

    // Flip over the pieces. Clear static list because checkDirection populated it.
    flip.stream().forEach(abstractTile -> abstractTile.getPiece().flipPiece());

    // Find opponents moves.
    PieceColour opponentColour = PieceColour.BLACK;
    if (pieceColour.equals(PieceColour.BLACK)) {
      opponentColour = PieceColour.WHITE;
    }
    List<Point> opponentsMoves = allPossibleMoves(board, opponentColour);

    // Revert board back.
    flip.stream().forEach(abstractTile -> abstractTile.getPiece().flipPiece());
    board.removePiece(Board.translatePointToIndex(new Point(col, row)));

    return opponentsMoves;
  }

  /**
   * Checks if there is a valid move that can be made for a given piece colour.
   *
   * @param board       the board on which to look for valid moves.
   * @param pieceColour the current player colour so we know which player we are checking has valid
   *                    moves.
   * @return a list of valid moves in the form of Point objects.
   */
  public static List<Point> allPossibleMoves(Board board, PieceColour pieceColour) {
    Move.board = board;
    // All validation is false unless proven true
    boolean valid = false;

    List<Point> potentialValidMoves = new ArrayList<>();

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        AbstractTile tile = board.getTile(row, col);

        if (tile.isVacant()) {
          for (Direction direction : Direction.values()) {
            checkDirection(pieceColour, row, col, direction);
            if (piecesToFlip.size() > 0) {
              potentialValidMoves.add(new Point(col, row));
              piecesToFlip.clear();
            }
          }
        }
      }
    }
    return potentialValidMoves;
  }

  /**
   * Defines directions and the translation steps needed to move 1 step in that direction.
   *
   * <p>Created by Karan Thaker.</p>
   */
  private enum Direction {

    /**
     * North/Upward direction. Reduce Y axis by 1.
     **/
    NORTH(0, -1),

    /**
     * East/Right direction. Increase X axis by 1.
     **/
    EAST(1, 0),

    /**
     * South/Downward direction. Increase Y axis by 1.
     **/
    SOUTH(0, 1),

    /**
     * West/Left direction. Decrease X axis by 1.
     **/
    WEST(-1, 0),

    /**
     * Northeast/Upward-right direction. Increase X axis by 1, and decrease Y axis by 1.
     **/
    NORTHEAST(1, -1),

    /**
     * Southeast/Downward-right direction. Increase both axis by 1.
     **/
    SOUTHEAST(1, 1),

    /**
     * Southwest/Downward-left direction. Decrease X axis by 1, and increase Y axis by 1.
     **/
    SOUTHWEST(-1, 1),

    /**
     * Northwest/Upward-left direction. Decrease both axis by 1.
     **/
    NORTHWEST(-1, -1);

    /**
     * X axis step size.
     **/
    private int x;

    /**
     * Y axis step size.
     **/
    private int y;

    /**
     * Constructor for Direction.
     *
     * @param x the X axis step size.
     * @param y the Y axis step size.
     */
    Direction(final int x, final int y) {
      this.x = x;
      this.y = y;
    }

    /**
     * Gets the step size in the X axis for the direction.
     *
     * @return the step size of the X axis.
     */
    public int getX() {
      return this.x;
    }

    /**
     * Gets the step size in the Y axis for the direction.
     *
     * @return the step size of the Y axis.
     */
    public int getY() {
      return this.y;
    }
  }
}
