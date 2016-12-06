package uk.ac.aston.dc2060.group5.reversi;

import uk.ac.aston.dc2060.group5.reversi.gui.BoardUI;
import uk.ac.aston.dc2060.group5.reversi.players.CPUPlayer;
import uk.ac.aston.dc2060.group5.reversi.players.HumanPlayer;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.GameType;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Sam on 09/10/2016.
 */
public class ReversiEngine implements Runnable {

  private AbstractGame game;
  private BoardUI gui;

  public ReversiEngine(AbstractGame game, BoardUI gui) {
    this.game = game;
    this.gui = gui;
    updateListeners();
  }

  @Override
  public void run() {
    // Check if game is ended.
    if (this.game.getGameState().equals(AbstractGame.GameState.GAVE_OVER)) {
      this.gui.endGamePopup();
    } else {
      if (this.game.getGameType().equals(GameType.PVC) && this.game.getCurrentPlayer() instanceof CPUPlayer) {
        ((CPUPlayer) this.game.getCurrentPlayer()).takeTurn(this.game);
        afterMove();
      }
    }
  }

  public void afterMove() {
    updateListeners();
    run();
  }

  public void updateListeners() {
    for (BoardUI.TilePanel tiles : this.gui.getBoardPanel().getBoardTiles()) {
      if (tiles.getMouseListeners().length == 0) {
        tiles.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            // Only allow mouse clicks for HumanPlayer types.
            if (game.getCurrentPlayer() instanceof HumanPlayer) {
              if (game.getCurrentPlayer().takeTurn(game, ((BoardUI.TilePanel) e.getSource()).getTileId())) {
                System.out.println("Added piece to tile: " + (((BoardUI.TilePanel) e.getSource()).getTileId()));
              } else {
                System.out.println("Failed to add piece to tile.");
              }
              afterMove();
            }
          }
        });
      }
    }
  }
}
