package uk.ac.aston.dc2060.group5.reversi.gui;

import uk.ac.aston.dc2060.group5.reversi.ReversiEngine;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AntiReversi;
import uk.ac.aston.dc2060.group5.reversi.rulesets.GameType;
import uk.ac.aston.dc2060.group5.reversi.rulesets.TimeMode;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Created by Maryam on 17/01/2017.
 */
public class AntiReversiMenu {

  // Allows us to create a suitable size window for any screen resolution.
  private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

  private final int MENU_HEIGHT = SCREEN_SIZE.height * 4 / 5;
  private final int MENU_WIDTH = SCREEN_SIZE.width * 1 / 3;
  private TimeMode gameTimeMode;

  public AntiReversiMenu() throws IOException {
    //Create main window and set exit on close
    JFrame frame = new JFrame("Anti Reversi Game Menu");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
    frame.setLayout(new GridLayout(0, 1));
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.getContentPane().setBackground(Color.GREEN);
    frame.setResizable(false);
    frame.setVisible(true);


    //Get background image and set to background

    InputStream imageStream = this.getClass().getResourceAsStream("/menu/reversi.png");

    // Scale down the image to fit the frame
    ImageIcon image = null;
    try {
      image = new ImageIcon(new ImageIcon(ImageIO.read(imageStream))
          .getImage().getScaledInstance(MENU_WIDTH, MENU_HEIGHT, Image.SCALE_SMOOTH));
    } catch (final IOException e) {
      e.printStackTrace();
    }

    JLabel background = new JLabel(image);

    //Set Panel background
    JPanel panel = new JPanel();
    panel.setBounds(0, -5, MENU_WIDTH, MENU_HEIGHT);
    panel.add(background);

    //MainMenu options
    JButton buttonPvP = createButtons("Player vs Player");
    JButton buttonPvC = createButtons("Player vs Al");
    JCheckBox checkBoxTimedGame = createCheckBox("Timer?");
    JButton exit = createButtons("Exit");
    JButton buttonBack = createButtons("Back");


    //Actions when button clicked
    exit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });

    checkBoxTimedGame.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (checkBoxTimedGame.isSelected()) {
          //Create main window and set exit on close
          JFrame dialogFrame = new JFrame();
          JDialog dialog = new JDialog(dialogFrame, "Timer Options", Dialog.ModalityType.APPLICATION_MODAL);
          dialog.setLayout(new GridLayout(0, 1));
          dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

          dialog.add(new JLabel("Modes:", SwingConstants.CENTER));
          dialog.add(new JLabel(TimeMode.BLITZ.getDescription(), SwingConstants.CENTER));
          dialog.add(new JLabel(TimeMode.NORMAL.getDescription(), SwingConstants.CENTER));
          dialog.add(new JLabel(TimeMode.COMPETITION.getDescription(), SwingConstants.CENTER));

          dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
              super.windowClosing(e);
              checkBoxTimedGame.setSelected(false);
            }
          });

          JComboBox jComboBox = new JComboBox(TimeMode.values());
          JButton okButton = new JButton("OK");
          okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              TimeMode chosenMode = (TimeMode) jComboBox.getSelectedItem();
              gameTimeMode = chosenMode;
              dialogFrame.dispose();
            }
          });

          dialog.add(jComboBox);
          dialog.add(okButton);
          dialog.setResizable(true);
          dialog.pack();
          dialog.setLocationRelativeTo(null);
          dialog.setVisible(true);
        }
      }
    });


    buttonPvP.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AbstractGame game;
        if (checkBoxTimedGame.isSelected()) {
          game = new AntiReversi(GameType.PVP, gameTimeMode.getTimeModeLengthInSeconds());
        } else {
          game = new AntiReversi(GameType.PVP);
        }
        new ReversiEngine(game, new BoardUI(game));
        frame.dispose();
      }
    });

    buttonPvC.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AbstractGame game;
        if (checkBoxTimedGame.isSelected()) {
          game = new AntiReversi(GameType.PVC, gameTimeMode.getTimeModeLengthInSeconds());
        } else {
          game = new AntiReversi(GameType.PVC);
        }
        new ReversiEngine(game, new BoardUI(game));
        frame.dispose();
      }
    });

    buttonBack.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          MainMenu mainMenu = new MainMenu();
          frame.dispose();
        } catch (IOException e1) {
          e1.printStackTrace();
        }

      }
    });

    // Set panel menu options
    JPanel panel2 = new JPanel();

    // Makes sure dimension values are nice for all screen sizes.
    panel2.setLayout(new GridLayout(0, 1, 5, 5));
    panel2.setSize(MENU_WIDTH / 2, MENU_HEIGHT / 4);

    int xOffset = (MENU_WIDTH / 4);
    int yOffset = (MENU_HEIGHT / 4);
    panel2.setBounds(xOffset, yOffset, panel2.getWidth(), panel2.getHeight());

    panel2.setOpaque(false);
    panel2.add(buttonPvP);
    panel2.add(buttonPvC);
    panel2.add(checkBoxTimedGame);
    panel2.add(exit);
    panel2.add(buttonBack);

    JLayeredPane master = new JLayeredPane();
    master.add(panel, new Integer(0), 0);
    master.add(panel2, new Integer(1), 1);

    frame.add(master);

    //Display
    frame.pack();
  }

  private JButton createButtons(String buttonText) {
    JButton button = new JButton(buttonText);
    button.setFocusPainted(false);
    button.setOpaque(false);
    button.setContentAreaFilled(false);
    button.setForeground(Color.WHITE);
    button.setFont(registerFont());
    return button;
  }

  private JCheckBox createCheckBox(String checkboxText) {
    JCheckBox checkBox = new JCheckBox(checkboxText);
    checkBox.setForeground(Color.WHITE);
    checkBox.setBorderPainted(true);
    checkBox.setOpaque(false);
    checkBox.setHorizontalAlignment(SwingConstants.CENTER);
    checkBox.setFont(registerFont());
    return checkBox;
  }

  private Font registerFont() {
    Font customFont = new Font("Tahoma", Font.BOLD, MENU_WIDTH * 2 / 100);
    try {
      customFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/themes/Xolonium-Regular.ttf"));
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(customFont);
      customFont = new Font(customFont.getName(), Font.PLAIN, MENU_WIDTH * 3 / 100);
    } catch (IOException | FontFormatException e) {
      e.printStackTrace();
    }
    return customFont;
  }
}
