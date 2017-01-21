package uk.ac.aston.dc2060.group5.reversi.players;

import junit.framework.TestCase;

import org.junit.Before;

import uk.ac.aston.dc2060.group5.reversi.model.Move;
import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.rulesets.ClassicGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.GameType;

import java.awt.Point;
import java.util.List;

/**
 * Created by Karan Thaker.
 */
public class PlayerTest extends TestCase {

  private CPUPlayer cpuPlayer;
  private HumanPlayer humanPlayer;
  private ClassicGame game;

  @Before
  public void setUp() {
    game = new ClassicGame(GameType.PVC, false);
    humanPlayer = (HumanPlayer) game.getPlayers()[0];
    cpuPlayer = (CPUPlayer) game.getPlayers()[1];
  }

  /**
   * Assert that a basic cpu player can make a valid move on it's own.
   */
  public void testCPUPlayerTakesTurn() {
    // Switch turn to cpu player
    game.switchPlayer();

    assertEquals(2, game.getBoard().getPieceCount(cpuPlayer.getPlayerColour()));

    cpuPlayer.takeTurn(game);

    assertEquals(4, game.getBoard().getPieceCount(cpuPlayer.getPlayerColour()));
  }

  /**
   * Assert that a difficult cpu player will always take a corner if it is available. The cpu will
   * take 4 moves in a row and will always pick a corner over the moves available in the center.
   * The diagram shows what the board starts and ends like in 4 cpu moves.
   *
   * - b w - - w b - *=> w w w - - w w w
   * - - - - - - - - *=> - - - - - - - -
   * - - - - - - - - *=> - - - - - - - -
   * - - - w b - - - *=> - - - w b - - -
   * - - - b w - - - *=> - - - b w - - -
   * - - - - - - - - *=> - - - - - - - -
   * - - - - - - - - *=> - - - - - - - -
   * - b w - - w b - *=> w w w - - w w w
   */
  public void testHardCPUPlayerCornerPlay() {
    cpuPlayer = new CPUPlayer(PieceColour.WHITE, true);
    game.switchPlayer();

    game.getBoard().addPiece(1, PieceColour.BLACK);
    game.getBoard().addPiece(2, PieceColour.WHITE);

    game.getBoard().addPiece(5, PieceColour.WHITE);
    game.getBoard().addPiece(6, PieceColour.BLACK);

    game.getBoard().addPiece(57, PieceColour.BLACK);
    game.getBoard().addPiece(58, PieceColour.WHITE);

    game.getBoard().addPiece(61, PieceColour.WHITE);
    game.getBoard().addPiece(62, PieceColour.BLACK);


    assertTrue(game.getBoard().getTile(0, 0).isVacant());
    cpuPlayer.takeTurn(game);
    assertFalse(game.getBoard().getTile(0, 0).isVacant());

    assertTrue(game.getBoard().getTile(0, 7).isVacant());
    cpuPlayer.takeTurn(game);
    assertFalse(game.getBoard().getTile(0, 7).isVacant());

    assertTrue(game.getBoard().getTile(7, 0).isVacant());
    cpuPlayer.takeTurn(game);
    assertFalse(game.getBoard().getTile(7, 0).isVacant());

    assertTrue(game.getBoard().getTile(7, 7).isVacant());
    cpuPlayer.takeTurn(game);
    assertFalse(game.getBoard().getTile(7, 7).isVacant());
  }

  /**
   * Asserts that a hard cpu player will always avoid giving away a corner if it has another option.
   * The cpu only has 2 move options in the first diagram below - (1,3) and (0,5). It will always
   * pick the latter since it will avoid allowing black to place a piece in the corner. The diagrams
   * show how the board starts and ends.
   *
   * - - - w b - - - *=> - - - w w w - -
   * w b - - - - - - *=> w b - - - - - -
   * - - b - - - - - *=> - - b - - - - -
   * - - - b b - - - *=> - - - b b - - -
   * - - - b b - - - *=> - - - b b - - -
   * - - - - - - - - *=> - - - - - - - -
   * - - - - - - - - *=> - - - - - - - -
   * - - - - - - - - *=> - - - - - - - -
   */
  public void testHardCPUAvoidsGivingCorner() {
    cpuPlayer = new CPUPlayer(PieceColour.WHITE, true);
    game.switchPlayer();

    game.getBoard().getTile(3, 3).getPiece().flipPiece();
    game.getBoard().getTile(4, 4).getPiece().flipPiece();

    game.getBoard().addPiece(3, PieceColour.WHITE);
    game.getBoard().addPiece(4, PieceColour.BLACK);
    game.getBoard().addPiece(8, PieceColour.WHITE);
    game.getBoard().addPiece(9, PieceColour.BLACK);
    game.getBoard().addPiece(18, PieceColour.BLACK);

    // Assert that the CPU currently has 2 possible moves.
    List<Point> cpuMoves = Move.allPossibleMoves(game.getBoard(), cpuPlayer.getPlayerColour());
    assertEquals(2, cpuMoves.size());


    /* Assert that the CPU only has one possible move after assessing if it can avoid giving away a
     * corner.
     */
    List<Point> newCpuMoves = cpuPlayer.avoidCornerOpportunity(cpuMoves, game);
    assertEquals(1, newCpuMoves.size());
  }

  /**
   * Assert that a human can make a valid move. Assert that trying to make the same move twice
   * cannot be done.
   */
  public void testHumanCanTakeTurn() {
    assertTrue(humanPlayer.takeTurn(game, 19));
    assertFalse(humanPlayer.takeTurn(game, 19));
  }
}
