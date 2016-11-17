package uk.ac.aston.dc2060.group5.reversi.gui;

import uk.ac.aston.dc2060.group5.reversi.model.Board;
import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.players.AbstractPlayer;
import uk.ac.aston.dc2060.group5.reversi.players.HumanPlayer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Creates the view of the Reversi playing board.
 *
 * <p>Created by Karan Thaker</p>
 */
public class BoardUI implements Observer {

  private JFrame mainWindow;
  private BoardPanel boardPanel;
  private Board boardModel;
  private ScorePanel scorePanel;
  private CurrentTurnPanel currentTurnPanel;

  private final String PIECE_IMAGE_DIR = "/pieces";
  private final String PIECE_BLACK = "/piece_black.png";
  private final String PIECE_WHITE = "/piece_white.png";

  public BoardUI() { AbstractPlayer[] players = { new HumanPlayer(PieceColour.BLACK), new HumanPlayer(PieceColour.WHITE) };
    this.boardModel = new Board(players);
    this.boardModel.addObserver(this);
    System.out.println(this.boardModel.toString());

    this.mainWindow = new JFrame("Reversi");
    this.mainWindow.setLayout(new BorderLayout());
    this.mainWindow.setSize(new Dimension(640, 640));

    this.boardPanel = new BoardPanel();
    this.mainWindow.add(boardPanel, BorderLayout.CENTER);

    this.scorePanel = new ScorePanel();
    this.scorePanel.setLayout(new GridLayout(0,2));
    this.mainWindow.add(scorePanel, BorderLayout.EAST);

    this.currentTurnPanel = new CurrentTurnPanel();
    this.mainWindow.add(currentTurnPanel, BorderLayout.SOUTH);

    this.mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.mainWindow.setVisible(true);


  }

  @Override
  public void update(Observable o, Object arg) {
    System.out.println("View: Added " + (PieceColour) arg + " piece to " + o.getClass());
    this.mainWindow.remove(boardPanel);
    boardPanel = new BoardPanel();
    this.mainWindow.add(boardPanel);
    this.mainWindow.validate();
    this.mainWindow.repaint();
    System.out.println(boardModel);

    this.currentTurnPanel.updatePlayer();
      this.scorePanel.updateScores();
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

      JLabel p1ScoreField;
      JLabel p2ScoreField;

    ScorePanel() {
      super(new FlowLayout());
      JLabel p1ScoreLabel = new JLabel();
      JLabel p2ScoreLabel = new JLabel();
      p1ScoreField = new JLabel();
      p2ScoreField = new JLabel();
      add(p1ScoreLabel);
      add(p1ScoreField);
      add(p2ScoreLabel);
      add(p2ScoreField);
      p1ScoreLabel.setText("Player 1: ");
      p2ScoreLabel.setText("Player 2: ");
      p1ScoreField.setText(Integer.toString(boardModel.getBlackPieceCount()));
      p2ScoreField.setText(Integer.toString(boardModel.getWhitePieceCount()));
    }

    private void updateScores() {
        p1ScoreField.setText(Integer.toString(boardModel.getBlackPieceCount()));
        p2ScoreField.setText(Integer.toString(boardModel.getWhitePieceCount()));
    }
  }
}
