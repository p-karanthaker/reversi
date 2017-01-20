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

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

/**
 * Controls the game play of Reversi.
 *
 * <p>Created by Karan Thaker.</p>
 */
public class ReversiEngine implements Runnable {

  /**
   * The game to be controlled by the engine.
   */
  private AbstractGame game;

  /**
   * The gui to be controlled by the game.
   */
  private BoardUI gui;

  /**
   * The game timer.
   */
  private Timer gameTimer;

  /**
   * Constructor for the controller.
   *
   * @param game the game being controlled.
   * @param gui  the gui being controlled.
   */
  public ReversiEngine(AbstractGame game, BoardUI gui) {
    this.game = game;
    this.gui = gui;
    this.oneTimeListeners();
    this.updateListeners();
    this.run();
  }

  /**
   * Runs after every move to print the state of the board to the console, check if the game has
   * been aborted, check if the game is over, start the time in a timed game, and allow AI to make
   * a move.
   */
  @Override
  public void run() {
    System.out.println(this.game.getBoard());
    // Check if game is aborted.
    if (this.game.getGameState().equals(AbstractGame.GameState.ABORTED)) {
      Thread.currentThread().interrupt();
      return;
    }

    // Check if game is ended.
    if (this.game.getGameState().equals(AbstractGame.GameState.GAVE_OVER)) {
      this.gui.endGamePopup();
      Thread.currentThread().interrupt();
      return;
    } else {
      // Start the timer if it's a timed game.
      if (this.game.isTimedGame()) {
        startTimer();
      }

      if (!this.game.getGameType().equals(GameType.PVP)
          && this.game.getCurrentPlayer() instanceof CPUPlayer) {
        // Execute logic from the GUI without blocking it.
        new SwingWorker<String, Void>() {
          @Override
          protected String doInBackground() throws Exception {
            // Thinking delay for AI
            try {
              TimeUnit.SECONDS.sleep(new Random().nextInt(2) + 1);
            } catch (InterruptedException ex) {
              ex.printStackTrace();
            } finally {
              // Must check if the computer can still make a move after the delay.
              if (game.isTimedGame() && game.getCurrentPlayer().getTimeLeftToPlayInSeconds() <= 0) {
                return null;
              }
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

  /**
   * Should be run after every move. Pauses the timer for the player who just moved, updates the
   * listeners on the board tiles, passes the turn to the opposing player, updates the GUI, and then
   * invokes the run() method.
   */
  public void afterMove() {
    if (this.game.isTimedGame()) {
      // Pause timer first
      this.pauseTimer();
    }

    // Update all listeners
    this.updateListeners();

    // Pass the turn before updating the GUI otherwise it will display the wrong person's turn.
    this.passTurn();

    // Update GUI
    this.game.update();

    /* Check if game is over/aborted, start the timer if game is in progress, let AI take a move
       if possible. */
    this.run();
  }

  /**
   * Starts the timer for the player who is taking their turn. Ends their turn once the timer
   * reaches 0.
   */
  public void startTimer() {
    this.gameTimer = new Timer();
    this.gameTimer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        if (game.getCurrentPlayer().getTimeLeftToPlayInSeconds() <= 0) {
          afterMove();
          return;
        }

        int time = game.getCurrentPlayer().getTimeLeftToPlayInSeconds();
        game.getCurrentPlayer().setTimeLeftToPlayInSeconds(--time);
        game.update();
      }
    }, 0, 1000);
  }

  /**
   * Pauses the timer for the player who just took their turn.
   */
  public void pauseTimer() {
    this.gameTimer.cancel();
  }

  /**
   * Attempts to pass the turn to the opposing player by carrying out various conditional checks.
   */
  public void passTurn() {
    this.game.switchPlayer();

    // Does the new player have time to play?
    if (this.game.getCurrentPlayer().getTimeLeftToPlayInSeconds() > 0) {
      // Can the new player make a legal move?
      if (Move.allPossibleMoves(this.game.getBoard(), this.game.getCurrentPlayer().getPlayerColour()).size() > 0) {
        // Let them play
      } else {
        // Can the other player make a legal move?
        this.game.switchPlayer();
        if (Move.allPossibleMoves(this.game.getBoard(), this.game.getCurrentPlayer().getPlayerColour()).size() > 0) {
          // Do they have time to play?
          if (this.game.getCurrentPlayer().getTimeLeftToPlayInSeconds() > 0) {
            // Let them play
          } else {
            // End Game
            this.game.setGameState(AbstractGame.GameState.GAVE_OVER);
          }
        } else {
          // End Game
          this.game.setGameState(AbstractGame.GameState.GAVE_OVER);
        }
      }
    } else {
      // Does the other player have time to play?
      this.game.switchPlayer();
      if (this.game.getCurrentPlayer().getTimeLeftToPlayInSeconds() > 0) {
        // Can they make a legal move?
        if (Move.allPossibleMoves(this.game.getBoard(), this.game.getCurrentPlayer().getPlayerColour()).size() > 0) {
          // Let them play
        } else {
          // End Game
          this.game.setGameState(AbstractGame.GameState.GAVE_OVER);
        }
      } else {
        // End Game
        this.game.setGameState(AbstractGame.GameState.GAVE_OVER);
      }
    }
  }

  /**
   * Updates the listeners on each board tile so a human player can click on the tiles.
   */
  public void updateListeners() {
    for (BoardUI.TilePanel tiles : this.gui.getBoardPanel().getBoardTiles()) {
      if (tiles.getMouseListeners().length == 0) {
        tiles.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent event) {
            // Execute logic from the GUI without blocking it.
            new SwingWorker<String, Void>() {
              @Override
              protected String doInBackground() throws Exception {
                // Only allow mouse clicks for HumanPlayer types.
                if (game.getCurrentPlayer() instanceof HumanPlayer) {
                  if (game.getCurrentPlayer().takeTurn(game, ((BoardUI.TilePanel) event.getSource()).getTileId())) {
                    afterMove();
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
        if (this.game.isTimedGame()) {
          this.pauseTimer();
        }
        this.game = null;
        this.gui = null;
        new MainMenu();
      } catch (IOException ignored) {
        ignored.printStackTrace();
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
