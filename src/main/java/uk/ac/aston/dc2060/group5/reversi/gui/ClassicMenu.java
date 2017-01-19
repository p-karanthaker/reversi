package uk.ac.aston.dc2060.group5.reversi.gui;

import uk.ac.aston.dc2060.group5.reversi.ReversiEngine;
import uk.ac.aston.dc2060.group5.reversi.rulesets.AbstractGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.ClassicGame;
import uk.ac.aston.dc2060.group5.reversi.rulesets.GameType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Maryam on 17/01/2017.
 */
public class ClassicMenu extends JFrame {


    // Allows us to create a suitable size window for any screen resolution.
    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private final int MENU_HEIGHT = SCREEN_SIZE.height * 4 / 5;
    private final int MENU_WIDTH = SCREEN_SIZE.width * 1 / 3;

    public ClassicMenu() throws IOException {
        //Create main window and set exit on close
        JFrame frame = new JFrame("Classic Game Menu");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JButton buttonPvP = new JButton("Player vs Player");
        JButton buttonPvC = new JButton("Player vs Al");
        JCheckBox checkBoxHardDifficulty = new JCheckBox("Hard Difficulty?");
        JButton exit = new JButton("Exit");
        JButton buttonBack = new JButton("Back");


        //Actions when button clicked
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        buttonPvP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractGame game = new ClassicGame(GameType.PVP, false);
                new ReversiEngine(game, new BoardUI(game));
                frame.dispose();
            }
        });

        buttonPvC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractGame game = new ClassicGame(GameType.PVC, checkBoxHardDifficulty.isSelected());
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
        panel2.setSize(MENU_WIDTH / 4, MENU_HEIGHT);
        int xOffset = (MENU_WIDTH / 2) - (MENU_WIDTH / 8);
        int yOffset = (MENU_HEIGHT / 2) - (MENU_HEIGHT / 8);
        panel2.setBounds(xOffset, yOffset, panel2.getWidth(), panel2.getHeight());

        panel2.setOpaque(false);
        panel2.add(buttonPvP);
        panel2.add(buttonPvC);
        panel2.add(checkBoxHardDifficulty);
        panel2.add(exit);
        panel2.add(buttonBack);

        JLayeredPane master = new JLayeredPane();
        master.add(panel, new Integer(0), 0);
        master.add(panel2, new Integer(1), 0);

        frame.add(master);

        //Display
        frame.pack();
    }
}
