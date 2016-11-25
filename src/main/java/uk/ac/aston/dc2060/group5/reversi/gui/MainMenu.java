package uk.ac.aston.dc2060.group5.reversi.gui;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import java.io.InputStream;

/**
 * Created by Maryam on 22/11/2016.
 */
public class MainMenu extends JFrame{

    public MainMenu() throws IOException {
        //Create main window and set exit on close
        JFrame frame = new JFrame("Reversi Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600,975));
        frame.setLayout(new GridLayout(0, 1));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.GREEN);

        frame.setVisible(true);


        //Get background image and set to background

        InputStream imageStream = this.getClass().getResourceAsStream("/menu/reversi.png");
        JLabel background = new JLabel(new ImageIcon(ImageIO.read(imageStream)));

        //Set Panel background
        JPanel panel = new JPanel();
        panel.setBounds(0,-100, 600,975);
        panel.add(background);



        //Menu options
        JButton option1 = new JButton("Player vs Player");
        JButton option2 = new JButton("Player vs Al");
        JButton exit = new JButton("Exit");


        //Actions when button clicked

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });

        option1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BoardUI();
            }
        });




        //set panel menu options
        JPanel panel2 = new JPanel();
        panel2.setBounds(225,300, 150,150);
        panel2.setOpaque(false);
        panel2.add(option1);
        panel2.add(option2);
        panel2.add(exit);




        JLayeredPane master = new JLayeredPane();
        master.add(panel, new Integer(0), 0);
        master.add(panel2, new Integer(1), 0);

        frame.add(master);

        //Display
        frame.pack();

    }

    /*public static void main(String[] args) throws IOException {
        new MainMenu();
    }*/


}

