package uk.ac.aston.dc2060.group5.reversi.rulesets;

import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;

/**
 * Created by Sam on 09/10/2016.
 */
public class AntiReversi extends AbstractGame {

  public AntiReversi(GameType gameType) {
    super(gameType, false);
  }

  public AntiReversi(GameType gameType, int totalTimePerPlayerInSeconds) {
    super(gameType, false, totalTimePerPlayerInSeconds);
  }

  @Override
  public PieceColour determineWinner() {
    // Black Wins if they have less pieces on the board than White.
    if (this.getBoard().getPieceCount(PieceColour.BLACK) < this.getBoard().getPieceCount(PieceColour.WHITE)) {
      return PieceColour.BLACK;
    }
    // White Wins if they have less pieces on the board than Black.
    if (this.getBoard().getPieceCount(PieceColour.BLACK) > this.getBoard().getPieceCount(PieceColour.WHITE)) {
      return PieceColour.WHITE;
    }
    // Draw if there are an equal number of Black and White pieces on the board.
    return null;
  }

}
