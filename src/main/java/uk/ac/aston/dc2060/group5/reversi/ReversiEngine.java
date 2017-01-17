package uk.ac.aston.dc2060.group5.reversi;

import uk.ac.aston.dc2060.group5.reversi.gui.BoardUI;
import uk.ac.aston.dc2060.group5.reversi.gui.MainMenu;
import uk.ac.aston.dc2060.group5.reversi.gui.RulesUI;
import uk.ac.aston.dc2060.group5.reversi.gui.SettingsUI;
import uk.ac.aston.dc2060.group5.reversi.model.Move;
import uk.ac.aston.dc2060.group5.reversi.players.CPUPlayer;
import uk.ac.aston.dc2060.group5.reversi.players.HumanPlayer;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.GameType;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
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
    this.oneTimeListeners();
    this.updateListeners();
    this.run();
  }

  @Override
  public void run() {
    // Check if game is aborted.
    if (this.game.getGameState().equals(AbstractGame.GameState.ABORTED)) {
      Thread.currentThread().interrupt();
      return;
    }

    // Check if game is ended.
    if (this.game.getGameState().equals(AbstractGame.GameState.GAVE_OVER)) {
      this.gui.endGamePopup();
    } else {
      if (!this.game.getGameType().equals(GameType.PVP) && this.game.getCurrentPlayer() instanceof CPUPlayer) {
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
    System.out.println(this.game.getBoard());
    updateListeners();
    passTurn();
    game.update();
    run();
  }

  public void passTurn() {
    this.game.switchPlayer();

    // Check if the new player can make a legal move, and pass back turn if they can't
    if (Move.allPossibleMoves(this.game.getBoard(), this.game.getCurrentPlayer().getPlayerColour()).size() == 0) {
      this.game.switchPlayer();
      if (Move.allPossibleMoves(this.game.getBoard(), this.game.getCurrentPlayer().getPlayerColour()).size() == 0) {
        // End game if both players cannot make a legal move.
        this.game.setGameState(AbstractGame.GameState.GAVE_OVER);
      }
    }
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
                    afterMove();
                  } else {
                    System.out.println("Failed to add piece to tile.");
                  }
                }
                return null;
              }
            }.execute();
          }
        });
      }
    }


  }

  /**
   * Listeners which do not need to be refreshed.
   */
  public void oneTimeListeners() {
    this.gui.backToMainMenuItem.addActionListener((ActionEvent event) -> {
      try {
        this.game.setGameState(AbstractGame.GameState.ABORTED);
        this.gui.mainWindow.dispose();
        this.game = null;
        this.gui = null;
        new MainMenu();
      } catch (IOException ignored) {
      }

    });

    this.gui.exitMenuItem.addActionListener((ActionEvent event) -> {
      System.exit(0);
    });

    this.gui.rulesMenuItem.addActionListener((ActionEvent event) -> {
      new RulesUI();
    });

    this.gui.settingsMenuItem.addActionListener((ActionEvent event) -> {
      new SettingsUI(this.gui);
    });
  }
}
