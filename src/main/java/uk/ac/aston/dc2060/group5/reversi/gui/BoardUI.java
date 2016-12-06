package uk.ac.aston.dc2060.group5.reversi.gui;

import uk.ac.aston.dc2060.group5.reversi.model.Board;
import uk.ac.aston.dc2060.group5.reversi.model.Move;
import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.players.AbstractPlayer;
import uk.ac.aston.dc2060.group5.reversi.players.CPUPlayer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Creates the view of the Reversi playing board.
 *
 * <p>Created by Karan Thaker</p>
 */
public class BoardUI implements Observer {

  private JFrame mainWindow;
  private JMenuBar menuBar = createMenuBar();
  private BoardPanel boardPanel;
  private Board boardModel;
  private ScorePanel scorePanel;
  private CurrentTurnPanel currentTurnPanel;

  private final String PIECE_IMAGE_DIR = "/pieces";
  private final String PIECE_BLACK = "/piece_black.png";
  private final String PIECE_WHITE = "/piece_white.png";

  // Allows us to create a suitable size window for any screen resolution.
  private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

  private final int GAME_HEIGHT = SCREEN_SIZE.height * 4/5;
  private final int GAME_WIDTH = SCREEN_SIZE.height * 4/5;

  private AbstractPlayer[] players;

  public BoardUI(AbstractPlayer[] players) {
    this.players = players;
    this.boardModel = new Board(players);
    this.boardModel.addObserver(this);
    System.out.println(this.boardModel.toString());

    this.mainWindow = new JFrame("Reversi");
    this.mainWindow.setLayout(new BorderLayout());
    this.mainWindow.setSize(new Dimension(GAME_HEIGHT, GAME_WIDTH));
    this.mainWindow.setJMenuBar(menuBar);

    this.boardPanel = new BoardPanel();
    this.mainWindow.add(boardPanel, BorderLayout.CENTER);

    this.scorePanel = new ScorePanel();
    this.scorePanel.setLayout(new GridLayout(0, 1));
    this.mainWindow.add(scorePanel, BorderLayout.EAST);

    this.currentTurnPanel = new CurrentTurnPanel();
    this.mainWindow.add(currentTurnPanel, BorderLayout.SOUTH);

    this.mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.mainWindow.setResizable(false);
    this.mainWindow.setLocationRelativeTo(null);
    this.mainWindow.setVisible(true);
  }

  @Override
  public void update(Observable o, Object arg) {
    this.mainWindow.remove(boardPanel);
    boardPanel = new BoardPanel();
    this.mainWindow.add(boardPanel);
    this.mainWindow.validate();
    this.mainWindow.repaint();
    System.out.println(boardModel);

    this.currentTurnPanel.updatePlayer();
      this.scorePanel.updateScores();

    // Determine if the game is over.
    if ((boolean) arg) {
      Object[] options = { "Play Again?", "Main Menu", "Exit" };
      // Determine winner
      int optionPicked = 0;
      if (boardModel.getBlackPieceCount() > boardModel.getWhitePieceCount()) {
        optionPicked = JOptionPane.showOptionDialog(mainWindow,
            "Black Wins!",
            "Game Over",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[1]);
      } else {
        optionPicked = JOptionPane.showOptionDialog(mainWindow,
            "White Wins!",
            "Game Over",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[1]);
      }

      switch (optionPicked) {
        // Play Again
        case 0:
          this.mainWindow.dispose();
          new BoardUI(this.players);
          break;
        // Exit
        case 2:
          System.exit(0);
          break;
        // Main Menu
        case 1:
        default:
          this.mainWindow.dispose();
          try {
            new MainMenu();
          } catch (IOException e) {
            e.printStackTrace();
          }
          break;
      }

    }
  }

  private class BoardPanel extends JPanel {

    final List<TilePanel> boardTiles;

    BoardPanel() {
      super(new GridLayout(8, 8));
      this.boardTiles = new ArrayList<TilePanel>();
      int row = 0;
      for (int i = 0; i < 64; i++) {
        final TilePanel tilePanel = new TilePanel(this, i);
        this.boardTiles.add(tilePanel);

        if (i % 8 == 0 && i != 0) {
          row++;
        }

        if (row % 2 == 0) {
          if (i % 2 == 0) {
            tilePanel.setBackground(Color.decode("#2ecc71"));
          } else {
            tilePanel.setBackground(Color.decode("#27ae60"));
          }
        } else {
          if (i % 2 == 0) {
            tilePanel.setBackground(Color.decode("#27ae60"));
          } else {
            tilePanel.setBackground(Color.decode("#2ecc71"));
          }
        }

        add(tilePanel);
      }
      this.setPreferredSize(new Dimension(480, 480));
      this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      this.setBackground(Color.decode("#95a5a6"));
      this.validate();
    }
  }

  private class TilePanel extends JPanel {

    private final BoardPanel boardPanel;
    private final int tileId;

    TilePanel(final BoardPanel boardPanel, final int tileId) {
      this.boardPanel = boardPanel;
      this.tileId = tileId;
      this.setLayout(new BorderLayout());
      this.drawTileIcon(boardModel);
      this.addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (boardModel.addPiece(tileId)) {
            System.out.println("Added piece to tile: " + tileId);

            // Let AI take a turn if the game is against a CPUPlayer
            if (boardModel.getCurrentPlayer() instanceof CPUPlayer) {
              // Pick random valid move
              List<Point> moves = Move.allPossibleMoves(boardModel, boardModel.getCurrentPlayer().getPlayerColour());
              int upperBound = moves.size();
              Random random = new Random();
              int index = random.nextInt(upperBound);

              Point moveToMake = moves.get(index);
              boardModel.addPiece(boardModel.translatePointToIndex(moveToMake));
            }

          } else {
            System.out.println("Failed to add piece to tile.");
          }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
      });
    }

    private void drawTileIcon(Board board) {
      this.removeAll();
      if (!board.getTile(this.tileId).isVacant()) {
        PieceColour pieceColour = board.getTile(this.tileId).getPiece().getPieceColour();
        String imagePath = pieceColour.equals(PieceColour.BLACK) ? PIECE_BLACK : PIECE_WHITE;

        InputStream imageStream = this.getClass().getResourceAsStream(PIECE_IMAGE_DIR + imagePath);
        try {
          ImageIcon image = new ImageIcon(new ImageIcon(ImageIO.read(imageStream))
              .getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
          this.add(new JLabel(image));
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    }

  }



  private class CurrentTurnPanel extends JPanel {
    JLabel currentTurnLabel;

    CurrentTurnPanel() {
      super(new FlowLayout());
      this.currentTurnLabel = new JLabel();
      add(currentTurnLabel);
      currentTurnLabel.setText("Current player: " + boardModel.getCurrentPlayer().getPlayerColour());
      }

      private void updatePlayer() {
        currentTurnLabel.setText("Current player: " + boardModel.getCurrentPlayer().getPlayerColour());
      }
    }

  private class ScorePanel extends JPanel {

    public JPanel p1;
    public JPanel p2;

    public JLabel p1ScoreField;
    public JLabel p2ScoreField;

    ScorePanel() {
      p1 = new JPanel(new BorderLayout());
      p2 = new JPanel(new BorderLayout());
      JLabel p1ScoreLabel = new JLabel();
      JLabel p2ScoreLabel = new JLabel();
      p1ScoreField = new JLabel();
      p2ScoreField = new JLabel();
      p1.add(p1ScoreLabel, BorderLayout.NORTH);
      p1.add(p1ScoreField, BorderLayout.CENTER);
      p2.add(p2ScoreLabel, BorderLayout.NORTH);
      p2.add(p2ScoreField, BorderLayout.CENTER);
      add(p1);
      add(p2);
      p1ScoreLabel.setText("Player 1       ");
      p2ScoreLabel.setText("Player 2");
      p1ScoreLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
      p2ScoreLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
      p1ScoreField.setText(Integer.toString(boardModel.getBlackPieceCount()));
      p2ScoreField.setText(Integer.toString(boardModel.getWhitePieceCount()));
      p1ScoreField.setFont(new Font("Tahoma", Font.PLAIN, 100));
      p2ScoreField.setFont(new Font("Tahoma", Font.PLAIN, 100));

      p1.setBackground(Color.decode("#1d1d1d"));
      p1ScoreLabel.setForeground(Color.decode("#ecf0f1"));
      p1ScoreField.setForeground(Color.decode("#ecf0f1"));

      p2.setBackground(Color.decode("#ecf0f1"));
      p2ScoreLabel.setForeground(Color.decode("#1d1d1d"));
      p2ScoreField.setForeground(Color.decode("#1d1d1d"));
    }

    private void updateScores() {
        p1ScoreField.setText(Integer.toString(boardModel.getBlackPieceCount()));
        p2ScoreField.setText(Integer.toString(boardModel.getWhitePieceCount()));
    }
  }

  private JMenuBar createMenuBar() {


    JMenuBar menubar = new JMenuBar();

    JMenu menu = new JMenu("Menu");
    JMenu help = new JMenu("Help");

    menu.setMnemonic(KeyEvent.VK_M);
    help.setMnemonic(KeyEvent.VK_H);

    JMenuItem backToMainMenuItem  = new JMenuItem("Main Menu");
    backToMainMenuItem.setToolTipText("Go back to main menu");
    backToMainMenuItem.addActionListener((ActionEvent event) -> {
      try {
        mainWindow.dispose();
        new MainMenu();
      }catch (IOException ignored){}

    });

    JMenuItem exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.setToolTipText("Exit application");
    exitMenuItem.addActionListener((ActionEvent event) -> {
      System.exit(0);
    });

    JMenuItem rulesMenuItem = new JMenuItem("Rules");
    rulesMenuItem.setToolTipText("Learn the rules of Reversi");
    rulesMenuItem.addActionListener((ActionEvent event) -> {
      new RulesUI();
    });

    menu.add(backToMainMenuItem);
    menu.add(exitMenuItem);
    help.add(rulesMenuItem);

    menubar.add(menu);
    menubar.add(help);

    return menubar;

  }
}
