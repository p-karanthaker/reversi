package uk.ac.aston.dc2060.group5.reversi.gui;

import uk.ac.aston.dc2060.group5.reversi.model.Board;
import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Creates the view of the Reversi playing board.
 *
 * Created by Karan Thaker
 * Task 2 work: Dean Sohn
 */
public class BoardUI implements Observer {

  private JFrame mainWindow;
  private BoardPanel boardPanel;
  private Board boardModel;

  private final Path PIECE_IMAGE_PATH = Paths.get("pieces/");
  private final Path PIECE_BLACK_PATH = Paths.get("piece_black.png");
  private final Path PIECE_WHITE_PATH = Paths.get("piece_white.png");

  public BoardUI() {
    this.boardModel = new Board();
    System.out.println(this.boardModel.toString());
    this.boardModel.addObserver(this);
    
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
      int row = 0;
      for (int i = 0; i < 64; i++) {
        final TilePanel tilePanel = new TilePanel(this, i);
        this.boardTiles.add(tilePanel);

        if (i % 8 == 0 && i != 0)
          row++;

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
      }
      this.setPreferredSize(new Dimension(480, 480));
      this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      this.setBackground(Color.decode("#95a5a6"));
      this.validate();
    }
  }

  private class TilePanel extends JPanel implements MouseListener {

    private final BoardPanel boardPanel;
    private final int tileId;
    private MouseListener mouseListener;

    TilePanel(final BoardPanel boardPanel, final int tileId) {
      this.boardPanel = boardPanel;
      this.tileId = tileId;
      this.setLayout(new BorderLayout());
      this.drawTileIcon(boardModel);
    }

    private void drawTileIcon(Board board) {
      this.removeAll();
      if (!board.getTile(this.tileId).isVacant()) {
        PieceColour pieceColour = board.getTile(this.tileId).getPiece().getPieceColour();
        Path iPath = pieceColour.equals(PieceColour.BLACK) ? PIECE_BLACK_PATH : PIECE_WHITE_PATH;

        InputStream imageStream = this.getClass().getClassLoader()
            .getResourceAsStream(PIECE_IMAGE_PATH.resolve(iPath).toString());
        try {
          ImageIcon image = new ImageIcon(new ImageIcon(ImageIO.read(imageStream))
              .getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
          this.add(new JLabel(image));
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    }
    
    @SuppressWarnings("unused")
	private void createMouseListener() {
    	this.addMouseListener(mouseListener);
    	
    }

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Adding Piece to Tile");
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

  }

public void update(Observable o, Object arg) {
	// TODO Auto-generated method stub
	this.boardPanel.removeAll();
	this.boardPanel = new BoardPanel();
	this.mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.mainWindow.setVisible(true);
    this.mainWindow.validate();
    this.mainWindow.repaint();
	}

}
