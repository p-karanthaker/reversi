package uk.ac.aston.dc2060.group5.reversi.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

/**
 * Created by Sam on 09/10/2016.
 */
public class BoardUI {

  private JFrame mainWindow;
  private BoardPanel boardPanel;

  public BoardUI() {
    this.mainWindow = new JFrame("Reversi");
    this.mainWindow.setLayout(new BorderLayout());
    this.mainWindow.setSize(new Dimension(640, 640));
    this.boardPanel = new BoardPanel();
    this.mainWindow.add(boardPanel, BorderLayout.CENTER);
    this.mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.mainWindow.setVisible(true);
  }

  private class BoardPanel extends JPanel {

    final List<TilePanel> boardTiles;

    BoardPanel() {
      super(new GridLayout(8, 8));
      this.boardTiles = new ArrayList<TilePanel>();
      int row = 1;
      for (int i = 1; i < 65; i++) {
        final TilePanel tilePanel = new TilePanel(this, i);
        this.boardTiles.add(tilePanel);

        if (row % 2 == 0) {
          if (i % 2 == 0)
            tilePanel.setBackground(Color.decode("#2ecc71"));
          else
            tilePanel.setBackground(Color.decode("#27ae60"));
        } else {
          if (i % 2 == 0)
            tilePanel.setBackground(Color.decode("#27ae60"));
          else
            tilePanel.setBackground(Color.decode("#2ecc71"));
        }


        add(tilePanel);
        if (i % 8 == 0)
          row++;
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
      this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

  }

}
