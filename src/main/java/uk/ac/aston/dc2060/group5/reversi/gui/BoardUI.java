package uk.ac.aston.dc2060.group5.reversi.gui;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import uk.ac.aston.dc2060.group5.reversi.ReversiEngine;
import uk.ac.aston.dc2060.group5.reversi.Settings;
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
import java.awt.FontFormatException;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
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
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * Creates the view of the Reversi playing board.
 *
 * <p>Created by Karan Thaker</p>
 */
public class BoardUI implements Observer {

  public AbstractGame game;

  public JFrame mainWindow;

  private JMenuBar menuBar = createMenuBar();
  public JMenuItem backToMainMenuItem;
  public JMenuItem exitMenuItem;
  public JMenuItem rulesMenuItem;
  public JMenuItem settingsMenuItem;

  private BoardPanel boardPanel;
  private PlayerInfoPanel infoPanel;
  private CurrentTurnPanel currentTurnPanel;

  private Image bg;
  private Settings theme;

  // Allows us to create a suitable size window for any screen resolution.
  private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

  private final int GAME_HEIGHT = SCREEN_SIZE.height * 4/5;
  private final int GAME_WIDTH = GAME_HEIGHT * 86/100;

  private AbstractPlayer[] players;

  public BoardUI(AbstractGame game) {
    this.loadTheme();

    this.game = game;
    this.game.addObserver(this);

    this.mainWindow = new JFrame("Reversi");
    this.mainWindow.setLayout(new BorderLayout());
    this.mainWindow.setSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    this.mainWindow.setJMenuBar(menuBar);

    this.boardPanel = new BoardPanel();
    this.mainWindow.add(boardPanel, BorderLayout.CENTER);

    /*this.scorePanel = new ScorePanel();
    this.scorePanel.setLayout(new GridLayout(0, 1));
    this.mainWindow.add(scorePanel, BorderLayout.EAST);
*/
    /*this.timePanel = new TimePanel();
    this.timePanel.setLayout(new GridLayout(1, 0));
    this.mainWindow.add(timePanel, BorderLayout.NORTH);*/

    infoPanel = new PlayerInfoPanel();
    infoPanel.setLayout(new GridLayout(1, 0));
    this.mainWindow.add(infoPanel, BorderLayout.NORTH);

    this.currentTurnPanel = new CurrentTurnPanel();
    this.mainWindow.add(currentTurnPanel, BorderLayout.SOUTH);

    this.mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.mainWindow.setResizable(false);
    this.mainWindow.setLocationRelativeTo(null);
    this.mainWindow.setVisible(true);
  }

  public void loadTheme() {
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
      theme = gson.fromJson(reader, Settings.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (this.theme.getBgImage() != null && !this.theme.getBgImage().isEmpty()) {
      InputStream bgStream = this.getClass().getResourceAsStream("/themes/" + this.theme.getBgImage());
      try {
        this.bg = ImageIO.read(bgStream);
      } catch (final IOException e) {
        e.printStackTrace();
      }
    } else {
      this.bg = null;
    }
  }

  public void endGamePopup() {
    Object[] options = { "Play Again?", "Main Menu", "Exit" };
    // Determine winner
    int optionPicked = 0;
    String popupMessageText = null;

    PieceColour winner = this.game.determineWinner();

    if (winner == null) {
      popupMessageText = "The game was a tie!";
    } else {
      popupMessageText = winner.getLongName() + " Wins!";
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
          newGame = new ClassicGame(game.getGameType(), game.getDifficulty());
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
    // Redraw board
    boardPanel.repaint();

    // Update turn
    this.currentTurnPanel.repaint();

    // Update time and scores
    this.infoPanel.repaint();
  }

  public class BoardPanel extends JPanel {

    final List<TilePanel> boardTiles;

    BoardPanel() {
      super(new GridLayout(8, 8));

      this.boardTiles = new ArrayList<TilePanel>();

      for (int i = 0; i < 64; i++) {
        final TilePanel tilePanel = new TilePanel(this, i);
        this.boardTiles.add(tilePanel);
        add(tilePanel);
      }

      this.validate();
    }

    @Override
    protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      super.paintComponent(g2);
      if (bg != null) {
        g2.drawImage(bg, 0, 0, null);
      } else {
        this.setBackground(Color.decode(theme.getBgColour()));
      }

      int row = 0;
      int i = 0;
      for (TilePanel tilePanel : boardTiles) {
        if (i % 8 == 0 && i != 0) {
          row++;
        }
        Settings.Opacity opacity = theme.getOpacity()[0];
        if (row % 2 == 0) {
          if (i % 2 == 0) {
            tilePanel.setBackground(new Color(0, 0, 0, opacity.getLight()));
          } else {
            tilePanel.setBackground(new Color(0, 0, 0, opacity.getDark()));
          }
        } else {
          if (i % 2 == 0) {
            tilePanel.setBackground(new Color(0, 0, 0, opacity.getDark()));
          } else {
            tilePanel.setBackground(new Color(0, 0, 0, opacity.getLight()));
          }
        }
        i++;
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
      Graphics2D g2 = (Graphics2D) g;
      g2.setColor(getBackground());
      Rectangle r = g2.getClipBounds();
      g2.fillRect(r.x, r.y, r.width, r.height);

      if (!board.getTile(this.tileId).isVacant()) {
        PieceColour pieceColour = board.getTile(this.tileId).getPiece().getPieceColour();
        int h = getHeight();
        int w = getWidth();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color colour;
        // Fill circle with solid colour - Black or White
        Settings.PieceColours pieceColours = theme.getPieceColours()[0];
        if (pieceColour.equals(PieceColour.BLACK)) {
          String black = pieceColours.getBlack();
          colour = Color.decode(black);
          g2.setColor(colour);
        } else {
          String white = pieceColours.getWhite();
          colour = Color.decode(white);
          g2.setColor(colour);
        }
        // Fills the circle with solid color
        g2.fillOval(1, 1, w-1, h-1);

        // Adds shadows at the top
        Paint p;
        p = new GradientPaint(0, 0, new Color(0.0f, 0.0f, 0.0f, 0.4f), 0, getHeight(), new Color(0.0f, 0.0f, 0.0f, 0.0f));
        g2.setPaint(p);
        g2.fillOval(1, 1, w-1, h-1);

        // Adds highlights at the bottom
        p = new GradientPaint(0, 0, new Color(1.0f, 1.0f, 1.0f, 0.0f), 0, getHeight(), new Color(1.0f, 1.0f, 1.0f, 0.4f));
        g2.setPaint(p);
        g2.fillOval(1, 1, w-1, h-1);

        // Creates dark edges for 3D effect
        p = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0,
            getHeight() / 2.0), getWidth() / 2.0f,
            new float[] { 0.0f, 1.0f },
            new Color[] { new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), 1/2), new Color(0.0f, 0.0f, 0.0f, 0.1f) });
        g2.setPaint(p);
        g2.fillOval(1, 1, w-1, h-1);

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
        g2.fillOval(1, 1, w-1, h-1);

        // Adds oval specular highlight at the top left
        p = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0,
            getHeight() / 2.0), getWidth() / 1.4f,
            new Point2D.Double(45.0, 25.0),
            new float[] { 0.0f, 0.5f },
            new Color[] { new Color(1.0f, 1.0f, 1.0f, 0.4f), new Color(1.0f, 1.0f, 1.0f, 0.0f) },
            RadialGradientPaint.CycleMethod.NO_CYCLE);
        g2.setPaint(p);
        g2.fillOval(1, 1, w-1, h-1);

        super.paintComponent(g2);
      } else {
        super.paintComponent(g2);
      }
    }

  }

  private class CurrentTurnPanel extends JPanel {
    JLabel currentTurnLabel;

    CurrentTurnPanel() {
      super(new FlowLayout());
      this.currentTurnLabel = new JLabel();
      add(currentTurnLabel);
    }

    private void setLabelText() {
      currentTurnLabel.setText("Current Player");
    }

    private void setColours() {
      Color black = Color.decode(theme.getPieceColours()[0].getBlack());
      Color white = Color.decode(theme.getPieceColours()[0].getWhite());
      if (game.getCurrentPlayer().getPlayerColour().equals(PieceColour.BLACK)) {
        this.setBackground(black);
        currentTurnLabel.setForeground(white);
      } else {
        this.setBackground(white);
        currentTurnLabel.setForeground(black);
      }
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      setColours();
      setLabelText();
    }

    private void updatePlayer() {
      currentTurnLabel.setText("Current player: " + game.getCurrentPlayer().getPlayerColour());
    }
  }

  private class PlayerInfoPanel extends JPanel {
    private JPanel panel1;
    private JPanel panel2;

    private JLabel player1Icon;
    private JLabel player2Icon;
    private JLabel player1TimeIcon;
    private JLabel player2TimeIcon;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JLabel player1TimeLabel;
    private JLabel player2TimeLabel;

    private Font customFont;
    private Font standardFont;

    public PlayerInfoPanel() {
      createPlayerPanels();
      createLabels();
      setColours();
      registerFont();
      setLabelFont();
      setScoreLabelText();
      setTimeLabelText();

      panel1.add(player1Icon);
      panel1.add(player1ScoreLabel);
      panel1.add(player1TimeIcon);
      panel1.add(player1TimeLabel);

      panel2.add(player2Icon);
      panel2.add(player2ScoreLabel);
      panel2.add(player2TimeIcon);
      panel2.add(player2TimeLabel);

      add(panel1);
      add(panel2);
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      setColours();
      setScoreLabelText();
      setTimeLabelText();
    }

    private void createPlayerPanels() {
      panel1 = new JPanel(new GridLayout(1, 4));
      panel2 = new JPanel(new GridLayout(1, 4));
    }

    private void createLabels() {
      player1Icon = new JLabel("", SwingConstants.CENTER);
      player1ScoreLabel = new JLabel();
      player1TimeIcon = new JLabel("", SwingConstants.RIGHT);
      player1TimeLabel = new JLabel("", SwingConstants.CENTER);

      player2Icon = new JLabel("", SwingConstants.CENTER);
      player2ScoreLabel = new JLabel();
      player2TimeIcon = new JLabel("", SwingConstants.RIGHT);
      player2TimeLabel = new JLabel("", SwingConstants.CENTER);
    }

    private void setColours() {
      Color black = Color.decode(theme.getPieceColours()[0].getBlack());
      Color white = Color.decode(theme.getPieceColours()[0].getWhite());

      panel1.setBackground(black);
      player1Icon.setForeground(white);
      player1ScoreLabel.setForeground(white);
      player1TimeIcon.setForeground(white);
      player1TimeLabel.setForeground(white);

      panel2.setBackground(white);
      player2Icon.setForeground(black);
      player2ScoreLabel.setForeground(black);
      player2TimeIcon.setForeground(black);
      player2TimeLabel.setForeground(black);
    }

    private void registerFont() {
      try {
        customFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/themes/fontawesome-webfont.ttf"));
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);
        customFont = new Font(customFont.getName(), Font.PLAIN, GAME_HEIGHT * 5/100);
        standardFont = new Font("Tahoma", Font.PLAIN, GAME_HEIGHT * 4/100);
      } catch (IOException | FontFormatException e) {
        e.printStackTrace();
      }
    }

    private void setLabelFont() {
      player1Icon.setFont(customFont);
      player1ScoreLabel.setFont(standardFont);
      player1TimeIcon.setFont(customFont);
      player1TimeLabel.setFont(standardFont);

      player2Icon.setFont(customFont);
      player2ScoreLabel.setFont(standardFont);
      player2TimeIcon.setFont(customFont);
      player2TimeLabel.setFont(standardFont);
    }

    private void setScoreLabelText() {
      player1Icon.setText("\uF2C0");
      player1ScoreLabel.setText(Integer.toString(game.getBoard().getPieceCount(PieceColour.BLACK)));
      player2Icon.setText("\uF2C0");
      player2ScoreLabel.setText(Integer.toString(game.getBoard().getPieceCount(PieceColour.WHITE)));
    }

    private void setTimeLabelText() {
      int player1SecondsLeft = game.getPlayers()[0].getTimeLeftToPlayInSeconds();
      int player2SecondsLeft = game.getPlayers()[1].getTimeLeftToPlayInSeconds();

      String s1 = Character.toString('\u221e');
      String s2 = Character.toString('\u221e');
      if (game.isTimedGame()) {
        s1 = String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes(player1SecondsLeft), TimeUnit.SECONDS.toSeconds(player1SecondsLeft) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(player1SecondsLeft)));
        s2 = String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes(player2SecondsLeft), TimeUnit.SECONDS.toSeconds(player2SecondsLeft) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(player2SecondsLeft)));
      }

      player1TimeIcon.setText("\uF252");
      player1TimeLabel.setText(s1);
      player2TimeIcon.setText("\uF252");
      player2TimeLabel.setText(s2);
    }
  }

  private JMenuBar createMenuBar() {
    JMenuBar menubar = new JMenuBar();

    JMenu menu = new JMenu("Menu");
    JMenu options = new JMenu("Options");

    menu.setMnemonic(KeyEvent.VK_M);
    options.setMnemonic(KeyEvent.VK_O);

    backToMainMenuItem = new JMenuItem("Main Menu");
    backToMainMenuItem.setToolTipText("Go back to main menu");

    exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.setToolTipText("Exit application");

    rulesMenuItem = new JMenuItem("Rules");
    rulesMenuItem.setToolTipText("Learn the rules of Reversi");

    settingsMenuItem = new JMenuItem("Settings");
    settingsMenuItem.setToolTipText("Change the theme of the Reversi board.");

    menu.add(backToMainMenuItem);
    menu.add(exitMenuItem);
    options.add(rulesMenuItem);
    options.add(settingsMenuItem);

    menubar.add(menu);
    menubar.add(options);

    return menubar;
  }
}
