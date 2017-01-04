package uk.ac.aston.dc2060.group5.reversi.gui;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uk.ac.aston.dc2060.group5.reversi.ReversiEngine;
import uk.ac.aston.dc2060.group5.reversi.model.Board;
import uk.ac.aston.dc2060.group5.reversi.model.Piece.PieceColour;
import uk.ac.aston.dc2060.group5.reversi.players.AbstractPlayer;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AntiReversi;
import uk.ac.aston.dc2060.group5.reversi.rulesets.ClassicGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Creates the view of the Reversi playing board.
 *
 * <p>Created by Karan Thaker</p>
 */
public class BoardUI implements Observer {

  private AbstractGame game;

  public JFrame mainWindow;
  private JMenuBar menuBar = createMenuBar();
  private BoardPanel boardPanel;
  private ScorePanel scorePanel;
  private CurrentTurnPanel currentTurnPanel;

  private JsonObject theme;
  private Image bg;

  // Allows us to create a suitable size window for any screen resolution.
  private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

  private final int GAME_HEIGHT = SCREEN_SIZE.height * 4/5;
  private final int GAME_WIDTH = SCREEN_SIZE.height * 4/5;

  private AbstractPlayer[] players;

  public BoardUI(AbstractGame game) {
    Gson gson = new Gson();
    JsonElement jsonElement = null;
    Path configPath = Paths.get(System.getProperty("user.home") + File.separator + "reversi" + File.separator + "config.json");
    System.out.println(configPath);
    if (!Files.exists(configPath)) {
      try {
        Files.createDirectories(configPath.getParent());
        Files.createFile(configPath);

        InputStream is = this.getClass().getResourceAsStream("/config.json");
        OutputStream os = new FileOutputStream(configPath.toFile());
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        os.write(buffer);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try {
      InputStream is = new FileInputStream(configPath.toFile());
      Reader reader = new InputStreamReader(is, "UTF-8");
      jsonElement = gson.fromJson(reader, JsonElement.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    JsonObject jsonObject = jsonElement.getAsJsonObject();
    System.out.println(jsonObject);
    this.theme = jsonObject.getAsJsonObject("theme");

    if (!this.theme.get("bgImage").toString().equals("null") && !this.theme.get("bgImage").getAsString().isEmpty()) {
      InputStream bgStream = this.getClass().getResourceAsStream("/themes/" + this.theme.get("bgImage").getAsString());
      try {
        this.bg = ImageIO.read(bgStream);
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }

    this.game = game;
    this.game.addObserver(this);
    System.out.println(this.game.getBoard().toString());

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

  public void endGamePopup() {
    Object[] options = { "Play Again?", "Main Menu", "Exit" };
    // Determine winner
    int optionPicked = 0;
    String popupMessageText = null;

    switch (this.game.determineWinner()) {
      case BLACK:
        popupMessageText = "Black Wins!";
        break;
      case WHITE:
        popupMessageText = "White Wins!";
        break;
      default:
        popupMessageText = "The game was a tie!";
        break;
    }

    optionPicked = JOptionPane.showOptionDialog(mainWindow,
        popupMessageText,
        "Game Over",
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE,
        null,
        options,
        options[1]);

    switch (optionPicked) {
      // Play Again
      case 0:
        this.mainWindow.dispose();
        AbstractGame newGame;
        if (game instanceof ClassicGame) {
          newGame = new ClassicGame(game.getGameType());
        } else {
          newGame = new AntiReversi(game.getGameType());
        }
        new ReversiEngine(newGame, new BoardUI(newGame));
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

  public BoardPanel getBoardPanel() {
    return this.boardPanel;
  }

  @Override
  public void update(Observable o, Object arg) {
    // Update tiles
    for (TilePanel tilePanel : this.boardPanel.getBoardTiles()) {
      tilePanel.revalidate();
      tilePanel.repaint();
    }

    // Update turn
    this.currentTurnPanel.updatePlayer();

    // Update scores
    this.scorePanel.updateScores();
  }

  public class BoardPanel extends JPanel {

    final List<TilePanel> boardTiles;

    BoardPanel() {
      super(new GridLayout(8, 8));

      // Default background
      if (!theme.get("bgColour").toString().equals("null") && !theme.get("bgColour").getAsString().isEmpty()) {
        this.setBackground(Color.decode(theme.get("bgColour").getAsString()));
      }

      this.boardTiles = new ArrayList<TilePanel>();
      int row = 0;
      for (int i = 0; i < 64; i++) {
        final TilePanel tilePanel = new TilePanel(this, i);
        this.boardTiles.add(tilePanel);

        if (i % 8 == 0 && i != 0) {
          row++;
        }

        JsonObject opacityObject = theme.getAsJsonArray("opacity").get(0).getAsJsonObject();
        if (row % 2 == 0) {
          if (i % 2 == 0) {
            tilePanel.setBackground(new Color(0, 0, 0, opacityObject.get("light").getAsInt()));
          } else {
            tilePanel.setBackground(new Color(0, 0, 0, opacityObject.get("dark").getAsInt()));
          }
        } else {
          if (i % 2 == 0) {
            tilePanel.setBackground(new Color(0, 0, 0, opacityObject.getAsJsonObject().get("dark").getAsInt()));
          } else {
            tilePanel.setBackground(new Color(0, 0, 0, opacityObject.getAsJsonObject().get("light").getAsInt()));
          }
        }
        add(tilePanel);
      }

      this.validate();
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (bg != null) {
        g.drawImage(bg, 0, 0, null);
      }
    }

    public List<TilePanel> getBoardTiles() {
      return this.boardTiles;
    }
  }

  public class TilePanel extends JPanel {

    private final BoardPanel boardPanel;
    private final int tileId;
    private Board board;

    TilePanel(final BoardPanel boardPanel, final int tileId) {
      this.boardPanel = boardPanel;
      this.tileId = tileId;
      this.setLayout(new BorderLayout());
      this.setOpaque(false);
      this.board = game.getBoard();
    }

    public int getTileId() {
      return this.tileId;
    }

    protected void paintComponent(Graphics g) {
      g.setColor(getBackground());
      Rectangle r = g.getClipBounds();
      g.fillRect(r.x, r.y, r.width, r.height);

      if (!board.getTile(this.tileId).isVacant()) {
        PieceColour pieceColour = board.getTile(this.tileId).getPiece().getPieceColour();
        int h = getHeight();
        int w = getWidth();

        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Retain old paint
        Paint oldPaint = g2.getPaint();
        Color colour;

        // Fill circle with solid colour - Black or White
        JsonObject pieceColourObject = theme.getAsJsonArray("pieceColours").get(0).getAsJsonObject();
        if (pieceColour.equals(PieceColour.BLACK)) {
          String black = pieceColourObject.get("black").getAsString();
          colour = Color.decode(black);
          g2.setColor(colour);
        } else {
          String white = pieceColourObject.get("white").getAsString();
          colour = Color.decode(white);
          g2.setColor(colour);
        }
        // Fills the circle with solid color
        g2.fillOval(1, h/32, w-1, w-1);

        // Adds shadows at the top
        Paint p;
        p = new GradientPaint(0, 0, new Color(0.0f, 0.0f, 0.0f, 0.4f), 0, getHeight(), new Color(0.0f, 0.0f, 0.0f, 0.0f));
        g2.setPaint(p);
        g2.fillOval(1, h/32, w-1, w-1);

        // Adds highlights at the bottom
        p = new GradientPaint(0, 0, new Color(1.0f, 1.0f, 1.0f, 0.0f), 0, getHeight(), new Color(1.0f, 1.0f, 1.0f, 0.4f));
        g2.setPaint(p);
        g2.fillOval(1, h/32, w-1, w-1);

        // Creates dark edges for 3D effect
        p = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0,
            getHeight() / 2.0), getWidth() / 2.0f,
            new float[] { 0.0f, 1.0f },
            new Color[] { colour, new Color(0.0f, 0.0f, 0.0f, 0.1f) });
        g2.setPaint(p);
        g2.fillOval(1, h/32, w-1, w-1);

        // Adds oval inner highlight at the bottom
        int rTint = colour.getRed() + (255 - colour.getRed()) * 0;
        int gTint = colour.getGreen() + (255 - colour.getGreen()) * 0;
        int bTint = colour.getBlue() + (255 - colour.getBlue()) * 0;

        p = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0,
            getHeight() * 1.5), getWidth() / 2.3f,
            new Point2D.Double(getWidth() / 2.0, getHeight() * 1.75 + 6),
            new float[] { 0.0f, 0.8f },
            new Color[] { colour, new Color(rTint, gTint, bTint, 0) },
            RadialGradientPaint.CycleMethod.NO_CYCLE,
            RadialGradientPaint.ColorSpaceType.SRGB,
            AffineTransform.getScaleInstance(1.0, 0.5));
        g2.setPaint(p);
        g2.fillOval(1, h/32, w-1, w-1);

        // Adds oval specular highlight at the top left
        p = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0,
            getHeight() / 2.0), getWidth() / 1.4f,
            new Point2D.Double(45.0, 25.0),
            new float[] { 0.0f, 0.5f },
            new Color[] { new Color(1.0f, 1.0f, 1.0f, 0.4f), new Color(1.0f, 1.0f, 1.0f, 0.0f) },
            RadialGradientPaint.CycleMethod.NO_CYCLE);
        g2.setPaint(p);
        g2.fillOval(1, h/32, w-1, w-1);

        // Restores the previous state
        g2.setPaint(oldPaint);

      } else {
        super.paintComponent(g);
      }
    }

  }



  private class CurrentTurnPanel extends JPanel {
    JLabel currentTurnLabel;

    CurrentTurnPanel() {
      super(new FlowLayout());
      this.currentTurnLabel = new JLabel();
      add(currentTurnLabel);
      currentTurnLabel.setText("Current player: " + game.getCurrentPlayer().getPlayerColour());
      }

      private void updatePlayer() {
        currentTurnLabel.setText("Current player: " + game.getCurrentPlayer().getPlayerColour());
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
      p1ScoreField.setText(Integer.toString(game.getBoard().getPieceCount(PieceColour.BLACK)));
      p2ScoreField.setText(Integer.toString(game.getBoard().getPieceCount(PieceColour.WHITE)));
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
        p1ScoreField.setText(Integer.toString(game.getBoard().getPieceCount(PieceColour.BLACK)));
        p2ScoreField.setText(Integer.toString(game.getBoard().getPieceCount(PieceColour.WHITE)));
    }
  }

  private JMenuBar createMenuBar() {


    JMenuBar menubar = new JMenuBar();

    JMenu menu = new JMenu("Menu");
    JMenu options = new JMenu("Options");

    menu.setMnemonic(KeyEvent.VK_M);
    options.setMnemonic(KeyEvent.VK_O);

    JMenuItem backToMainMenuItem = new JMenuItem("Main Menu");
    backToMainMenuItem.setToolTipText("Go back to main menu");
    backToMainMenuItem.addActionListener((ActionEvent event) -> {
      try {
        this.game.setGameState(AbstractGame.GameState.ABORTED);
        this.mainWindow.dispose();
        new MainMenu();
      } catch (IOException ignored) {
      }

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

    JMenuItem settingsMenuItem = new JMenuItem("Settings");
    settingsMenuItem.setToolTipText("Change the theme of the Reversi board.");
    settingsMenuItem.addActionListener((ActionEvent event) -> {
      new SettingsUI(this);
    });

    menu.add(backToMainMenuItem);
    menu.add(exitMenuItem);
    options.add(rulesMenuItem);
    options.add(settingsMenuItem);

    menubar.add(menu);
    menubar.add(options);

    return menubar;

  }
}
