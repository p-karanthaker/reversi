package uk.ac.aston.dc2060.group5.reversi.rulesets;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;

/**
 * Models a classic Reversi game.
 *
 * <p>Created by Karan Thaker.</p>
 */
public class ClassicGame extends AbstractGame {

  /**
   * Constructs a classic Reversi game.
   *
   * @param gameType       the type of game.
   * @param hardDifficulty whether the AI is set to hard mode.
   */
  public ClassicGame(GameType gameType, boolean hardDifficulty) {
    super(gameType, hardDifficulty);
  }

  /**
   * Constructs a timed classic Reversi game.
   *
   * @param gameType                    the type of game.
   * @param hardDifficulty              whether the AI is set to hard mode.
   * @param totalTimePerPlayerInSeconds the amount of time each player has to play.
   */
  public ClassicGame(GameType gameType, boolean hardDifficulty, int totalTimePerPlayerInSeconds) {
    super(gameType, hardDifficulty, totalTimePerPlayerInSeconds);
  }

  /**
   * Determines the winner by checking which piece colour occupies the most space on the board.
   *
   * @return the colour of piece occupying the most space. Null if they are equal.
   */
  @Override
  public PieceColour determineWinner() {
    // Black Wins if they have more pieces on the board than White.
    if (this.getBoard().getPieceCount(PieceColour.BLACK)
        > this.getBoard().getPieceCount(PieceColour.WHITE)) {
      return PieceColour.BLACK;
    }
    // White Wins if they have more pieces on the board than Black.
    if (this.getBoard().getPieceCount(PieceColour.BLACK)
        < this.getBoard().getPieceCount(PieceColour.WHITE)) {
      return PieceColour.WHITE;
    }
    // Draw if there are an equal number of Black and White pieces on the board.
    return null;
  }

}
