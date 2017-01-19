package uk.ac.aston.dc2060.group5.reversi.gui;

import uk.ac.aston.dc2060.group5.reversi.ReversiEngine;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.ClassicGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.GameType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * Created by Maryam on 22/11/2016.
 */
public class MainMenu extends JFrame {

  // Allows us to create a suitable size window for any screen resolution.
  private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

  private final int MENU_HEIGHT = SCREEN_SIZE.height * 4 / 5;
  private final int MENU_WIDTH = SCREEN_SIZE.width * 1 / 3;

  public MainMenu() throws IOException {
    //Create main window and set exit on close
    JFrame frame = new JFrame("Reversi Main Menu");
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

    //MainMenu, NewGameMenu, ClassicMenu and AntiReversiMenu options
    JButton buttonClassic = createButtons("Classic");
    JButton buttonAntiReversi = createButtons("AntiReversi");
    JButton buttonDemo = createButtons("Demo");
    JButton buttonInstructions = createButtons("Instructions");
    JButton exit = createButtons("Exit");


    //Actions when button clicked
    exit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });


    buttonClassic.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          ClassicMenu classicMenu = new ClassicMenu();
          frame.dispose();

        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    });

    buttonAntiReversi.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          AntiReversiMenu antiReversiMenu = new AntiReversiMenu();
          frame.dispose();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    });


    buttonDemo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AbstractGame game = new ClassicGame(GameType.DEMO, false);
        new ReversiEngine(game, new BoardUI(game));
        frame.dispose();
      }
    });

    buttonInstructions.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new RulesUI();
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
    panel2.add(buttonClassic);
    panel2.add(buttonAntiReversi);
    panel2.add(buttonDemo);
    panel2.add(buttonInstructions);
    panel2.add(exit);

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
