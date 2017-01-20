package uk.ac.aston.dc2060.group5.reversi.rulesets;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;

/**
 * Models the game AntiReversi. It is the same as a ClassicGame except the winner is the player with
 * the least pieces.
 *
 * <p>Created by Karan Thaker.</p>
 */
public class AntiReversi extends AbstractGame {

  /**
   * Constructs an AntiReversi game.
   *
   * @param gameType the type of game.
   */
  public AntiReversi(GameType gameType) {
    super(gameType, false);
  }

  /**
   * Constructs a timed AntiReversi game.
   *
   * @param gameType                    the type of game.
   * @param totalTimePerPlayerInSeconds the total time each player has in seconds.
   */
  public AntiReversi(GameType gameType, int totalTimePerPlayerInSeconds) {
    super(gameType, false, totalTimePerPlayerInSeconds);
  }

  /**
   * Deterimes the winner by checking who has the least pieces on the board.
   *
   * @return the player colour with the least pieces. Null if they are equal.
   */
  @Override
  public PieceColour determineWinner() {
    // Black Wins if they have less pieces on the board than White.
    if (this.getBoard().getPieceCount(PieceColour.BLACK)
        < this.getBoard().getPieceCount(PieceColour.WHITE)) {
      return PieceColour.BLACK;
    }
    // White Wins if they have less pieces on the board than Black.
    if (this.getBoard().getPieceCount(PieceColour.BLACK)
        > this.getBoard().getPieceCount(PieceColour.WHITE)) {
      return PieceColour.WHITE;
    }
    // Draw if there are an equal number of Black and White pieces on the board.
    return null;
  }

}
