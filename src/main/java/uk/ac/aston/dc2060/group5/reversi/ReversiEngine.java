package uk.ac.aston.dc2060.group5.reversi;

import uk.ac.aston.dc2060.group5.reversi.gui.BoardUI;
import uk.ac.aston.dc2060.group5.reversi.model.Move;
import uk.ac.aston.dc2060.group5.reversi.players.CPUPlayer;
import uk.ac.aston.dc2060.group5.reversi.players.HumanPlayer;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.GameType;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

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
    run();
  }

  @Override
  public void run() {
    // Check if game is ended.
    if (this.game.getGameState().equals(AbstractGame.GameState.GAVE_OVER)) {
      this.gui.endGamePopup();
    } else {
      if (this.game.getGameType().equals(GameType.PVC) && this.game.getCurrentPlayer() instanceof CPUPlayer) {
        // Execute logic from the GUI without blocking it.
        new SwingWorker<String, Void>() {
          @Override
          protected String doInBackground() throws Exception {
            // Thinking delay for AI
            try {
              TimeUnit.SECONDS.sleep(new Random().nextInt(2) + 1);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }

            // Make move after delay.
            ((CPUPlayer) game.getCurrentPlayer()).takeTurn(game);
            afterMove();
            return null;
          }
        }.execute();
      }
    }
  }

  public void afterMove() {
    updateListeners();
    run();
  }

  public void highlightMoves() {
    List<Point> moves = Move.allPossibleMoves(game.getBoard(), game.getCurrentPlayer().getPlayerColour());

    // TODO Implement rest of this functionality to highlight all possible moves on the GUI.
  }

  public void clearHighlitedMoved() {
    // TODO Implement clearing of highlighted moves.
  }

  public void updateListeners() {
    for (BoardUI.TilePanel tiles : this.gui.getBoardPanel().getBoardTiles()) {
      if (tiles.getMouseListeners().length == 0) {
        tiles.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            // Execute logic from the GUI without blocking it.
            new SwingWorker<String, Void>() {
              @Override
              protected String doInBackground() throws Exception {
                // Only allow mouse clicks for HumanPlayer types.
                if (game.getCurrentPlayer() instanceof HumanPlayer) {
                  if (game.getCurrentPlayer().takeTurn(game, ((BoardUI.TilePanel) e.getSource()).getTileId())) {
                    System.out.println("Added piece to tile: " + (((BoardUI.TilePanel) e.getSource()).getTileId()));
                  } else {
                    System.out.println("Failed to add piece to tile.");
                  }
                  afterMove();
                }
                return null;
              }
            }.execute();
          }
        });
      }
    }
  }
}
